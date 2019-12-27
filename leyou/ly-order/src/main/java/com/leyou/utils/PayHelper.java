package com.leyou.utils;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.leyou.common.Exception.LyException;
import com.leyou.common.ExceptionEnum;
import com.leyou.config.PayConfig;
import com.leyou.order.mapper.OrderMapper;
import com.leyou.order.mapper.OrderStatusMapper;
import com.leyou.order.pojo.Order;
import com.leyou.order.pojo.OrderState;
import com.leyou.order.pojo.OrderStatus;
import com.leyou.order.pojo.PayState;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;




/**
 * @Author zhangfeixiang
 * @Date 2019/12/10 19:58
 * @Version 1.0
 */
@Component
@Slf4j
public class PayHelper {
	@Autowired
	private OrderStatusMapper orderStatusMapper;
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private WXPay wxPay;

	@Autowired
	private PayConfig payConfig;

	public String createOrder(Long orderId, Long totalPay, String desc){
		try {
			Map<String, String> data = new HashMap<>();
			//商品描述
			data.put("body", desc);
			//订单号
			data.put("out_trade_no", orderId.toString());
			//金额，单位：分
			data.put("total_fee", "1");
			//调用微信支付的终端ip
			data.put("spbill_create_ip", "127.0.0.1");
			//回调地址
			data.put("notify_url", payConfig.getNotifyUrl());
			//交易支付类型为扫码支付
			data.put("trade_type", "NATIVE");
			//利用wxPay工具完成下单
			Map<String, String> result = wxPay.unifiedOrder(data);
			//判断业务和通信标识
			isSuccess(result);
			//校验密匙
			isValidSign(result);
			//下单成功 获取支付链接
			String url = result.get("code_url");

			return url;
		} catch (Exception e) {
			log.error("支付出现异常，订单ID：{}", orderId);
			e.printStackTrace();
		}
		return null;

	}
	public void isSuccess(Map<String,String> result){
		//判断通信标识
		String returnCode = result.get("return_code");
		if (WXPayConstants.FAIL.equals(returnCode)){
			//通信失败
			log.error("[微信下单]微信下单失败原因，错误码:{}", result.get("return_msg"));
			throw new LyException(ExceptionEnum.WX_PAY_ORDER_FAILED);
		}
		//判断业务标识
		String resultCode = result.get("result_code");
		if (WXPayConstants.FAIL.equals(resultCode)){
			//通信失败
			log.error("[微信下单]微信下单失败原因，错误码:{},错误原因：{}", result.get("err_code"), result.get("err_code_des"));
			throw new LyException(ExceptionEnum.WX_PAY_ORDER_FAILED);
		}
	}
	public void isValidSign(Map<String, String> result){
		try {
			//重新生成签名
			String sign1 = WXPayUtil.generateSignature(result, payConfig.getKey(), WXPayConstants.SignType.HMACSHA256);
			String sign2 = WXPayUtil.generateSignature(result, payConfig.getKey(), WXPayConstants.SignType.MD5);
			//和传过来的签名进行比较
			String sign = result.get("sign");
			if (!StringUtils.equals(sign1, sign) && !StringUtils.equals(sign, sign2)){
				throw new LyException(ExceptionEnum.INVALID_SIGN_ERROR);
			}
		} catch (Exception e) {
			throw new LyException(ExceptionEnum.INVALID_SIGN_ERROR);

		}

	}
	public Integer  queryPayState(Long orderId){
		try {
			//组织请求参数
			Map<String, String> data = new HashMap<>();
			//订单号
			data.put("out_trade_no", orderId.toString());
			Map<String, String> result = wxPay.orderQuery(data);
			//校验通信状态
			isSuccess(result);
			//校验签名
			isValidSign(result);
			//校验金额
			//判断金额是否一致
			String totalFeeStr = result.get("total_fee");
			if (StringUtils.isBlank(totalFeeStr)){
				throw new LyException(ExceptionEnum.PARAM_IS_NULL);
			}
			Long totalFee = Long.valueOf(totalFeeStr);
			Order order = orderMapper.selectByPrimaryKey(orderId);
			if (totalFee.intValue() != 1){
				throw new LyException(ExceptionEnum.MONEY_IS_NULL);
			}
			String state = result.get("trade_state");
			if ("SUCCESS".equals(state)){
				//修改订单状态
				OrderStatus orderStatus = new OrderStatus();
				orderStatus.setOrderId(orderId);
				orderStatus.setStatus(PayState.PAY.value());
				orderStatus.setPaymentTime(new Date());
				//修改订单状态
				int i = orderStatusMapper.updateByPrimaryKey(orderStatus);
				if (i !=1){
					throw new LyException(ExceptionEnum.SELECT_ORDERSTATUS_FAIL);
				}
				return OrderState.SUCCESS.value();
			}
			if ("NOTPAY".equals(state) || "USERPAYING".equals(state)){
				return OrderState.UN_PAY.value();
			}
			return  OrderState.FAILD.value();
		} catch (Exception e) {
			log.error("支付失败，订单id{}",orderId);
			return  OrderState.FAILD.value();

		}

	}
}
