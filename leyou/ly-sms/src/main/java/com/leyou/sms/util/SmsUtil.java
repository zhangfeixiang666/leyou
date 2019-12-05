package com.leyou.sms.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.leyou.sms.config.SmsConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Author zhangfeixiang
 * @Date 2019/11/23 17:03
 * @Version 1.0
 */
@Component
@EnableConfigurationProperties(SmsConfig.class)
public class SmsUtil {
	@Autowired
	private  SmsConfig config;
	@Autowired
	private StringRedisTemplate redisTemplate;

	//产品名称:云通信短信API产品,开发者无需替换
	static final String product = "Dysmsapi";
	//产品域名,开发者无需替换
	static final String domain = "dysmsapi.aliyuncs.com";

	private static final String KEY_PREFIX = "sms:phone";
	private static final long SMS_MIN_INTERVAL_IN_MILLIS = 60000;

	static final Logger logger = LoggerFactory.getLogger(SmsUtil.class);

	public SendSmsResponse sendSms(String phone, String code, String signName, String template) {
		String key = KEY_PREFIX + phone;
		try{
			String lastTime = redisTemplate.opsForValue().get(key);
			if(StringUtils.isNotBlank(lastTime)){
				if (System.currentTimeMillis() - Long.parseLong(lastTime) < SMS_MIN_INTERVAL_IN_MILLIS){
					logger.info("【短信服务】短信发送频繁，请稍后再试，手机号码：{}",phone);

				}
			}

			//可自助调整超时时间
			System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
			System.setProperty("sun.net.client.defaultReadTimeout", "10000");

			//初始化acsClient,暂不支持region化
			IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou",
					config.getAccessKeyId(), config.getAccessKeySecret());
			DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
			IAcsClient acsClient = new DefaultAcsClient(profile);

			//组装请求对象-具体描述见控制台-文档部分内容
			SendSmsRequest request = new SendSmsRequest();
			request.setMethod(MethodType.POST);
			//必填:待发送手机号
			request.setPhoneNumbers(phone);
			//必填:短信签名-可在短信控制台中找到
			request.setSignName(signName);
			//必填:短信模板-可在短信控制台中找到
			request.setTemplateCode(template);
			//可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
			request.setTemplateParam(code);

			//选填-上行短信扩展码(无特殊需求用户请忽略此字段)
			//request.setSmsUpExtendCode("90997");

			//可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
			//request.setOutId("123456");

			//hint 此处可能会抛出异常，注意catch
			SendSmsResponse resp = acsClient.getAcsResponse(request);
			if (!"OK".equals(resp.getCode())) {
				logger.info("【短信服务】发送短信失败，phone：{},原因：{}", phone, resp.getMessage());
			}
			logger.info("【短信服务】发送短信验证码,手机号码：{}",phone);
			redisTemplate.boundValueOps(key).set(String.valueOf(System.currentTimeMillis()),1, TimeUnit.MINUTES);
			return resp;
		}catch (Exception e){
			logger.info("短信发送异常：{},手机号码：{}",e,phone);
			return null;
		}

	}
}
