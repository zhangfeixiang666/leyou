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
public class SpecificationController {
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

	/**
	 * 查询规格组参数 以及组内参数
	 * @param cid
	 * @return
	 */
	@GetMapping("{cid}")
	public ResponseEntity<List<SpecGroup>> queryParamsByCid(@PathVariable("cid")Long cid ){
		return ResponseEntity.ok(specificationService.queryParamsByCid(cid));
	}

	/**
	 * 增加规格参数组
	 * @param group
	 * @return
	 */
	@PostMapping("group")
	public ResponseEntity<Void> saveGroup(@RequestBody SpecGroup group){
		this.specificationService.saveGroup(group);
		return ResponseEntity.noContent().build();
	}

	/**
	 * 修改规格参数组
	 * @param group
	 * @return
	 */
	@PutMapping("group")
	public ResponseEntity<Void> updateGroup(@RequestBody SpecGroup group){
		this.specificationService.updateGroup(group);
		return ResponseEntity.noContent().build();
	}

	/**
	 * 删除规格组
	 * @param id
	 * @return
	 */
	@DeleteMapping("group/{id}")
	public ResponseEntity<Void> deleteGroup(@PathVariable("id") Long id){
		this.specificationService.deleteGroup(id);
		return ResponseEntity.noContent().build();
	}

	/**
	 * 新增参数
	 * @param param
	 * @return
	 */
	@PostMapping("param")
	public ResponseEntity<Void> saveParam(@RequestBody SpecParam param){
		this.specificationService.saveParam(param);
		return ResponseEntity.noContent().build();
	}

	/**
	 *
	 * @param param
	 * @return
	 */
	@PutMapping("param")
	public ResponseEntity<Void> updateParam(@RequestBody SpecParam param){
		this.specificationService.updateParam(param);
		return ResponseEntity.noContent().build();
	}

	/**
	 * 删除规格参数
	 * @param id
	 * @return
	 */
	@DeleteMapping("param/{id}")
	public ResponseEntity<Void> deleteParam(@PathVariable("id") Long id){
		this.specificationService.deleteParam(id);
		return ResponseEntity.noContent().build();
	}
}
