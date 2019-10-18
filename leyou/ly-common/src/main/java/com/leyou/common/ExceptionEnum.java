package com.leyou.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Author zhangfeixiang
 * @Date 2019/10/15 20:23
 * @Version 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ExceptionEnum {
	PRiCE_CANNOT_BE_NULL(400, "价格不能为空"),
	CATEGORY_NOT_FOUND(404,"商品分类未找到"),
	BRAND_NOT_FOUND(404,"品牌未找到"),
	BRAND_SAVE_FAILED(404,"新增品牌失败"),
	INVALID_FILE_TYPE(404,"文件类型不正确"),
	;
	private int code;
	private String msg;
}