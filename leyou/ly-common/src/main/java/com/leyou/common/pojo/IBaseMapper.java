package com.leyou.common.pojo;

import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Author zhangfeixiang
 * @Date 2019/10/23 10:16
 * @Version 1.0
 */
@RegisterMapper
public interface IBaseMapper<T> extends Mapper<T>, SelectByIdListMapper<T,Long>, InsertListMapper<T> {
}
