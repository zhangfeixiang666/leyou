package com.leyou.item.controller;

import com.leyou.common.vo.PageRuslt;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.servcie.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author zhangfeixiang
 * @Date 2019/10/22 15:10
 * @Version 1.0
 */
@RestController
@RequestMapping
public class GoodsController {
	@Autowired
	private IGoodsService goodsService;
	@GetMapping("spu/page")
	public ResponseEntity<PageRuslt<SpuBo>>querySpuBoByPage(
			@RequestParam(value = "key", required = false)String key,
			@RequestParam(value = "saleable", defaultValue = "true", required = false)Boolean saleable,
			@RequestParam(value = "page", defaultValue = "1")Integer page,
			@RequestParam(value = "rows", defaultValue = "5")Integer rows

	){

		return ResponseEntity.ok(goodsService.querySpuBoByPage(key, saleable, page, rows));
	}
	@PostMapping("goods")
	public ResponseEntity<Void>saveGoods(@RequestBody SpuBo spuBo){
		goodsService.saveGoods(spuBo);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	@GetMapping("spu/detail/{spuId}")
	public ResponseEntity<SpuDetail>querySpuDetailById(@PathVariable("spuId")Long spuId){
		return ResponseEntity.ok(goodsService.querySpuDetailById(spuId));
	}
	@GetMapping("sku/list")
	public ResponseEntity<List<Sku>>querySkusById(@RequestParam("id")Long id){
		return ResponseEntity.ok(goodsService.querySkusById(id));
	}
	@PutMapping("goods")
	public ResponseEntity<Void>updateGoods(@RequestBody SpuBo spuBo){
		goodsService.updateGoods(spuBo);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
