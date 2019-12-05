package com.leyou.repository;

import com.leyou.client.IGoodsClient;
import com.leyou.common.vo.PageRuslt;
import com.leyou.domain.Goods;
import com.leyou.item.bo.SpuBo;
import com.leyou.service.IGoodsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author zhangfeixiang
 * @Date 2019/11/1 17:58
 * @Version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class IGoodRepositoryTest {
	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;
	@Autowired
	private IGoodRepository goodRepository;
	@Autowired
	IGoodsClient goodsClient;
	@Autowired
	private IGoodsService goodsService;
	@Test
	public void createIndex(){
		elasticsearchTemplate.createIndex(Goods.class);
		elasticsearchTemplate.putMapping(Goods.class);
	}
	@Test
	public void delete(){
		elasticsearchTemplate.deleteIndex(Goods.class);
	}
	@Test
	public void saveGoods(){
		int size = 100;
		int page = 1;
		do{
			PageRuslt<SpuBo> spuBoPageRuslt = goodsClient.querySpuBoByPage(null, true, page, size);
			List<SpuBo> spuBos = spuBoPageRuslt.getItems();
			/*List<Goods> goods = spuBos.stream().map(spuBo -> {
				return goodsService.queryGoodsBySpuBo(spuBo);
			}).collect(Collectors.toList());
			goodRepository.saveAll(goods);*/
			List<Goods> goods = new ArrayList<>();
			spuBos.forEach(spuBo -> {
				Goods good = goodsService.queryGoodsBySpuBo(spuBo);
				goods.add(good);
			});
			goodRepository.saveAll(goods);
			size = spuBoPageRuslt.getItems().size();
			page++;
		}while(size == 100);
	}
}