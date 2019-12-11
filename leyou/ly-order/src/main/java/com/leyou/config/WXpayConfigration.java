package com.leyou.config;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author zhangfeixiang
 * @Date 2019/12/10 19:41
 * @Version 1.0
 */
@Configuration
public class WXpayConfigration {

	@Bean
	@ConfigurationProperties("leyou.pay")
	public PayConfig payConfig(){
		return new PayConfig();
	}
	public WXPay wxPay(){
		return new WXPay(payConfig(), WXPayConstants.SignType.HMACSHA256);
	}
}
