package com.leyou.oreder.mapper;

import com.leyou.oreder.pojo.Order;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OrderMapper extends Mapper<Order> {

    List<Order> queryOrderList(
			@Param("userId") Long userId,
			@Param("status") Integer status);
}
