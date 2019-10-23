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
@ControllerAdvice
public class LyExceptionAdvice {

	@ExceptionHandler(LyException.class)
	public ResponseEntity<ExceptionResult> lyExceptionHandler(LyException e){
		return ResponseEntity.status
				(e.getExceptionEnum().getCode()).body(new ExceptionResult(e.getExceptionEnum()));
	}
}
