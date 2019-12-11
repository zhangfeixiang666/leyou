package com.leyou.order.pojo;

/**
 * @Author zhangfeixiang
 * @Date 2019/12/6 14:17
 * @Version 1.0
 */
public enum PayState {
	/**
	 * 初始阶段：1、未付款、未发货；初始化所有数据
	 * 付款阶段：2、已付款、未发货；更改付款时间
	 * 发货阶段：3、已发货，未确认；更改发货时间、物流名称、物流单号
	 * 成功阶段：4、已确认，未评价；更改交易结束时间
	 * 关闭阶段：5、关闭； 更改更新时间，交易关闭时间。
	 * 评价阶段：6、已评价
	 */
	UN_PAY(1,"未付款"),
	PAY(2,"已付款,未发货"),
	SEND(3,"已发货，为确认"),
	SUCCESS(4,"已确认，未评价"),
	CLOSE(5,"关闭"),
	PERMIT(6,"已评价"),
	;

	PayState(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	public int value() {
		return code;
	}
	private int code;
	private String msg;
}
