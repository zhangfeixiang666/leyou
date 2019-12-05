package com.leyou.cart.controller;

import com.leyou.cart.pojo.Cart;
import com.leyou.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author zhangfeixiang
 * @Date 2019/11/29 10:54
 * @Version 1.0
 */
@RestController
@RequestMapping
public class CartController {
	@Autowired
	private CartService cartService;

	/**
	 * 加购物车
	 * @param cart
	 * @return
	 */
	@PostMapping
	public ResponseEntity<Void> addCart(@RequestBody Cart cart){
		cartService.addCart(cart);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	/**
	 * 查询购物车
	 * @return
	 */
	@GetMapping
	public ResponseEntity<List<Cart>>queryCart(){
		return ResponseEntity.ok(cartService.queryCart());
	}
	@PutMapping("/put")
	public ResponseEntity<Void> updateCart(@RequestBody Cart cart){
		cartService.updateCart(cart);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	@DeleteMapping("{id}")
	public ResponseEntity<Void> deleteCart(@PathVariable("id") Long skuId){
		cartService.deleteCart(skuId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	@PostMapping("sotre")
	public ResponseEntity<Void> addLcoalCart(@RequestBody List<Cart> carts){
		cartService.addLcoalCart(carts);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
