package com.leyou.item.bo;

import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import lombok.Data;

import java.util.List;

/**
 * @Author zhangfeixiang
 * @Date 2019/10/22 15:08
 * @Version 1.0
 */
@Data
public class SpuBo extends Spu {
	private String bname;
	private String cname;
	private List<Sku> skus;
	private SpuDetail spuDetail;
}
