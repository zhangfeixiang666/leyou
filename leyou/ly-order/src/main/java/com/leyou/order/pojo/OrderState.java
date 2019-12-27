package com.leyou.order.pojo;

/**
 * @Author zhangfeixiang
 * @Date 2019/12/18 22:28
 * @Version 1.0
 */
public enum OrderState {
	UN_PAY(1),
	SUCCESS(2),
	FAILD(3),
	;
	OrderState(int code) {
		this.code = code;
	}
	public int value() {
		return code;
	}
	private int code;

}
