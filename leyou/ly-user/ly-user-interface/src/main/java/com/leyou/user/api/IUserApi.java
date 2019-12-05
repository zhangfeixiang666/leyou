package com.leyou.user.api;

import com.leyou.user.domain.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author zhangfeixiang
 * @Date 2019/11/27 9:55
 * @Version 1.0
 */
public interface IUserApi {
	/**
	 * 查询用户
	 * @param username
	 * @param password
	 * @return
	 */
	@GetMapping("query")
	User queryUser(@RequestParam("username")String username, @RequestParam("password")String password);

}
