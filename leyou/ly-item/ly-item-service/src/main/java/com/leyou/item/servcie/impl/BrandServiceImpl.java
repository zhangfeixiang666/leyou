package com.leyou.item.servcie.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.Exception.LyException;
import com.leyou.common.ExceptionEnum;
import com.leyou.common.vo.PageRuslt;
import com.leyou.item.mapper.IBrandMapper;
import com.leyou.item.pojo.Brand;
import com.leyou.item.servcie.IBrandService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author zhangfeixiang
 * @Date 2019/10/18 10:06
 * @Version 1.0
 */
@Service
@Slf4j
public class BrandServiceImpl implements IBrandService {
	@Autowired
	private IBrandMapper brandMapper;
	@Override
	public PageRuslt<Brand> queryBrandPage(String key, Integer page, Integer rows, String sortBy, Boolean desc) {
		//1.分页
		PageHelper.startPage(page, rows);
		//2.过滤
		//2.1创建模板，要分页实体类的class
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

	@Override
	public List<Brand> queryBrandsByIds(List<Long> ids) {
		List<Brand> brands = brandMapper.selectByIdList(ids);
		if (CollectionUtils.isEmpty(brands)){
			throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
		}
		return brands;
	}

	@Override
	public Brand queryBrandById(Long id) {
		Brand brand = this.brandMapper.selectByPrimaryKey(id);
		if (brand == null){
			throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
		}
		return brand;
	}

	@Override
	@Transactional
	public void updateBrand(Brand brand, List<Long> cids) {
		//更新品牌
		int i = brandMapper.updateByPrimaryKey(brand);
		if (i != 1){
			throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
		}
		//更新品牌分类表
		//1.1删除以前存在的
		i = brandMapper.deleteCategroyAndBrand(brand.getId());
		if (i != 1){
			throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
		}
		//1.2添加更新的
		cids.forEach(cid->{
			int count = brandMapper.insertCategoryAndBrand(cid, brand.getId());
			if (count != 1){
				throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
			}
		});
	}
	@Transactional
	@Override
	public void deleteBrandById(Long id) {
		//1.删除中间表的数据
		brandMapper.deleteCategroyAndBrand(id);
		//2.删除图片
		//2.1查询品牌
		Brand brand = brandMapper.selectByPrimaryKey(id);
		//2.2异步删除图片
		String image = brand.getImage();
		if (StringUtils.isNotBlank(image)){
			List<String> images = new ArrayList<>();
			images.add(image);
			new GoodsServiceImpl().sendFiles(images);
		}
		//3.删除品牌
		int count = brandMapper.deleteByPrimaryKey(id);
		if (count != 1){
			log.error("商品删除失败，商品id为：{}",id);
			throw new LyException(ExceptionEnum.DELETE_BRAND_FAILED);
		}

	}
}
