package com.leyou.service;

import com.leyou.domain.Goods;
import com.leyou.domain.SearchRequest;
import com.leyou.domain.SearchResult;
import com.leyou.item.bo.SpuBo;

/**
 * @Author zhangfeixiang
 * @Date 2019/11/1 10:24
 * @Version 1.0
 */
public interface IGoodsService {
	/**
	 * 初始化索引库
	 * @param spuBo
	 * @return
	 */
	Goods queryGoodsBySpuBo(SpuBo spuBo);

	/**
	 * 根据搜索条件查询分页
	 * @param searchRequest
	 * @return
	 */
	SearchResult<Goods> searchGoodsPage(SearchRequest searchRequest);

	/**
	 * 创建索引
	 * @param id
	 */
	void createIndex(Long id);

	/**
	 * 删除索引
	 * @param id
	 */
	void deleteIndex(Long id);
}
