package com.leyou.listener;

import com.leyou.service.GoodsServiceImpl;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author zhangfeixiang
 * @Date 2019/11/22 16:57
 * @Version 1.0
 */
@Component
public class GoodsListener {

	@Autowired
	private GoodsServiceImpl goodsService;
	/**
	 * 处理增改
	 *
	 * @param id
	 */
	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(value = "leyou.create.index.queue", durable = "true"),
			exchange = @Exchange(
					value = "leyou.item.exchange",
					ignoreDeclarationExceptions = "true",
					type = ExchangeTypes.TOPIC
			),
			key = {"item.insert", "item.update"}
	))
	public void listenCreate(Long id) {
		if (id == null) {
			return;
		} else {
			goodsService.createHtml(id);
		}
	}

	/**
	 * 处理删除
	 *
	 * @param id
	 */
	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(value = "leyou.delete.index.queue", durable = "true"),
			exchange = @Exchange(
					value = "leyou.item.exchange",
					ignoreDeclarationExceptions = "true",
					type = ExchangeTypes.TOPIC
			),
			key = {"item.delete"}
	))
	public void listenDelete(Long id) {
		if (id == null) {
			return;
		} else {
			goodsService.deleteHtml(id);
		}
	}
}
