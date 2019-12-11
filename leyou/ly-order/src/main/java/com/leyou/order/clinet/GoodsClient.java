package com.leyou.order.clinet;

import com.leyou.item.api.IGoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author zhangfeixiang
 * @Date 2019/12/6 12:55
 * @Version 1.0
 */
@FeignClient("item-service")
public interface GoodsClient  extends IGoodsApi {
}
