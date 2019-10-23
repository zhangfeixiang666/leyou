package com.leyou.item.servcie;

import com.leyou.common.vo.PageRuslt;
import com.leyou.item.bo.SpuBo;

/**
 * @Author zhangfeixiang
 * @Date 2019/10/22 15:15
 * @Version 1.0
 */
public interface IGoodsService  {
	PageRuslt<SpuBo> querySpuBoByPage(String key, Boolean saleable, Integer page, Integer rows);

	/**
	 * 保存商品
	 * @param spuBo
	 */
	void saveGoods(SpuBo spuBo);
}
