package com.leyou.order.service;

import com.leyou.auth.entry.UserInfo;
import com.leyou.common.Exception.LyException;
import com.leyou.common.ExceptionEnum;
import com.leyou.common.util.IdWorker;
import com.leyou.common.vo.CartDTO;
import com.leyou.item.pojo.Sku;
import com.leyou.order.clinet.GoodsClient;
import com.leyou.order.dto.OrderDTO;
import com.leyou.order.interceptor.LoginInterceptor;
import com.leyou.order.mapper.OrderDetailMapper;
import com.leyou.order.mapper.OrderMapper;
import com.leyou.order.mapper.OrderStatusMapper;
import com.leyou.order.pojo.*;
import com.leyou.utils.PayHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author zhangfeixiang
 * @Date 2019/12/5 19:35
 * @Version 1.0
 */
@Slf4j
@Service
public class OrderService {
	@Autowired
	private IdWorker idWorker;
	@Autowired
	private GoodsClient goodsClient;
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	OrderDetailMapper orderDetailMapper;
	@Autowired
	private OrderStatusMapper orderStatusMapper;
	@Autowired
	private PayHelper payHelper;
	@Transactional
	public Long createOderder(OrderDTO orderDTO) {
		//新增订单
		//获取登录用户
		UserInfo userInfo = LoginInterceptor.getLoginUser();
		Order order = new Order();
		//1.1订单编号，基本信息
		long orderId = idWorker.nextId();
		order.setOrderId(orderId);
		order.setCreateTime(new Date());
		order.setPaymentType(1);
		//1.2用户信息
		order.setBuyerNick(userInfo.getUsername());
		order.setBuyerRate(false);
		order.setUserId(userInfo.getId());
		//1.3收货人地址
		order.setReceiver("沐橙");
		order.setReceiverAddress("斗罗大陆，天斗成，1号院");
		order.setReceiverCity("天斗成");
		order.setReceiverMobile("18337256218");
		order.setReceiverState("天斗");
		order.setReceiverZip("310001");
		order.setReceiverDistrict("史莱克");
		//1.4金额
		List<CartDTO> carts = orderDTO.getCarts();
		//list 为map
		Map<Long, Integer> numMap = carts.stream().collect(Collectors.toMap(CartDTO::getSkuId, CartDTO::getNum));
		Set<Long> longs = numMap.keySet();
		List<Long> ids =  new ArrayList(longs);
		List<Sku> skus = goodsClient.querySkusByIds(ids);
		List<OrderDetail> details = new ArrayList<>();
		Long totalNum = 0L;
		for (Sku sku : skus) {
			Integer num = numMap.get(sku.getId());
			totalNum += sku.getPrice() * num;
			OrderDetail orderDetail = new OrderDetail();
			orderDetail.setImage(StringUtils.substringBefore(sku.getImages(),","));
			orderDetail.setNum(num);
			orderDetail.setOwnSpec(sku.getOwnSpec());
			orderDetail.setPrice(sku.getPrice());
			orderDetail.setOrderId(orderId);
			orderDetail.setSkuId(sku.getId());
			orderDetail.setTitle(sku.getTitle());
			details.add(orderDetail);
		}
		order.setTotalPay(totalNum);
		order.setActualPay(totalNum);
		order.setPostFee("0");
		int i = orderMapper.insertSelective(order);
		if (i != 1){
			throw new LyException(ExceptionEnum.ORDER_CREATE_FAILED);
		}
		//新增订单详情
		int count = orderDetailMapper.insertList(details);
		if (count != details.size()){
			throw new LyException(ExceptionEnum.ORDER_CREATE_FAILED);
		}
		//新增订单状态
		OrderStatus orderStatus = new OrderStatus();
		orderStatus.setOrderId(orderId);
		orderStatus.setStatus(PayState.UN_PAY.value());
		orderStatus.setCreateTime(new Date());
		count = orderStatusMapper.insertSelective(orderStatus);
		if (count != 1){
			throw new LyException(ExceptionEnum.ORDER_CREATE_FAILED);
		}
		//减库存
		goodsClient.desStock(carts);
		return  orderId;
	}

	public Order findOrder(Long orderId) {
		Order order = orderMapper.selectByPrimaryKey(orderId);
		if (order == null){
			throw new LyException(ExceptionEnum.SELECT_ORDER_FAIL);
		}
		//查询orderState
		OrderStatus orderStatus = orderStatusMapper.selectByPrimaryKey(orderId);
		if (orderStatus == null){
			throw new LyException(ExceptionEnum.SELECT_ORDERSTATUS_FAIL);
		}
		order.setStatus(orderStatus);
		//查询订单详情
		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setOrderId(orderId);
		List<OrderDetail> details = orderDetailMapper.select(orderDetail);
		if (CollectionUtils.isEmpty(details)){
			throw new LyException(ExceptionEnum.SELECT_ORDERDETAILS_FAIL);
		}
		order.setOrderDetails(details);
		return order;
	}

	public String createPayUrl(Long orderId) {
		//查询订单
		Order order = findOrder(orderId);
		if (order == null){
			throw new LyException(ExceptionEnum.SELECT_ORDER_FAIL);
		}
		//判断订单的状态
		Integer status = order.getStatus().getStatus();
		if (status != PayState.UN_PAY.value()){
			throw new LyException(ExceptionEnum.ORDER_PAYED);
		}
		//获取描述
		String title = order.getOrderDetails().get(0).getTitle();
		return payHelper.createOrder(orderId, 1L, "张飞翔666");

	}

	public void handleNotify(Map<String, String> result) {
		//数据校验
		payHelper.isSuccess(result);
		//校验签名
		payHelper.isValidSign(result);
		//判断金额是否一致
		String totalFeeStr = result.get("total_fee");
		if (StringUtils.isBlank(totalFeeStr)){
			throw new LyException(ExceptionEnum.PARAM_IS_NULL);
		}
		Long totalFee = Long.valueOf(totalFeeStr);
		String tradeNo = result.get("out_trade_no");
		if (StringUtils.isBlank(tradeNo)){
			throw new LyException(ExceptionEnum.PARAM_IS_NULL);
		}
		Long orderId = Long.valueOf(tradeNo);
		Order order = orderMapper.selectByPrimaryKey(orderId);
		if (totalFee.intValue() != 1){
			throw new LyException(ExceptionEnum.MONEY_IS_NULL);
		}
		OrderStatus orderStatus = new OrderStatus();
		orderStatus.setOrderId(orderId);
		orderStatus.setStatus(PayState.PAY.value());
		orderStatus.setPaymentTime(new Date());
		//修改订单状态
		int i = orderStatusMapper.updateByPrimaryKey(orderStatus);
		if (i !=1){
			throw new LyException(ExceptionEnum.SELECT_ORDERSTATUS_FAIL);
		}
		log.info("订单正确，id{}",orderId);

	}

	public Integer queryOrderState(Long orderId) {
		OrderStatus orderStatus = orderStatusMapper.selectByPrimaryKey(orderId);
		//未付款
		if (orderStatus.getStatus() != PayState.UN_PAY.value()){
			return OrderState.UN_PAY.value();
		}
		return payHelper.queryPayState(orderId);
	}
}
