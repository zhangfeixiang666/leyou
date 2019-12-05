package com.leyou.auth.client;

import com.leyou.user.api.IUserApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author zhangfeixiang
 * @Date 2019/11/27 9:59
 * @Version 1.0
 */
@FeignClient(value = "user-service")
public interface IUserClient extends IUserApi {
}
