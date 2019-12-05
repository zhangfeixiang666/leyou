package com.leyou.item.servcie.impl;

import com.leyou.common.Exception.LyException;
import com.leyou.common.ExceptionEnum;
import com.leyou.item.mapper.ICategoryMapper;
import com.leyou.item.pojo.Category;
import com.leyou.item.servcie.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author zhangfeixiang
 * @Date 2019/10/17 10:11
 * @Version 1.0
 */
@Service
public class CategoryServiceImpl implements ICategoryService {
	@Autowired
	private ICategoryMapper categoryMapper;
	@Override
	public List<Category> findCategoryByPid(Long pid) {
		Category category = new Category();
		category.setParentId(pid);
		List<Category> categories = categoryMapper.select(category);
		if (CollectionUtils.isEmpty(categories)){
			throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
		}
		return categories;
	}

	@Override
	public List<String> findNameByCids(List<Long> cids) {
		ArrayList<String> names = new ArrayList<>();
		for (Long cid : cids) {
			String name = categoryMapper.selectByPrimaryKey(cid).getName();
			names.add(name);
		}
		return names;
	}

	@Override
	public List<Category> queryCategoriesByCids(List<Long> cids) {
		List<Category> categories = categoryMapper.selectByIdList(cids);
		if (CollectionUtils.isEmpty(categories)){
			throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
		}
		return categories;
	}

	@Override
	public List<Category> findCategroyById(Long cid3) {
		List<Category> categories = new ArrayList<>();
		try {
			Category category = this.categoryMapper.selectByPrimaryKey(cid3);
			Category category1 = this.categoryMapper.selectByPrimaryKey(category.getParentId());
			Category category2 = this.categoryMapper.selectByPrimaryKey(category1.getParentId());
			categories.add(category);
			categories.add(category1);
			categories.add(category2);
		} catch (Exception e) {
			throw new LyException(ExceptionEnum.CATEGORY_NOT_FOUND);
		}
		return categories;
	}
}
