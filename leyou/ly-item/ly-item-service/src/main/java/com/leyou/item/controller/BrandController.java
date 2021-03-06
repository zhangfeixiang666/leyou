package com.leyou.item.controller;

import com.leyou.common.vo.PageRuslt;
import com.leyou.item.pojo.Brand;
import com.leyou.item.servcie.IBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author zhangfeixiang
 * @Date 2019/10/18 10:16
 * @Version 1.0
 */
@RestController
@RequestMapping("brand")
public class BrandController {
	@Autowired
	private IBrandService brandService;

	/**
	 * 分页查询商品
	 * @param key 关键字
	 * @param page 当前页
	 * @param rows 条数
	 * @param sortBy 排序字段
	 * @param desc  升序还是降序
	 * @return
	 */
	@GetMapping("page")
	public ResponseEntity<PageRuslt<Brand>> queryBrandPage(
			@RequestParam(value = "key", required = false)String key,
			@RequestParam(value = "page", defaultValue = "1")Integer page,
			@RequestParam(value = "rows", defaultValue = "5")Integer rows,
			@RequestParam(value = "sortBy", required = false)String sortBy,
			@RequestParam(value = "desc", required = false, defaultValue = "false")Boolean desc
	){
		return ResponseEntity.ok(brandService.queryBrandPage(key, page, rows, sortBy, desc));
	}

	/**
	 * 新增品牌
	 * @param brand
	 * @return
	 */
	@PostMapping("brand")
	public ResponseEntity<Void> addBrand(Brand brand, @RequestParam("cids")List<Long> cids){
		brandService.addBrand(brand,cids);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	/**
	 * 修改品牌
	 * @param brand
	 * @param cids
	 * @return
	 */
	@PutMapping("brand")
	public ResponseEntity<Void> updateBrand(Brand brand, @RequestParam("cids")List<Long> cids){
		brandService.updateBrand(brand,cids);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	/**
	 * 根据cid查询品牌名称
	 * @param cid
	 * @return
	 */
	@GetMapping("cid/{id}")
	public ResponseEntity<List<Brand>>queryBrandNameByCid(@PathVariable("id")Long cid){
		return ResponseEntity.ok(brandService.queryBrandNamesBycid(cid));
	}
	@GetMapping("list")
	public ResponseEntity<List<Brand>>queryBrandsByIds(@RequestParam("ids") List<Long> ids){
		return ResponseEntity.ok(brandService.queryBrandsByIds(ids));
	}
	@GetMapping("{id}")
	public ResponseEntity<Brand>queryBrandById(@PathVariable("id")Long id){
		return ResponseEntity.ok(brandService.queryBrandById(id));
	}

	/**
	 * 根据id删除品牌
	 * @param id
	 * @return
	 */
	@GetMapping("delete/{id}")
	public ResponseEntity<Void> deleteBrandById(@PathVariable("id")Long id){
		brandService.deleteBrandById(id);
		return ResponseEntity.noContent().build();
	}

}
