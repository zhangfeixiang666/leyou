package com.leyou.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author zhangfeixiang
 * @Date 2019/12/19 17:37
 * @Version 1.0
 */
public class ThreadUtil {
	private static final ExecutorService es = Executors.newFixedThreadPool(10);
	public static void execute(Runnable runnable){
		es.submit(runnable);
	}
}
