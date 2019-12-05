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
	Goods queryGoodsBySpuBo(SpuBo spuBo);

	/**
	 * 根据搜索条件查询分页
	 * @param searchRequest
	 * @return
	 */
	SearchResult<Goods> searchGoodsPage(SearchRequest searchRequest);

	void createIndex(Long id);

	void deleteIndex(Long id);
}
