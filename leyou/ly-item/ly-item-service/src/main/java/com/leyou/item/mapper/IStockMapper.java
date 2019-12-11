package com.leyou.item.mapper;

import com.leyou.common.pojo.IBaseMapper;
import com.leyou.item.pojo.Stock;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @Author zhangfeixiang
 * @Date 2019/10/23 16:08
 * @Version 1.0
 */
public interface IStockMapper extends IBaseMapper<Stock> {
	@Update("update tb_stock set stock = stock - #{num} Where sku_id = # id AND stock >= #{num}")
	int desStock(@Param("id") Long id, @Param("num") Integer num);
}
