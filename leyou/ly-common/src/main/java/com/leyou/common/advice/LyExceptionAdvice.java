package com.leyou.common.advice;

import com.leyou.common.Exception.LyException;
import com.leyou.common.vo.ExceptionResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @Author zhangfeixiang
 * @Date 2019/10/15 20:20
 * @Version 1.0
 */
//@ExceptionHandler的作用主要在于声明一个或多个类型的异常，
//当符合条件的Controller抛出这些异常之后将会对这些异常进行捕获，
//然后按照其标注的方法的逻辑进行处理，从而改变返回的视图信息
@ControllerAdvice
//@ControllerAdvice
//结合方法型注解@ExceptionHandler，
//用于捕获Controller中抛出的指定类型的异常，
//从而达到不同类型的异常区别处理的目的
public class LyExceptionAdvice {

	@ExceptionHandler(LyException.class)
	public ResponseEntity<ExceptionResult> lyExceptionHandler(LyException e){
		return ResponseEntity.status
				(e.getExceptionEnum().getCode()).body(new ExceptionResult(e.getExceptionEnum()));
	}
}
