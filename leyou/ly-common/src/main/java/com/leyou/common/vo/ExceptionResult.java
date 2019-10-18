package com.leyou.common.vo;

import com.leyou.common.ExceptionEnum;
import lombok.Data;

/**
 * @Author zhangfeixiang
 * @Date 2019/10/15 20:27
 * @Version 1.0
 */

@Data
public class ExceptionResult {
	private String msg;
	private int code;
	private Long timestamp;
	public ExceptionResult(ExceptionEnum e){
		this.msg = e.getMsg();
		this.code = e.getCode();
		this.timestamp = System.currentTimeMillis();
	}
}
