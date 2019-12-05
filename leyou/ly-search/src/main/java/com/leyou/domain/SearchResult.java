package com.leyou.domain;

import com.leyou.common.vo.PageRuslt;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;

import java.util.List;
import java.util.Map;

/**
 * @Author zhangfeixiang
 * @Date 2019/11/7 12:55
 * @Version 1.0
 */
public class SearchResult<G> extends PageRuslt<Goods> {
	private List<Category> categories;
	private List<Brand> brands;
	private List<Map<String, Object>> specs;

	public List<Map<String, Object>> getSpecs() {
		return specs;
	}

	public void setSpecs(List<Map<String, Object>> specs) {
		this.specs = specs;
	}

	public SearchResult(Long totalPage, List items, Long total, List<Category> categories, List<Brand> brands, List<Map<String, Object>> specs) {
		super(totalPage, items, total);
		this.categories = categories;
		this.brands = brands;
		this.specs = specs;
	}

	public SearchResult() {
	}


	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public List<Brand> getBrands() {
		return brands;
	}

	public void setBrands(List<Brand> brands) {
		this.brands = brands;
	}
}
