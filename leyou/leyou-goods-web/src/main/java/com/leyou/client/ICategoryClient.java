package com.leyou.client;

import com.leyou.item.api.ICategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author zhangfeixiang
 * @Date 2019/11/7 13:14
 * @Version 1.0
 */
@FeignClient(serviceId ="Item-service")
public interface ICategoryClient extends ICategoryApi {
}
