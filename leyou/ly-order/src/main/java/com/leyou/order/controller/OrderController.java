package com.leyou.order.controller;

import com.leyou.order.dto.OrderDTO;
import com.leyou.order.pojo.Order;
import com.leyou.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

	/**
	 * 查询订单
	 * @param orderId
	 * @return
	 */
	@GetMapping("{id}")
	public ResponseEntity<Order> findOrder(@PathVariable("id") Long orderId){
		return ResponseEntity.ok(orderService.findOrder(orderId));
	}

	/**
	 * 获取支付链接
	 * @param orderId
	 * @return
	 */
	@GetMapping("url/{id}")
	public ResponseEntity<String> createPayUrl(@PathVariable("id") Long orderId){
		return ResponseEntity.ok(orderService.createPayUrl(orderId));
	}

	/**
	 * 查看订单状态
	 * @param orderId
	 * @return
	 */
	@GetMapping("state/{id}")
	public ResponseEntity<Integer> queryOrderState(@PathVariable("id") Long orderId){
		return ResponseEntity.ok(orderService.queryOrderState(orderId));
	}
}
