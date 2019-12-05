package com.leyou.item.api;

import com.leyou.common.vo.PageRuslt;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author zhangfeixiang
 * @Date 2019/11/1 9:13
 * @Version 1.0
 */
public interface IGoodsApi {
	/**
	 * 查询spu
	 * @param key
	 * @param saleable
	 * @param page
	 * @param rows
	 * @return
	 */
	@GetMapping("spu/page")
	PageRuslt<SpuBo> querySpuBoByPage(
			@RequestParam(value = "key", required = false)String key,
			@RequestParam(value = "saleable", defaultValue = "true", required = false)Boolean saleable,
			@RequestParam(value = "page", defaultValue = "1")Integer page,
			@RequestParam(value = "rows", defaultValue = "5")Integer rows

	);

	/**
	 * 查询商品详情
	 * @param spuId
	 * @return
	 */
	@GetMapping("spu/detail/{spuId}")
	SpuDetail querySpuDetailById(@PathVariable("spuId")Long spuId);

	/**
	 * 查询skus
	 * @param spuId
	 * @return
	 */
	@GetMapping("sku/list")
	List<Sku>querySkusById(@RequestParam("id")Long spuId);

	/**
	 * 查询spu
	 * @param spuId
	 * @return
	 */
	@GetMapping("spu/{id}")
	Spu querySpuById(@PathVariable("id") Long spuId);

	/**
	 * 查询sku
	 * @param skuId
	 * @return
	 */
	@GetMapping("sku/{id}")
	Sku querySkuById(@PathVariable("id") Long skuId);

	/**
	 * 根据skuids查询sku集合
	 * @param skuIds
	 * @return
	 */
	@GetMapping("skus")
	List<Sku> querySkusByIds(@RequestParam("skuIds")List<Long> skuIds);
}
