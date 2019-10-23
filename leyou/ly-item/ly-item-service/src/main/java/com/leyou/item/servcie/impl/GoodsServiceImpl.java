package com.leyou.item.servcie.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.Exception.LyException;
import com.leyou.common.ExceptionEnum;
import com.leyou.common.vo.PageRuslt;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.mapper.*;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.pojo.Stock;
import com.leyou.item.servcie.ICategoryService;
import com.leyou.item.servcie.IGoodsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Author zhangfeixiang
 * @Date 2019/10/22 15:15
 * @Version 1.0
 */
@Service
public class GoodsServiceImpl implements IGoodsService {
	@Autowired
	private ISpuMapper spuMapper;
	@Autowired
	private ICategoryService categoryService;
	@Autowired
	private IBrandMapper brandMapper;
	@Autowired
	private ISpuDetailMapper spuDetailMapper;
	@Autowired
	private ISkuMapper skuMapper;
	@Autowired
	private IStockMapper stockMapper;
	@Override
	public PageRuslt<SpuBo> querySpuBoByPage(String key, Boolean saleable, Integer page, Integer rows) {
		//1分页
		PageHelper.startPage(page, rows);
		//2过滤
		Example example = new Example(Spu.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(key)){
			criteria.andLike("title", "%" + key + "%");
		}
		if (saleable != null){
			criteria.andEqualTo("saleable", saleable);
		}
		//3.查询
		List<Spu> spus = spuMapper.selectByExample(example);
		if (CollectionUtils.isEmpty(spus)){
			throw new LyException(ExceptionEnum.SPU_NOT_FOUND);
		}
		ArrayList<SpuBo> spuBos = new ArrayList<>();
		for (Spu spu : spus) {
			SpuBo spuBo = new SpuBo();
			BeanUtils.copyProperties(spu, spuBo);
			List<String> names = categoryService.findNameByCids(
					Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
			spuBo.setCname(StringUtils.join(names,"/"));
			spuBo.setBname(brandMapper.selectByPrimaryKey(spu.getBrandId()).getName());
			spuBos.add(spuBo);
		}
		//4.解析
		PageInfo<Spu> info = new PageInfo<Spu>(spus);
		return new PageRuslt<SpuBo>(info.getTotal(), spuBos);
	}

	@Override
	@Transactional
	public void saveGoods(SpuBo spuBo) {
		//1.保存spu
		Spu spu = new Spu();
		BeanUtils.copyProperties(spuBo, spu);
		System.out.println("spuBo = " + spuBo);
		System.out.println("spu = " + spu);
		spu.setSaleable(true);
		spu.setValid(true);
		spu.setCreateTime(new Date());
		spu.setLastUpdateTime(new Date());
		int count = spuMapper.insert(spu);
		if (count != 1){
			throw new LyException(ExceptionEnum.GOODS_SIVE_FAILED);
		}
		//2.保存spuDatials
		Long spuId = spu.getId();
		SpuDetail spuDetail = spuBo.getSpuDetail();
		spuDetail.setSpuId(spuId);
		count = spuDetailMapper.insert(spuDetail);
		if (count != 1){
			throw new LyException(ExceptionEnum.GOODS_SIVE_FAILED);
		}
		//3.保存sku
		List<Sku> skus = spuBo.getSkus();
		List<Stock> stocks = new ArrayList<Stock>();
		if(!CollectionUtils.isEmpty(skus)){
			skus.forEach(sku->{
				sku.setSpuId(spuId);
				sku.setCreateTime(new Date());
				sku.setLastUpdateTime(new Date());
				skuMapper.insert(sku);
				//创建stock接受id和stock
				Stock stock = new Stock();
				stock.setSkuId(sku.getId());
				stock.setStock(sku.getStock());
				stocks.add(stock);
			});
		}
		//4.保存库存
		count = stockMapper.insertList(stocks);
		if (count < 1){
			throw new LyException(ExceptionEnum.GOODS_SIVE_FAILED);
		}

	}
}