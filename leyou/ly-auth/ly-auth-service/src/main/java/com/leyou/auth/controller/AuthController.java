package com.leyou.auth.controller;

import com.leyou.auth.client.IUserClient;
import com.leyou.auth.config.AuthConfig;
import com.leyou.auth.entry.UserInfo;
import com.leyou.auth.util.JwtUtils;
import com.leyou.common.Exception.LyException;
import com.leyou.common.ExceptionEnum;
import com.leyou.common.util.CookieUtils;
import com.leyou.user.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author zhangfeixiang
 * @Date 2019/11/27 9:51
 * @Version 1.0
 */
@RestController
@RequestMapping
@Slf4j
@EnableConfigurationProperties(AuthConfig.class)
public class AuthController {
	@Autowired
	private IUserClient userClient;
	@Autowired
	private AuthConfig authConfig;
	@PostMapping("/accredit")
	public ResponseEntity<Void> accredit(
			@RequestParam("username")String username, @RequestParam("password")String password,
			HttpServletResponse response, HttpServletRequest request
	){
		//1.查看用户是否存在
		User user = userClient.queryUser(username, password);
		if (user == null){
			throw new LyException(ExceptionEnum.USER_VERIFY_FAILED);
		}
		//2.封装userInfo
		UserInfo userInfo = new UserInfo(user.getId(), username);
		//3.生成token
		String token= null;
		try {
			token = JwtUtils.generateToken(userInfo, authConfig.getPriKey(), 30);
			log.info("token为:{}",token);
		} catch (Exception e) {
			log.error("token身份信息错误:{}", token);
			e.printStackTrace();
		}
		//4.写入token
		CookieUtils.setCookie(request, response, authConfig.getCookieName(), token, null, null, true);
		return ResponseEntity.status(200).build();
	}
	@GetMapping("verify")
	public ResponseEntity<UserInfo> verify(HttpServletRequest request, HttpServletResponse response){
		Cookie[] cookies = request.getCookies();
		String token = CookieUtils.getCookieValue(request, authConfig.getCookieName(), false);
		UserInfo userInfo = null;
		if (StringUtils.isNotBlank(token)){
			try {
				userInfo = JwtUtils.getInfoFromToken(token, authConfig.getPubKey());
				//刷新token
				token = JwtUtils.generateToken(userInfo, authConfig.getPriKey(), authConfig.getExpire());
				CookieUtils.setCookie(request, response, authConfig.getCookieName(), token, null, null, true);
				return ResponseEntity.ok(userInfo);
			} catch (Exception e) {
				log.error("token被修改");
				e.printStackTrace();

			}
		}
		return ResponseEntity.status(403).body(null);



	}
}
