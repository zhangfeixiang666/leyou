package com.leyou.client;

import com.leyou.common.vo.PageRuslt;
import com.leyou.item.bo.SpuBo;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @Author zhangfeixiang
 * @Date 2019/11/1 9:46
 * @Version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest

public class IGoodsClientTest {
	@Autowired
	private IGoodsClient goodsClient;
	@Test
	public void test(){
		PageRuslt<SpuBo> spuBoPageRuslt = goodsClient.querySpuBoByPage(null, true, 1, 5);
		List<SpuBo> items = spuBoPageRuslt.getItems();
		//skus为null
		//spuDetial为null
		items.forEach(spuBo->{
			StringBuilder all = new StringBuilder(spuBo.getTitle());
			all.append(" ").append(spuBo.getBname())
					.append(StringUtils.replaceChars(spuBo.getCname(),"/", " "));
			System.out.println(all.toString());
		});
	}
}