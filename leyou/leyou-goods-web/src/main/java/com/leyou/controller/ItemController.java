package com.leyou.controller;

import com.leyou.service.GoodsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * @Author zhangfeixiang
 * @Date 2019/11/13 15:11
 * @Version 1.0
 */
@Controller
@RequestMapping("item")
@Slf4j
public class ItemController {
	@Autowired
	private GoodsServiceImpl goodsService;
	@GetMapping("{id}.html")
	public String toItemPage(@PathVariable("id")Long spuId, Model model){
		Map<String, Object> modelMap = goodsService.loadData(spuId);
		//返回封装数据
		model.addAllAttributes(modelMap);
		//生成静态页
		goodsService.asynExecute(spuId);
		log.info("走后台了：{}",spuId);
		return "item";
	}
}
