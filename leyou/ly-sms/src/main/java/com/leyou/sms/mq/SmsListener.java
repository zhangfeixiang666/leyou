package com.leyou.sms.mq;

import com.leyou.common.util.JsonUtils;
import com.leyou.sms.config.SmsConfig;
import com.leyou.sms.util.SmsUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * @Author zhangfeixiang
 * @Date 2019/11/23 19:03
 * @Version 1.0
 */
@Slf4j
@Component
@EnableConfigurationProperties(SmsConfig.class)
public class SmsListener {
	@Autowired
	private SmsConfig config;
	@Autowired
	private SmsUtil smsUtil;
	@RabbitListener(bindings=@QueueBinding(
			value = @Queue(name = "sms.verify.code.queue",durable = "true"),
			exchange = @Exchange(name = "ly.sms.exchange",type = ExchangeTypes.TOPIC),
			key = "sms.verify.code"
	))
	public void listenSms(Map<String, String> msg){
		if (CollectionUtils.isEmpty(msg)){
			return;
		}
		String phone = msg.remove("phone");
		if (StringUtils.isBlank(phone)){
			return;
		}
		smsUtil.sendSms(phone, JsonUtils.serialize(msg), config.getSignName(), config.getVerifyCodeTemplate());
		log.info("[短信服务]，发送短信验证码，手机号：{}", phone);
	}
}
