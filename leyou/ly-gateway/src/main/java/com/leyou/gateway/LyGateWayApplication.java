package com.leyou.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @Author zhangfeixiang
 * @Date 2019/10/13 16:43
 * @Version 1.0
 */
@SpringBootApplication
@EnableZuulProxy
@EnableDiscoveryClient
public class LyGateWayApplication {
	public static void main(String[] args) {
		SpringApplication.run(LyGateWayApplication.class, args);
	}
}
