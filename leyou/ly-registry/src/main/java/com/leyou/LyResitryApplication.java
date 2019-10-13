package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @Author zhangfeixiang
 * @Date 2019/10/13 15:34
 * @Version 1.0
 */
@SpringBootApplication
@EnableEurekaServer
public class LyResitryApplication {
	public static void main(String[] args) {
		SpringApplication.run(LyResitryApplication.class, args);
	}
}
