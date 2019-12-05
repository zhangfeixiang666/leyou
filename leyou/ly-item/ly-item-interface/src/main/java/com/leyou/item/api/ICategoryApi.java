package com.leyou.item.api;

import com.leyou.item.pojo.Category;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author zhangfeixiang
 * @Date 2019/11/7 13:15
 * @Version 1.0
 */
public interface ICategoryApi {
	@GetMapping("category/ids")
	List<Category> queryCategoriesByCids(@RequestParam("ids")List<Long> ids);
	@GetMapping("category/names")
	List<String> findNamesByIds(@RequestParam("ids") List<Long> ids);

}
