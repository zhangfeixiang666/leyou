package com.leyou.order.controller;

import com.leyou.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author zhangfeixiang
 * @Date 2019/12/18 20:55
 * @Version 1.0
 */
@RestController("notify")
public class NotifyController {
	@Autowired
	private OrderService orderService;
	/**
	 * 微信支付成功回调
	 * @param result
	 * @return
	 */
	@PostMapping(value = "pay", produces = "application/xml")
	public Map<String, String> pay(@RequestBody Map<String, String> result){
		//处理回调
		orderService.handleNotify(result);
		//返回成功
		Map<String, String> msg = new HashMap<>();
		msg.put("return_code", "SUCCESS");
		msg.put("return_msg", "OK");
		return msg;

	}
}
