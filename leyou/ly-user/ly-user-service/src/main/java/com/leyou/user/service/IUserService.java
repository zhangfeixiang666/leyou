package com.leyou.user.service;

import com.leyou.user.domain.User;

/**
 * @Author zhangfeixiang
 * @Date 2019/11/25 16:57
 * @Version 1.0
 */
public interface IUserService {
	/**
	 * 校验用户
	 * @param data
	 * @param type
	 * @return
	 */
	Boolean checkUser(String data, Integer type);
	/**
	 * 发送短信验证码
	 * @param phone
	 */
	void sendSms(String phone);

	/**
	 * 用户注册
	 * @param user
	 * @param code
	 */
	void register(User user, String code);

	/**
	 * 查询用户
	 * @param username
	 * @param password
	 * @return
	 */
	User queryUser(String username, String password);
}
