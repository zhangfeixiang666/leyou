package com.leyou.cart.interceptor;

import com.leyou.auth.entry.UserInfo;
import com.leyou.auth.util.JwtUtils;
import com.leyou.cart.config.JwtProperties;
import com.leyou.common.util.CookieUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author zhangfeixiang
 * @Date 2019/11/29 10:20
 * @Version 1.0
 */
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
	private JwtProperties jwtProperties;
	//定义共享线程
	private final static ThreadLocal<UserInfo> USER_INFO= new ThreadLocal<>();
	public LoginInterceptor(){}
	public LoginInterceptor(JwtProperties jwtProperties){
		this.jwtProperties = jwtProperties;
	}
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//1.获取token
		String token = CookieUtils.getCookieValue(request, jwtProperties.getCookieName());
		//2.解析token
		try {
			UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
			//存入用户信息
			USER_INFO.set(userInfo);
			return true;
		} catch (Exception e) {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			log.error("[身份鉴别失败，token不正确]");
			return false;
			//e.printStackTrace();
		}

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		//删除用户信息
		USER_INFO.remove();
	}

	/**
	 * 获取user
	 * @return
	 */
	public static UserInfo getLoginUser(){
		return USER_INFO.get();
	}
}
