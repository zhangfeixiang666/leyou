package com.leyou.item.test;

import com.leyou.item.pojo.Brand;
import com.leyou.item.servcie.IBrandService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @Author zhangfeixiang
 * @Date 2019/10/23 10:08
 * @Version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestSpring {
	@Autowired
	private IBrandService brandService;
	@Test
	public void test1(){
		long start = System.currentTimeMillis();
		List<Brand> brands = brandService.queryBrandNamesBycid(76L);
		long end = System.currentTimeMillis();
		System.out.println(end-start);

	}

}
