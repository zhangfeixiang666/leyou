package com.leyou.user.controller;

import com.leyou.user.domain.User;
import com.leyou.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Author zhangfeixiang
 * @Date 2019/11/25 16:56
 * @Version 1.0
 */
@RestController
@RequestMapping
@EnableConfigurationProperties()
public class UserController {
	@Autowired
	private IUserService userService;

	/**
	 * 用户验证
	 * @param data
	 * @param type
	 * @return
	 */
	@GetMapping("/check/{data}/{type}")
	public ResponseEntity<Boolean> checkUser
			(@PathVariable("data")String data, @PathVariable("type")Integer type){
		return ResponseEntity.ok(userService.checkUser(data, type));
	}

	/**
	 * 发送验证码
	 * @param phone
	 * @return
	 */
	@PostMapping("code")
	public ResponseEntity<Void> sendSms(String phone){
		this.userService.sendSms(phone);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	/**
	 * 用户注册
	 * @param user
	 * @param code
	 * @return
	 */
	@PostMapping("register")
	public ResponseEntity<Void> register(@Valid User user, BindingResult result, @RequestParam("code") String code){
		userService.register(user, code);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	@GetMapping("query")
	public ResponseEntity<User> queryUser(@RequestParam("username")String username, @RequestParam("password")String password){
		return ResponseEntity.ok(userService.queryUser(username, password));
	}


}
