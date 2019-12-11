package com.leyou.item.servcie;

import com.leyou.common.vo.CartDTO;
import com.leyou.common.vo.PageRuslt;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;

import java.util.List;

/**
 * @Author zhangfeixiang
 * @Date 2019/10/22 15:15
 * @Version 1.0
 */
public interface IGoodsService  {
	/**
	 * 查询商品
	 * @param key
	 * @param saleable
	 * @param page
	 * @param rows
	 * @return
	 */
	PageRuslt<SpuBo> querySpuBoByPage(String key, Boolean saleable, Integer page, Integer rows);

	/**
	 * 保存商品
	 * @param spuBo
	 */
	void saveGoods(SpuBo spuBo);

	/**
	 * 查询商品详情
	 * @param spuId
	 * @return
	 */
	SpuDetail querySpuDetailById(Long spuId);

	/**
	 * 查询sku集合
	 * @param id
	 * @return
	 */
	List<Sku> querySkusById(Long id);

	/**
	 * 修改商品集及其信息
	 * @param spuBo
	 */
	void updateGoods(SpuBo spuBo);

	Spu querySpuById(Long spuId);

	Sku querySkuById(Long skuId);
	/**
	 * 根据skuIds 查询sku集合
	 * @param skuIds
	 * @return
	 */
	List<Sku> querySkusByIds(List<Long> skuIds);

	/**
	 * 减库存
	 * @param cartDTOS
	 */
	void desStock(List<CartDTO> cartDTOS);
}
