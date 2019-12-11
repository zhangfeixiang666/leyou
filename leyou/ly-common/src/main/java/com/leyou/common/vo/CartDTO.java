package com.leyou.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author zhangfeixiang
 * @Date 2019/12/5 19:18
 * @Version 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CartDTO {
	private Long skuId;
	private Integer num;
}
