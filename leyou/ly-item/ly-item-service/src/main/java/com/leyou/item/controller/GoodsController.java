package com.leyou.item.controller;

import com.leyou.common.vo.CartDTO;
import com.leyou.common.vo.PageRuslt;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
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

	/**
	 * 分页查询goods
	 * @param key 搜索关键字
	 * @param saleable 上下架
	 * @param page 当前页
	 * @param rows 条数
	 * @return
	 */
	@GetMapping("spu/page")
	public ResponseEntity<PageRuslt<SpuBo>>querySpuBoByPage(
			@RequestParam(value = "key", required = false)String key,
			@RequestParam(value = "saleable", defaultValue = "true", required = false)Boolean saleable,
			@RequestParam(value = "page", defaultValue = "1")Integer page,
			@RequestParam(value = "rows", defaultValue = "5")Integer rows

	){

		return ResponseEntity.ok(goodsService.querySpuBoByPage(key, saleable, page, rows));
	}

	/**
	 * 保存商品
	 * @param spuBo
	 * @return
	 */
	@PostMapping("goods")
	public ResponseEntity<Void>saveGoods(@RequestBody SpuBo spuBo){
		goodsService.saveGoods(spuBo);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	/**
	 * 查询spu详情
	 * @param spuId
	 * @return
	 */
	@GetMapping("spu/detail/{spuId}")
	public ResponseEntity<SpuDetail>querySpuDetailById(@PathVariable("spuId")Long spuId){
		return ResponseEntity.ok(goodsService.querySpuDetailById(spuId));
	}

	/**
	 * 查询skus
	 * @param id
	 * @return
	 */
	@GetMapping("sku/list")
	public ResponseEntity<List<Sku>>querySkusById(@RequestParam("id")Long id){
		return ResponseEntity.ok(goodsService.querySkusById(id));
	}

	/**
	 * 修改商品
	 * @param spuBo
	 * @return
	 */
	@PutMapping("goods")
	public ResponseEntity<Void>updateGoods(@RequestBody SpuBo spuBo){
		goodsService.updateGoods(spuBo);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	/**
	 * 商品上下架
	 * @param spu
	 * @return
	 */
	@PutMapping("spu/saleable")
	public ResponseEntity<Void> saleable(@RequestBody Spu spu){
		goodsService.saleable(spu);
		return ResponseEntity.noContent().build();
	}

	/**
	 * 删除商品
	 * @param spuId
	 * @return
	 */
	@DeleteMapping("spu/delete/{id}")
	public ResponseEntity<Void> deleteGoods(@PathVariable("id")Long spuId){
		goodsService.deleteGoods(spuId);
		return ResponseEntity.noContent().build();
	}
	/**
	 * 查询spu：搜素微服务
	 * @param spuId
	 * @return
	 */
	@GetMapping("spu/{id}")
	public ResponseEntity<Spu> querySpuById(@PathVariable("id") Long spuId){
		return ResponseEntity.ok(goodsService.querySpuById(spuId));
	}

	/**
	 * 根据skuId查询sku
	 * @param skuId
	 * @return
	 */
	@GetMapping("sku/{id}")
	public ResponseEntity<Sku> querySkuById(@PathVariable("id") Long skuId){
		return ResponseEntity.ok(goodsService.querySkuById(skuId));

	}

	/**
	 * 根据skuIds 查询sku集合
	 * @param skuIds
	 * @return
	 */
	@GetMapping("sku/list/ids")
	public ResponseEntity<List<Sku>> querySkusByIds(@RequestParam("skuIds")List<Long> skuIds){
		return ResponseEntity.ok(goodsService.querySkusByIds(skuIds));
	}

	/**
	 * 减库存
	 * @param cartDTOS
	 * @return
	 */
	@PostMapping("spu/destock")
	public ResponseEntity<Void> desStock(@RequestBody List<CartDTO> cartDTOS){
		goodsService.desStock(cartDTOS);
		return ResponseEntity.noContent().build();
	}
}
