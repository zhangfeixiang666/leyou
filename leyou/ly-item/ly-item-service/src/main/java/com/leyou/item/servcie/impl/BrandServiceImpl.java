package com.leyou.item.servcie.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.Exception.LyException;
import com.leyou.common.ExceptionEnum;
import com.leyou.common.vo.PageRuslt;
import com.leyou.item.mapper.IBrandMapper;
import com.leyou.item.pojo.Brand;
import com.leyou.item.servcie.IBrandService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author zhangfeixiang
 * @Date 2019/10/18 10:06
 * @Version 1.0
 */
@Service
public class BrandServiceImpl implements IBrandService {
	@Autowired
	private IBrandMapper brandMapper;
	@Override
	public PageRuslt<Brand> queryBrandPage(String key, Integer page, Integer rows, String sortBy, Boolean desc) {
		//1.分页
		PageHelper.startPage(page, rows);
		//2.过滤
		Example example = new Example(Brand.class);
		if (!StringUtils.isBlank(key)){
			example.createCriteria().andLike("name", "%"+key+"%")
					.orEqualTo("letter", key.toUpperCase());
		}
		//3.排序
		if (!StringUtils.isBlank(sortBy)){
			String orderByClasue =sortBy +(desc ? " desc":" asc");
			example.setOrderByClause(orderByClasue);
		}
		//4.返回结果
		List<Brand> list = brandMapper.selectByExample(example);
		if (CollectionUtils.isEmpty(list)){
			throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
		}
		//5.解析结果
		PageInfo<Brand> info = new PageInfo<>(list);
		//6.返回结果
		return new PageRuslt<Brand>(info.getTotal(), info.getList());
	}

	/**
	 * 新增品牌
	 * @param brand
	 * @param cids
	 */
	@Override
	@Transactional
	public void addBrand(Brand brand, List<Long> cids) {
		brand.setId(null);
		int insert = brandMapper.insert(brand);
		if (insert != 1){
			throw new LyException(ExceptionEnum.BRAND_SAVE_FAILED);
		}
		for (Long cid : cids) {
			int i = brandMapper.insertCategoryAndBrand(cid, brand.getId());
			if (i !=1){
				throw new LyException(ExceptionEnum.BRAND_SAVE_FAILED);
			}
		}
	}

	@Override
	public List<Brand> queryBrandNamesBycid(Long cid) {
		List<Brand> brands = this.brandMapper.queryBrandIdsByCid(cid);
//		List<Long> ids = this.brandMapper.queryBrandIdsByCid(cid);


		if (CollectionUtils.isEmpty(brands)){
			throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
		}
		/*if (CollectionUtils.isEmpty(ids)){
			throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
		}*/
		/*ArrayList<Brand> brands = new ArrayList<>();
		for (Long id : ids) {
			Brand brand = this.brandMapper.selectByPrimaryKey(id);
			brands.add(brand);
		}*/
		System.out.println("brands.size() = " + brands.size());
		return brands;
	}
}
