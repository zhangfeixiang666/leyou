package com.leyou.item.mapper;

import com.leyou.item.pojo.Brand;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Author zhangfeixiang
 * @Date 2019/10/18 10:03
 * @Version 1.0
 */
public interface IBrandMapper extends Mapper<Brand> {
	@Insert("INSERT INTO tb_category_brand (category_id, brand_id) VALUES (#{cid},#{bid})")
	int insertCategoryAndBrand(@Param("cid") Long cid, @Param("bid") Long bid);
}
