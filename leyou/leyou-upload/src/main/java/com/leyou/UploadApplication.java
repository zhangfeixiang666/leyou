package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author zhangfeixiang
 * @Date 2019/10/18 15:10
 * @Version 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
public class UploadApplication {
	public static void main(String[] args) {
		SpringApplication.run(UploadApplication.class, args);
	}
}
