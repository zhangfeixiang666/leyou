package com.leyou.item.api;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author zhangfeixiang
 * @Date 2019/11/1 10:28
 * @Version 1.0
 */
public interface ISpecificationApi {
	/**
	 * 根据分类id查询规格组
	 * @param cid
	 * @return
	 */
	@GetMapping("spec/groups/{cid}")
	List<SpecGroup>querySpecGroupsByCids(@PathVariable("cid")Long cid);
	@GetMapping("spec/params")
	List<SpecParam> querySpecParamsByGid(
			@RequestParam(value = "gid",required = false)Long gid,
			@RequestParam(value = "cid",required = false)Long cid,
			@RequestParam(value = "generic",required = false)Boolean generic,
			@RequestParam(value = "searching",required = false)Boolean searching
	);
	//查询规格组和规格参数
	@GetMapping("spec/{cid}")
	List<SpecGroup> queryParamsByCid(@PathVariable("cid")Long cid);
}
