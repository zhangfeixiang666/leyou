package com.leyou.cart.service;

import com.leyou.auth.entry.UserInfo;
import com.leyou.cart.interceptor.LoginInterceptor;
import com.leyou.cart.pojo.Cart;
import com.leyou.common.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author zhangfeixiang
 * @Date 2019/11/29 11:08
 * @Version 1.0
 */
@Service
public class CartService {

	@Autowired
	private StringRedisTemplate redisTemplate;
	private final static String PREFIX = "leyou:cart:uid";

	public void addCart(Cart cart) {
		UserInfo userInfo = LoginInterceptor.getLoginUser();
		String key = PREFIX + userInfo.getId();
		Long skuId = cart.getSkuId();
		Integer num = cart.getNum();
		BoundHashOperations<String, Object, Object> ops = redisTemplate.boundHashOps(key);
		//3.判断是否存在key
		if (ops.hasKey(skuId.toString())) {
			cart = JsonUtils.parse(ops.get(skuId.toString()).toString(), Cart.class);
			cart.setNum(cart.getNum() + num);
		}
		ops.put(skuId.toString(), JsonUtils.serialize(cart));

	}

	public List<Cart> queryCart() {
		UserInfo userInfo = LoginInterceptor.getLoginUser();
		String key = PREFIX + userInfo.getId();
		//3.判断购物车是否为空
		if (!redisTemplate.hasKey(key)) {
			return null;
		}
		BoundHashOperations<String, Object, Object> ops = redisTemplate.boundHashOps(key);

		List<Object> values = ops.values();

		if (CollectionUtils.isEmpty(values)) {
			return null;
		}
		//4.获取购物车
		List<Cart> carts = values.stream().map(v -> JsonUtils.parse(v.toString(), Cart.class)).collect(Collectors.toList());

		/*List<Cart> carts = new ArrayList<>();
		values.forEach(value ->{
			Cart parse = JsonUtils.parse(value.toString(), Cart.class);
			carts.add(parse);
		});*/
		return carts;
	}

	public void updateCart(Cart cart) {
		UserInfo userInfo = LoginInterceptor.getLoginUser();
		String key = PREFIX + userInfo.getId();
		BoundHashOperations<String, Object, Object> ops = redisTemplate.boundHashOps(key);
		Long skuId = cart.getSkuId();
		//3.修改cart
		ops.put(skuId.toString(), JsonUtils.serialize(cart));
	}

	public void deleteCart(Long skuId) {
		UserInfo userInfo = LoginInterceptor.getLoginUser();
		String key = PREFIX + userInfo.getId();
		BoundHashOperations<String, Object, Object> ops = redisTemplate.boundHashOps(key);
		ops.delete(skuId.toString());

	}

	public void addLcoalCart(List<Cart> carts) {
		UserInfo userInfo = LoginInterceptor.getLoginUser();
		String key = PREFIX + userInfo.getId();
		BoundHashOperations<String, Object, Object> ops = redisTemplate.boundHashOps(key);
		//1.判断是否有购物车
		if (!redisTemplate.hasKey(key)) {
			carts.forEach(cart -> ops.put(cart.getSkuId().toString(), JsonUtils.serialize(cart)));
		} else {
			carts.forEach(cart -> {
				//判断登录购物车中是否有该物品
				if (ops.hasKey(cart.getSkuId().toString())) {
					Cart loginCart = JsonUtils.parse(ops.get(cart.getSkuId().toString()).toString(), Cart.class);
					loginCart.setNum(loginCart.getNum() + cart.getNum());
					ops.put(loginCart.getSkuId().toString(), JsonUtils.serialize(loginCart));
				} else {
					ops.put(cart.getSkuId().toString(), JsonUtils.serialize(cart));
				}
			});
		}
	}
}
