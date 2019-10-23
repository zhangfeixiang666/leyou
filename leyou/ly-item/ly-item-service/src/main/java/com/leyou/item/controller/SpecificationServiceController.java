package com.leyou.item.controller;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.servcie.ISpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author zhangfeixiang
 * @Date 2019/10/22 10:57
 * @Version 1.0
 */
@RestController
@RequestMapping("spec")
public class SpecificationServiceController {
	@Autowired
	private ISpecificationService specificationService;

	/**
	 * 根据分类cid查询规格组
	 * @param cid
	 * @return
	 */
	@GetMapping("groups/{cid}")
	public ResponseEntity<List<SpecGroup>>querySpecGroupsByCids(@PathVariable("cid")Long cid){
		return ResponseEntity.ok(specificationService.querySpecGroupsByCid(cid));
	}

	/**
	 * 根据规格组gid查询规格参数
	 * @param gid
	 * @return
	 */
	@GetMapping("params")
	public ResponseEntity<List<SpecParam>>querySpecParamsByGid(
			@RequestParam(value = "gid",required = false)Long gid,
			@RequestParam(value = "cid",required = false)Long cid,
			@RequestParam(value = "generic",required = false)Boolean generic,
			@RequestParam(value = "searching",required = false)Boolean searching
			){
		return ResponseEntity.ok(specificationService.querySpecParamsByGid(gid, cid, generic, searching));
	}
}
