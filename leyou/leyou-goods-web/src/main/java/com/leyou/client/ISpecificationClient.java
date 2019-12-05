package com.leyou.client;

import com.leyou.item.api.ISpecificationApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author zhangfeixiang
 * @Date 2019/11/1 10:33
 * @Version 1.0
 */
@FeignClient(serviceId = "Item-service")
public interface ISpecificationClient extends ISpecificationApi {
}
