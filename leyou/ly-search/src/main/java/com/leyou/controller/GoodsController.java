package com.leyou.controller;

import com.leyou.domain.Goods;
import com.leyou.domain.SearchRequest;
import com.leyou.domain.SearchResult;
import com.leyou.service.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author zhangfeixiang
 * @Date 2019/11/4 10:07
 * @Version 1.0
 */
@Controller
@RequestMapping
public class GoodsController {
	@Autowired
	private IGoodsService goodsService;

	/**
	 *分页查询数据
	 * @param searchRequest
	 * @return
	 */
	@PostMapping("page")
	public @ResponseBody ResponseEntity<SearchResult<Goods>> searchGoodsPage(@RequestBody SearchRequest searchRequest){
		System.out.println("searchRequest = " + searchRequest);
		return ResponseEntity.ok(goodsService.searchGoodsPage(searchRequest));
	}

}
