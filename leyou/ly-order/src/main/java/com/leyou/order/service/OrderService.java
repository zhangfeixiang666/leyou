package com.leyou.order.service;

import com.leyou.auth.entry.UserInfo;
import com.leyou.common.Exception.LyException;
import com.leyou.common.ExceptionEnum;
import com.leyou.common.util.IdWorker;
import com.leyou.item.pojo.Sku;
import com.leyou.order.clinet.GoodsClient;
import com.leyou.common.vo.CartDTO;
import com.leyou.order.dto.OrderDTO;
import com.leyou.order.interceptor.LoginInterceptor;
import com.leyou.order.mapper.OrderDetailMapper;
import com.leyou.order.mapper.OrderMapper;
import com.leyou.order.mapper.OrderStatusMapper;
import com.leyou.order.pojo.Order;
import com.leyou.order.pojo.OrderDetail;
import com.leyou.order.pojo.OrderStatus;
import com.leyou.order.pojo.PayState;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author zhangfeixiang
 * @Date 2019/12/5 19:35
 * @Version 1.0
 */
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

}
