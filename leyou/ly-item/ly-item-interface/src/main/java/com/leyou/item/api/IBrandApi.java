package com.leyou.item.api;

import com.leyou.item.pojo.Brand;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author zhangfeixiang
 * @Date 2019/11/7 14:12
 * @Version 1.0
 */
public interface IBrandApi {
	@GetMapping("brand/list")
	List<Brand> queryBrandsByIds(@RequestParam("ids")List<Long> cids);
	@GetMapping("brand/{id}")
	Brand queryBrandById(@PathVariable("id")Long id);
}
