package com.leyou.common.Exception;

import com.leyou.common.ExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Author zhangfeixiang
 * @Date 2019/10/15 20:26
 * @Version 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LyException extends RuntimeException {
	private ExceptionEnum exceptionEnum;
}
