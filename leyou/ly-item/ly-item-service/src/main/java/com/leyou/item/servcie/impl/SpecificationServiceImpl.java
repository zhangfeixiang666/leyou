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

	@Override
	public void saveGroup(SpecGroup group) {
		group.setId(null);
		int count = this.specGroupMapper.insert(group);
		if (count != 1){
			throw new LyException(ExceptionEnum.GROUP_SAVE_FAILED);
		}
	}

	@Override
	public void updateGroup(SpecGroup group) {
		int i = this.specGroupMapper.updateByPrimaryKey(group);
		if (i != 1){
			throw new LyException(ExceptionEnum.GROUP_UPDATE_FAILED);
		}
	}

	@Override
	public void deleteGruop(Long id) {

		 //先删除分组下的规格参数
		SpecParam param = new SpecParam();
		param.setGroupId(id);
		int i = this.specParamMapper.delete(param);
		if(i < 0){
			throw new LyException(ExceptionEnum.DELETE_GROUP_FAILED);
		}
		//删除规格参数
		i = this.specGroupMapper.deleteByPrimaryKey(id);
		if (i != 1){
			throw new LyException(ExceptionEnum.DELETE_GROUP_FAILED);
		}
	}

	@Override
	public void saveParam(SpecParam param) {
		param.setId(null);
		int count = this.specParamMapper.insert(param);
		if (count != 1){
			throw new LyException(ExceptionEnum.PARAM_SAVE_FAILED);
		}
	}

	@Override
	public void updateParam(SpecParam param) {
		int count = this.specParamMapper.updateByPrimaryKeySelective(param);
		if (count != 1){
			throw new LyException(ExceptionEnum.PARAM_UPDATE_FAILED);
		}
	}
	@Override
	public void deleteParam(Long id) {
		int count = this.specParamMapper.deleteByPrimaryKey(id);
		if (count != 1){
			throw new LyException(ExceptionEnum.DELETE_PARAM_FAILED);
		}
	}
}
