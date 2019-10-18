package com.leyou.item.servcie;

import com.leyou.item.pojo.Category;

import java.util.List;

/**
 * @Author zhangfeixiang
 * @Date 2019/10/17 10:09
 * @Version 1.0
 */
public interface ICategoryService {
	/**
	 * 根据pid查找分类
	 * @param pid
	 * @return
	 */
	List<Category> findCategoryByPid(Long pid);
}