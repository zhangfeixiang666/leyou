package com.leyou.sms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author zhangfeixiang
 * @Date 2019/11/23 16:59
 * @Version 1.0
 */
@Data
@ConfigurationProperties(prefix = "leyou.sms")
public class SmsConfig {
	 String accessKeyId;
	 String accessKeySecret;
	 String signName;
	 String verifyCodeTemplate;
}
