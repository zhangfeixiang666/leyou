package com.leyou.auth.config;

import com.leyou.auth.util.RsaUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @Author zhangfeixiang
 * @Date 2019/11/26 17:45
 * @Version 1.0
 */
@Data
@ConfigurationProperties(prefix = "ly.jwt")
public class AuthConfig {
	private String secret;
	private String pubKeyPath;
	private String priKeyPath;
	private Integer expire;
	private PublicKey pubKey;
	private PrivateKey priKey;
	private String cookieName;
	@PostConstruct
	public void init() throws Exception {
		File pri = new File(priKeyPath);
		File pub = new File(pubKeyPath);
		if (!pri.exists() || !pub.exists()){
			RsaUtils.generateKey(pubKeyPath, priKeyPath,secret);
		}
		this.priKey = RsaUtils.getPrivateKey(priKeyPath);
		this.pubKey = RsaUtils.getPublicKey(pubKeyPath);
	}
}
