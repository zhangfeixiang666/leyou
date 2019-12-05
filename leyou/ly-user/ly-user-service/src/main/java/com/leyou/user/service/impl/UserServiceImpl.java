package com.leyou.user.service.impl;

import com.leyou.common.Exception.LyException;
import com.leyou.common.ExceptionEnum;
import com.leyou.common.util.NumberUtils;
import com.leyou.user.domain.User;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.service.IUserService;
import com.leyou.user.util.CodecUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author zhangfeixiang
 * @Date 2019/11/25 16:58
 * @Version 1.0
 */
@Service
public class UserServiceImpl implements IUserService {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private AmqpTemplate amqpTemplate;
	@Autowired
	private StringRedisTemplate redisTemplate;
	public static final String  KEY_PREFIX = "sms.code.phone";
	@Override
	public Boolean checkUser(String data, Integer type) {
		User user = new User();
		switch (type){
			case 1:
				user.setUsername(data);
				break;
			case 2:
				user.setPhone(data);
				break;
			default:
				throw new LyException(ExceptionEnum.USER_VERIFY_FAILED);
		}
		return userMapper.selectCount(user) == 0;
	}

	@Override
	public void sendSms(String phone) {
		Map<String, String> map = new HashMap<>();
		map.put("phone", phone);
		String code = NumberUtils.generateCode(6);
		String key = KEY_PREFIX + phone;
		map.put("code", code);
		amqpTemplate.convertAndSend("ly.sms.exchange",
				"sms.verify.code", map);
		redisTemplate.opsForValue().set(key,"code",5, TimeUnit.MINUTES);
	}

	@Override
	public void register(User user, String code) {
		//1.校验验证码
		String recode = redisTemplate.opsForValue().get(KEY_PREFIX + user.getPhone());
		if (!StringUtils.equals(code, recode)){
			throw new LyException(ExceptionEnum.PRiCE_CANNOT_BE_NULL);
		}
		//2.加密密码
		String salt = CodecUtils.generateSalt();
		String newPwd = CodecUtils.md5Hex(user.getPassword(), salt);
		user.setSalt(salt);
		user.setPassword(newPwd);
		//3.用户创建时间
		user.setCreated(new Date());
		user.setId(null);
		//4.注册用户
		userMapper.insert(user);
	}

	@Override
	public User queryUser(String username, String password) {
		//1.根据用户名查询用户
		User user = new User();
		user.setUsername(username);
		User user1 = userMapper.selectOne(user);
		//判断用户是否存在
		if (user1 == null){
			throw new LyException(ExceptionEnum.PRiCE_CANNOT_BE_NULL);
		}
		//判断密码是否正确
		if (!StringUtils.equals(user1.getPassword(), CodecUtils.md5Hex(password, user1.getSalt()))){
			throw new LyException(ExceptionEnum.PRiCE_CANNOT_BE_NULL);
		}
		return user1;
	}
}
