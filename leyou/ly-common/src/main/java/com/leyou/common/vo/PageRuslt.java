package com.leyou.common.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author zhangfeixiang
 * @Date 2019/10/18 10:08
 * @Version 1.0
 */
@Data
public class PageRuslt<T> {
	private Long totalPage;//总页数
	private List<T> items;//内容
	private Long total;//总条数
	public PageRuslt(){

	}
	public PageRuslt(Long total, List<T> items){
		this.items = items;
		this.total = total;
	}
	public PageRuslt(Long totalPage, List<T> items, Long total){
		this.totalPage = totalPage;
		this.items = items;
		this.total = total;
	}
}
