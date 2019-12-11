package com.leyou.item.controller;

import com.leyou.item.pojo.Category;
import com.leyou.item.servcie.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author zhangfeixiang
 * @Date 2019/10/17 10:16
 * @Version 1.0
 */
@RestController
public class CategoryController {
	@Autowired
	private ICategoryService categoryService;

	/**
	 * 根据pid查询分类
	 * @param pid
	 * @return
	 */
	@GetMapping("category/list")
	public ResponseEntity<List<Category>> findCategroyByPid(@RequestParam("pid") Long pid){
		List<Category> categoryByPid = categoryService.findCategoryByPid(pid);
		return ResponseEntity.ok(categoryByPid);
	}
	@GetMapping("category/ids")
	public ResponseEntity<List<Category>> queryCategoriesByCids(@RequestParam("ids") List<Long> ids){
		return ResponseEntity.ok(categoryService.queryCategoriesByCids(ids));
	}
	@GetMapping("category/all/level")
	public ResponseEntity<List<Category>> findCategroyById(@RequestParam("id") Long cid3){
		List<Category> categoryByPid = categoryService.findCategroyById(cid3);
		return ResponseEntity.ok(categoryByPid);
	}
	@GetMapping("category/names")
	public ResponseEntity<List<String>> findNamesByIds(@RequestParam("ids") List<Long> ids){
		List<String> names = categoryService.findNameByCids(ids);
		return ResponseEntity.ok(names);
	}
	@GetMapping("category/bid/{id}")
	public ResponseEntity<List<Category>> queryCategoriesByBid(@PathVariable("id")Long id){
		return ResponseEntity.ok(categoryService.queryCategoriesByBid(id));
	}
}
