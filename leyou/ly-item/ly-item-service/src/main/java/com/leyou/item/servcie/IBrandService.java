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
	/**
	 * 分页查询品牌
	 * @param key
	 * @param page
	 * @param rows
	 * @param sortBy
	 * @param desc
	 * @return
	 */
	PageRuslt<Brand> queryBrandPage(String key, Integer page, Integer rows, String sortBy, Boolean desc);

	void addBrand(Brand brand, List<Long> cids);

	List<Brand> queryBrandNamesBycid(Long cid);

	List<Brand> queryBrandsByIds(List<Long> cids);

	Brand queryBrandById(Long id);

	void updateBrand(Brand brand, List<Long> cids);

	/**
	 * 根据id删除品牌
	 * @param id
	 */
	void deleteBrandById(Long id);
}
