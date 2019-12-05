package com.leyou.item.servcie.impl;

import com.leyou.common.Exception.LyException;
import com.leyou.common.ExceptionEnum;
import com.leyou.item.mapper.ISpecGroupMapper;
import com.leyou.item.mapper.ISpecParamMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.servcie.ISpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author zhangfeixiang
 * @Date 2019/10/22 10:57
 * @Version 1.0
 */
@Service
public class SpecificationServiceImpl implements ISpecificationService {
	@Autowired
	private ISpecGroupMapper specGroupMapper;
	@Autowired
	private ISpecParamMapper specParamMapper;
	@Override
	public List<SpecGroup> querySpecGroupsByCid(Long cid) {
		SpecGroup group = new SpecGroup();
		group.setCid(cid);
		List<SpecGroup> groupList = specGroupMapper.select(group);
		if (CollectionUtils.isEmpty(groupList)){
			throw new LyException(ExceptionEnum.GROUP_NOT_FOUND);
		}
		return groupList;
	}

	/**
	 * 根据组id
	 *
	 * @param gid
	 * @param cid
	 * @param generic
	 * @param searching
	 * @return
	 */
	@Override
	public List<SpecParam> querySpecParamsByGid(Long gid, Long cid, Boolean generic, Boolean searching) {
		SpecParam param = new SpecParam();
		param.setGroupId(gid);
		param.setCid(cid);
		param.setGeneric(generic);
		param.setSearching(searching);
		List<SpecParam> params = specParamMapper.select(param);
		if (CollectionUtils.isEmpty(params)){
			throw new LyException(ExceptionEnum.PARAM_NOT_FOUND);
		}
		return params;
	}

	/**
	 * 查询规格组合规格组参数
	 * @param cid
	 * @return
	 */
	@Override
	public List<SpecGroup> queryParamsByCid(Long cid) {
		List<SpecGroup> groups = this.querySpecGroupsByCid(cid);
		groups.forEach(g -> {
			// 查询组内参数
			g.setParams(this.querySpecParamsByGid(g.getId(), null, null, null));
		});
		return groups;
	}
}
