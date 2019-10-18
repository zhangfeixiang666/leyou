package com.leyou.item.servcie;

import com.leyou.common.vo.PageRuslt;
import com.leyou.item.pojo.Brand;

import java.util.List;

/**
 * @Author zhangfeixiang
 * @Date 2019/10/18 10:03
 * @Version 1.0
 */
public interface IBrandService {
	PageRuslt<Brand> queryBrandPage(String key, Integer page, Integer rows, String sortBy, Boolean desc);

	void addBrand(Brand brand, List<Long> cids);
}
