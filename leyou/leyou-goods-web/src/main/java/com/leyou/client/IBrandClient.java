package com.leyou.client;

import com.leyou.item.api.IBrandApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author zhangfeixiang
 * @Date 2019/11/7 14:14
 * @Version 1.0
 */
@FeignClient(serviceId ="Item-service")
public interface IBrandClient extends IBrandApi {
}
