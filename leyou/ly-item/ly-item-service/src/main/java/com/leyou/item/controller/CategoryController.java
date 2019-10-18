package com.leyou.item.controller;

import com.leyou.item.pojo.Category;
import com.leyou.item.servcie.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author zhangfeixiang
 * @Date 2019/10/17 10:16
 * @Version 1.0
 */
@RestController
@RequestMapping("category")
public class CategoryController {
	@Autowired
	private ICategoryService categoryService;

	@GetMapping("list")
	public ResponseEntity<List<Category>> findCategroyByPid(@RequestParam("pid") Long pid){
		List<Category> categoryByPid = categoryService.findCategoryByPid(pid);
		return ResponseEntity.ok(categoryByPid);
	}
}
