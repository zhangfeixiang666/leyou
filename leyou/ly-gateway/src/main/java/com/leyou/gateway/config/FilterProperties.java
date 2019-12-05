package com.leyou.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @Author zhangfeixiang
 * @Date 2019/11/28 10:11
 * @Version 1.0
 */
@Data
@ConfigurationProperties(prefix = "ly.filter")
public class FilterProperties {
	List<String> allowPaths;
}
