package com.leyou.test;

import com.leyou.common.util.JsonUtils;
import com.leyou.item.pojo.Spu;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Map;

/**
 * @Author zhangfeixiang
 * @Date 2019/12/18 9:28
 * @Version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class JsonTest {
	@Test
	public void test1(){
		Spu spu = new Spu();
		spu.setCreateTime(new Date());
		spu.setSaleable(false);
		spu.setId(200L);
		String serialize = JsonUtils.serialize(spu);
		System.out.println(serialize);
		System.out.println("----------------------");
		Map<Object, Object> map = JsonUtils.parseMap(serialize, Object.class, Object.class);
		for (Map.Entry<Object, Object> key : map.entrySet()) {
			System.out.println(key.getKey()+"-----"+key.getValue());
		}
	}
}
