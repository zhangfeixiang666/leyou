package com.leyou.order.controller;

import com.leyou.order.dto.OrderDTO;
import com.leyou.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author zhangfeixiang
 * @Date 2019/12/5 19:31
 * @Version 1.0
 */
@RestController
@RequestMapping("order")
public class OrderController {
	@Autowired
	private OrderService orderService;

	/**
	 * 创建订单
	 * @param orderDTO
	 * @return
	 */
	@PostMapping
	public ResponseEntity<Long> createOrder(@RequestBody OrderDTO orderDTO){
		return  ResponseEntity.ok(orderService.createOderder(orderDTO));
	}
}
