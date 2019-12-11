package com.leyou.utils;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.leyou.common.Exception.LyException;
import com.leyou.common.ExceptionEnum;
import com.leyou.config.PayConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
			//判断通信标识
			String returnCode = result.get("return_Code");
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
			//下单成功 获取支付链接
			String url = result.get("code_url");

			return url;
		} catch (Exception e) {
			log.error("支付出现异常，订单ID：{}", orderId);
			e.printStackTrace();
		}
		return null;

	}
}
