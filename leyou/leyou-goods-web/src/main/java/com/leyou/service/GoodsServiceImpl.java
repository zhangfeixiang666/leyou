package com.leyou.service;

import com.leyou.client.IBrandClient;
import com.leyou.client.ICategoryClient;
import com.leyou.client.IGoodsClient;
import com.leyou.client.ISpecificationClient;
import com.leyou.item.pojo.*;
import com.leyou.utils.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.PrintWriter;
import java.util.*;

/**
 * @Author zhangfeixiang
 * @Date 2019/11/18 15:17
 * @Version 1.0
 */
@Service
@Slf4j
public class GoodsServiceImpl {

	@Autowired
	private IBrandClient brandClient;

	@Autowired
	private ICategoryClient categoryClient;

	@Autowired
	private IGoodsClient goodsClient;

	@Autowired
	private ISpecificationClient specificationClient;
	@Autowired
	private TemplateEngine templateEngine;

	public Map<String, Object> loadData(Long spuId){
		Map<String, Object> map = new HashMap<>();

		// 根据id查询spu对象
		Spu spu = this.goodsClient.querySpuById(spuId);

		// 查询spudetail
		SpuDetail spuDetail = this.goodsClient.querySpuDetailById(spuId);

		// 查询sku集合
		List<Sku> skus = this.goodsClient.querySkusById(spuId);

		// 查询分类
		List<Long> cids = Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3());
		List<Category> names = this.categoryClient.queryCategoriesByCids(cids);
		List<Map<String, Object>> categories = new ArrayList<>();
		for (int i = 0; i < cids.size(); i++) {
			Map<String, Object> categoryMap = new HashMap<>();
			categoryMap.put("id", cids.get(i));
			categoryMap.put("name", names.get(i));
			categories.add(categoryMap);
		}

		// 查询品牌
		Brand brand = this.brandClient.queryBrandById(spu.getBrandId());

		// 查询规格参数组
		List<SpecGroup> groups = this.specificationClient.queryParamsByCid(spu.getCid3());

		// 查询特殊的规格参数
		List<SpecParam> params = this.specificationClient.querySpecParamsByGid(null, spu.getCid3(), false, null);
		Map<Long, String> paramMap = new HashMap<>();
		params.forEach(param -> {
			paramMap.put(param.getId(), param.getName());
		});

		// 封装spu
		map.put("spu", spu);
		// 封装spuDetail
		map.put("spuDetail", spuDetail);
		// 封装sku集合
		map.put("skus", skus);
		// 分类
		map.put("categories", categories);
		// 品牌
		map.put("brand", brand);
		// 规格参数组
		map.put("groups", groups);
		// 查询特殊规格参数
		map.put("paramMap", paramMap);
		return map;
	}

	public void createHtml(Long spuId) {

		PrintWriter writer = null;
		try {
			// 创建thymeleaf上下文对象
			Context context = new Context();
			// 把数据放入上下文对象
			Map<String, Object> spuMap = loadData(spuId);
			context.setVariables(spuMap);

			// 创建输出流
			File file = new File("C:\\Temp\\html\\" + spuId + ".html");
			if (file.exists()){
				file.delete();
			}
			writer = new PrintWriter(file);
			// 执行页面静态化方法
			templateEngine.process("item", context, writer);
		} catch (Exception e) {
			log.error("页面静态化出错：{}，"+ e, spuId);
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}
	//新建线程处理页面静态化
	public void asynExecute(Long supId){
		final long id = supId;
		ThreadUtil.execute(()-> createHtml(id));
	}

	public void deleteHtml(Long id) {
		File file = new File("C:\\Temp\\html\\" + id + ".html");
		if (file.exists()){
			file.delete();
		}
	}
}

