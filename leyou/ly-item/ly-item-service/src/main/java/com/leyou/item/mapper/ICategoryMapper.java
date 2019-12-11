package com.leyou.item.mapper;

import com.leyou.common.pojo.IBaseMapper;
import com.leyou.item.pojo.Category;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author zhangfeixiang
 * @Date 2019/10/17 10:08
 * @Version 1.0
 */
public interface ICategoryMapper extends IBaseMapper<Category> {
	@Select("select category_id from tb_category_brand where brand_id = #{id} ")
	List<Long> queryCategoriesbyBid(@Param("id")Long id);
}
