package com.leyou.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.leyou.client.IBrandClient;
import com.leyou.client.ICategoryClient;
import com.leyou.client.IGoodsClient;
import com.leyou.client.ISpecificationClient;
import com.leyou.common.Exception.LyException;
import com.leyou.common.ExceptionEnum;
import com.leyou.common.util.JsonUtils;
import com.leyou.domain.Goods;
import com.leyou.domain.SearchRequest;
import com.leyou.domain.SearchResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.*;
import com.leyou.repository.IGoodRepository;
import com.leyou.service.IGoodsService;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author zhangfeixiang
 * @Date 2019/11/1 10:26
 * @Version 1.0
 */
@Service
public class GoodsServiceImpl implements IGoodsService {
	@Autowired
	private ElasticsearchTemplate template;

	@Autowired
	private IGoodsClient goodsClient;
	@Autowired
	ISpecificationClient specificationClient;
	@Autowired
	private IGoodRepository goodRepository;
	@Autowired
	private ICategoryClient categoryClient;
	@Autowired
	private IBrandClient brandClient;

	@Override
	public Goods queryGoodsBySpuBo(SpuBo spuBo) {
		Long spuId = spuBo.getId();
		//1.拼接all字段
		StringBuilder all = new StringBuilder(spuBo.getTitle());
		all.append(" ").append(spuBo.getBname()).append(" ")
				.append(StringUtils.replaceChars(spuBo.getCname(),"/", " "));
		//2.处理价格处理skus
		Set<Long> priceSet = new TreeSet<>();
		List<Map<String, Object>> skus = new ArrayList<>();
		List<Sku> skuList = goodsClient.querySkusById(spuId);
		skuList.forEach(sku -> {
			//处理sku
			Map<String, Object> skuu = new HashMap<>();
			skuu.put("id",sku.getId());
			skuu.put("title",sku.getTitle());
			skuu.put("images",StringUtils.substringBefore(sku.getImages(), ","));
			skuu.put("createTime",sku.getCreateTime());
			skuu.put("price", sku.getPrice());
			skus.add(skuu);
			//处理价格
			priceSet.add(sku.getPrice());
		});
		//3.处理规格参数
		Map<String, Object> specs = new HashMap<>();
		//3.1查询规格参数名称
		List<SpecParam> params = specificationClient.querySpecParamsByGid(null, spuBo.getCid3(), null, null);
		//3.2查询商品详情
		SpuDetail spuDetail = goodsClient.querySpuDetailById(spuId);
		//3.3通用规格参数
		Map<Long, String> genericSpec = JsonUtils.parseMap(spuDetail.getGenericSpec(), Long.class, String.class);
		//3.4特有规格参数
		Map<Long, List<String>> specialSpec = JsonUtils.nativeRead(spuDetail.getSpecialSpec(), new TypeReference<Map<Long, List<String>>>() {});
		params.forEach(param -> {
			//3.5判断参数是否可搜索
			if(param.getSearching()){
				String name = param.getName();
				//3.6判断是否是通用规格参数
				if(param.getGeneric()){
					//3.7判断是否是数值

					if(param.getNumeric()){
						specs.put(name, chooseSegment(genericSpec.get(param.getId()),param));
					}else{
						specs.put(name, genericSpec.get(param.getId()));
					}
				}else{
					specs.put(name,specialSpec.get(param.getId()));
				}
			}

		});
		Goods goods = new Goods();
		goods.setId(spuId);
		goods.setAll(all.toString());
		goods.setSubTitle(spuBo.getSubTitle());
		goods.setBrandId(spuBo.getBrandId());
		goods.setCid1(spuBo.getCid1());
		goods.setCid2(spuBo.getCid2());
		goods.setCid3(spuBo.getCid3());
		goods.setCreateTime(spuBo.getCreateTime());
		goods.setPrice(priceSet);
		goods.setSkus(JsonUtils.serialize(skus));
		goods.setSpecs(specs);

		return goods;
	}

	@Override
	public SearchResult<Goods> searchGoodsPage(SearchRequest searchRequest) {
		//判断查询条件，不允许查询全部商品
		String key = searchRequest.getKey();
		if(!StringUtils.isNotBlank(key)){
			return null;
		}
		//构建搜索过滤
		//QueryBuilder builder = QueryBuilders.matchQuery("all", key).operator(Operator.AND);
		QueryBuilder builder = buildBasicQuery(searchRequest);
		NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
		queryBuilder.withQuery(builder);
		queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id","skus","subTitle"}, null));
		String sortBy = searchRequest.getSortBy();

		Boolean desc = searchRequest.getDescending();
		if (desc == null){
			desc = false;
		}
		if (StringUtils.isNotBlank(sortBy)){
			queryBuilder.withSort(SortBuilders.fieldSort(sortBy).order(desc ? SortOrder.DESC : SortOrder.ASC));
		}
		//3分页
		int page = searchRequest.getPage();
		int size = searchRequest.getSize();
		queryBuilder.withPageable(PageRequest.of(page-1, size));

		//4聚合
		String categoryAgg = "categoryAgg";
		String brandAgg = "brandAgg";
		queryBuilder.addAggregation(AggregationBuilders.terms(categoryAgg).field("cid3"));
		queryBuilder.addAggregation(AggregationBuilders.terms(brandAgg).field("brandId"));
		AggregatedPage<Goods> goods =(AggregatedPage<Goods>) this.goodRepository.search(queryBuilder.build());
		List<Category> categorylist = getCategoryList((LongTerms) goods.getAggregation(categoryAgg));
		List<Brand> brandList = getBrandList((LongTerms)goods.getAggregation(brandAgg));
		//5解析
		//解析聚合函数
		//6解析规格参数
		List<Map<String, Object>> specs = new ArrayList<>();
		if (categorylist.size() == 1 && categorylist != null){
			specs = getSpecs(categorylist.get(0).getId(),builder);
		}
		if (goods == null){
			throw new LyException(ExceptionEnum.GOODS_NOT_FIND);
		}
		long totalElements = goods.getTotalElements();
		Long totalPage = (totalElements + size -1)/size;
		return new SearchResult<Goods>(totalPage, goods.getContent(), Long.valueOf(totalElements),categorylist, brandList, specs);
	}

