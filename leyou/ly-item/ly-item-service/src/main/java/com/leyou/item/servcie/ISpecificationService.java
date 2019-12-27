package com.leyou.item.servcie;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;

import java.util.List;

/**
 * @Author zhangfeixiang
 * @Date 2019/10/22 10:56
 * @Version 1.0
 */
public interface ISpecificationService {
	List<SpecGroup> querySpecGroupsByCid(Long cid);
	List<SpecParam> querySpecParamsByGid(Long gid, Long cid, Boolean generic, Boolean searching);

	/**
	 * 查询规格组参数以及组内参数
	 * @param cid
	 * @return
	 */
	List<SpecGroup> queryParamsByCid(Long cid);

	/**
	 * 新增分组
	 * @param group
	 */
	void saveGroup(SpecGroup group);

	/**
	 * 修改规格参数组
	 * @param group
	 */
	void updateGroup(SpecGroup group);

	/**
	 * 删除规格组
	 * @param id
	 */
	void deleteGroup(Long id);

	/**
	 * 新增规格参数
	 * @param param
	 */
	void saveParam(SpecParam param);

	/**
	 * 修改规格参数
	 * @param param
	 */
	void updateParam(SpecParam param);

	/**
	 * 删除规格参数
	 * @param id
	 */
	void deleteParam(Long id);
}
