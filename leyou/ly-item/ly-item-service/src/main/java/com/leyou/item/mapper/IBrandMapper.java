package com.leyou.item.mapper;

import com.leyou.common.pojo.IBaseMapper;
import com.leyou.item.pojo.Brand;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author zhangfeixiang
 * @Date 2019/10/18 10:03
 * @Version 1.0
 */
public interface IBrandMapper extends IBaseMapper<Brand> {
	@Insert("INSERT INTO tb_category_brand (category_id, brand_id) VALUES (#{cid},#{bid})")
	int insertCategoryAndBrand(@Param("cid") Long cid, @Param("bid") Long bid);


	/*@Select("select brand_id from tb_category_brand where category_id = #{cid}")
	List<Long> queryBrandIdsByCid(@Param("cid") Long cid);*/

	@Select("SELECT  b.* FROM `tb_brand` b left join tb_category_brand c on b.id = c.brand_id where c.category_id = #{cid}")
	List<Brand> queryBrandIdsByCid(@Param("cid") Long cid);
}