	@Override
	public void deleteIndex(Long id) {
		this.goodRepository.deleteById(id);
	}

	@Override
	public void createIndex(Long id) {
		Spu spu = goodsClient.querySpuById(id);
		SpuBo spuBo = new SpuBo();
		BeanUtils.copyProperties(spu, spuBo);
		List<String> names = categoryClient.findNamesByIds(
				Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
		spuBo.setCname(StringUtils.join(names,"/"));
		spuBo.setBname(brandClient.queryBrandById(spu.getBrandId()).getName());
		Goods goods = this.queryGoodsBySpuBo(spuBo);
		//保存到索引库
		goodRepository.save(goods);
	}

	private QueryBuilder buildBasicQuery(SearchRequest searchRequest) {
		BoolQueryBuilder query = QueryBuilders.boolQuery();
		query.must(QueryBuilders.matchQuery("all", searchRequest.getKey()).operator(Operator.AND));
		BoolQueryBuilder filterBuilder = QueryBuilders.boolQuery();
		Map<String, String> filter = searchRequest.getFilter();
		if (filter == null){
			return null;
		}
		for (Map.Entry<String, String> entry : filter.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			if (key != "cid3" && key != "brandId"){
				key = "specs." + key + ".keyword";
			}
			filterBuilder.must(QueryBuilders.termQuery(key, value));
		}
		query.filter(filterBuilder);
		return query;
	}

	private List<Map<String, Object>> getSpecs(Long id, QueryBuilder builder) {
		List<Map<String, Object>> specs = new ArrayList<>();
		List<SpecParam> params = specificationClient.querySpecParamsByGid(null, id, null, true);
		NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
		queryBuilder.withQuery(builder);
		//聚合
		params.forEach(param->{
			String name = param.getName();
			queryBuilder.addAggregation(AggregationBuilders.terms(name).field("specs."+name+".keyword"));
		});
		//解析
		AggregatedPage<Goods> goods = this.template.queryForPage(queryBuilder.build(), Goods.class);
		params.forEach(param->{
			//
			Map<String,Object> spec = new HashMap<>();
			StringTerms terms = (StringTerms)goods.getAggregation(param.getName());
			List<StringTerms.Bucket> buckets = terms.getBuckets();
			List<String> keys = new ArrayList<>();
			buckets.forEach(bucket -> {
				String keyAsString = bucket.getKeyAsString();
				keys.add(keyAsString);
			});
			spec.put("k", param.getName());
			spec.put("options",keys);
			specs.add(spec);
		});
		return specs;
	}

	private List<Brand> getBrandList(LongTerms terms) {
		List<LongTerms.Bucket> buckets = terms.getBuckets();
		List<Long> brandIds = new ArrayList<>();
		buckets.forEach(bucket -> {
			brandIds.add(bucket.getKeyAsNumber().longValue());
		});
		return brandClient.queryBrandsByIds(brandIds);
	}

	private List<Category> getCategoryList(LongTerms terms) {
		List<LongTerms.Bucket> buckets = terms.getBuckets();
		List<Long> cids = new ArrayList<>();
		buckets.forEach(bucket -> {
			long cid = bucket.getKeyAsNumber().longValue();
			cids.add(cid);
		});
		return categoryClient.queryCategoriesByCids(cids);
	}

	private String chooseSegment(String value, SpecParam p) {
		double val = NumberUtils.toDouble(value);
		String result = "其它";
		// 保存数值段
		for (String segment : p.getSegments().split(",")) {
			String[] segs = segment.split("-");
			// 获取数值范围
			double begin = NumberUtils.toDouble(segs[0]);
			double end = Double.MAX_VALUE;
			if(segs.length == 2){
				end = NumberUtils.toDouble(segs[1]);
			}
			// 判断是否在范围内
			if(val >= begin && val < end){
				if(segs.length == 1){
					result = segs[0] + p.getUnit() + "以上";
				}else if(begin == 0){
					result = segs[1] + p.getUnit() + "以下";
				}else{
					result = segment + p.getUnit();
				}
				break;
			}
		}
		return result;
	}
}
