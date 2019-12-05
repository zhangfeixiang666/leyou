package com.leyou.cart.config;

import com.leyou.auth.util.RsaUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PublicKey;

/**
 * @Author zhangfeixiang
 * @Date 2019/11/28 9:40
 * @Version 1.0
 */
@Slf4j
@Data
@ConfigurationProperties(prefix = "ly.jwt")
public class JwtProperties {
	private String cookieName;
	private String pubKeyPath;
	private PublicKey publicKey;
	@PostConstruct
	public void init(){
		File file = new File(pubKeyPath);
		try {
			this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
		} catch (Exception e) {
			log.error("公匙不存在 :{}", e.getMessage());
		}
	}
}
