package com.leyou.sms.mq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author zhangfeixiang
 * @Date 2019/11/24 8:52
 * @Version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SmsListenerTest {
	@Autowired
	private AmqpTemplate amqpTemplate;
	@Test
	public void sendSms(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("phone", "18337256218");
		map.put("code", "6666");
		amqpTemplate.convertAndSend("ly.sms.exchange", "sms.verify.code", map);


	}
}