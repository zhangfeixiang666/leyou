package com.leyou.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @Author zhangfeixiang
 * @Date 2019/10/17 14:37
 * @Version 1.0
 */
@Configuration
public class GlobalCorsConfig {
	@Bean
	public CorsFilter corsFilter(){
		//1.添加CORS配置信息
		CorsConfiguration config = new CorsConfiguration();
		//1.1.添加允许的域名
		config.addAllowedOrigin("http://manage.leyou.com");
		config.addAllowedOrigin("http://www.leyou.com");
		//1.2.是否发送cookie信息
		config.setAllowCredentials(true);
		//1.3.允许请求的方式
		config.addAllowedMethod(HttpMethod.PUT);
		config.addAllowedMethod(HttpMethod.HEAD);
		config.addAllowedMethod(HttpMethod.GET);
		config.addAllowedMethod(HttpMethod.OPTIONS);
		config.addAllowedMethod(HttpMethod.POST);
		config.addAllowedMethod(HttpMethod.DELETE);
		config.addAllowedMethod(HttpMethod.PATCH);
		//1.4.允许的头信息
		config.addAllowedHeader("*");
		//1.5.设置预请求时间
		config.setMaxAge(3600*24L);
		//2.添加映射路径，我们拦截一切请求
		UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
		configSource.registerCorsConfiguration("/**", config);
		//3.返回新的CorsFilter.
		return new CorsFilter(configSource);

	}
}
