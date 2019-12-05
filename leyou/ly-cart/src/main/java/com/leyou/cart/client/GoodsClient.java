package com.leyou.cart.client;

import com.leyou.item.api.IGoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author zhangfeixiang
 * @Date 2019/11/29 11:35
 * @Version 1.0
 */
@FeignClient("user-service")
public interface GoodsClient extends IGoodsApi {
}
