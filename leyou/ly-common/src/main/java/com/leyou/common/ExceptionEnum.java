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
	USER_VERIFY_FAILED(400, "验证失败"),
	DELETE_GROUP_FAILED(400, "删除规格组失败"),
	DELETE_PARAM_FAILED(400, "删除规格参数失败"),
	CATEGORY_NOT_FOUND(404,"商品分类未找到"),
	PARAM_NOT_FOUND(404,"商品参数未找到"),
	BRAND_NOT_FOUND(404,"品牌未找到"),
	GROUP_SAVE_FAILED(500,"新增规格组失败"),
	PARAM_SAVE_FAILED(500,"新增规格参数失败"),
	GROUP_UPDATE_FAILED(500,"更新规格组失败"),
	PARAM_UPDATE_FAILED(500,"更新规格参数失败"),
	SPU_NOT_FOUND(404,"商品集未找到"),
	GOODS_NOT_FIND(404,"商品未找到"),
	SKU_NOT_FOUND(404,"商品未找到"),
	SPUDETAIL_NOT_FOUND(404,"商品详情未找到"),
	GROUP_NOT_FOUND(404,"商品组未找到"),
	BRAND_SAVE_FAILED(404,"新增品牌失败"),
	INVALID_FILE_TYPE(404,"文件类型不正确"),
	INVALID_USER_FAILED(404,"用户名或密码错误"),
	INVALID_VARIFY_CODE(404,"无效的验证码"),
	GOODS_SIVE_FAILED(500,"商品保存失败"),
	GOODS_UPDATE_FAILED(500,"商品保存失败"),
	SKU_NOT_NULL(500,"商品为空"),
	ORDER_CREATE_FAILED(500,"商品为空"),
	DELETE_BRAND_FAILED(500,"商品为空"),
	WX_PAY_ORDER_FAILED(500,"微信下单失败"),

	;
	private int code;
	private String msg;
}
