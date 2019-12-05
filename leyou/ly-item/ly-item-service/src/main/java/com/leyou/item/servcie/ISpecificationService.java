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

	List<SpecGroup> queryParamsByCid(Long cid);
}
