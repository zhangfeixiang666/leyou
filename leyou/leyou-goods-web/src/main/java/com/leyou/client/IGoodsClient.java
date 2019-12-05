package com.leyou.client;

import com.leyou.item.api.IGoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author zhangfeixiang
 * @Date 2019/11/1 9:06
 * @Version 1.0
 */
@FeignClient(serviceId ="Item-service")
public interface IGoodsClient extends IGoodsApi {
}
