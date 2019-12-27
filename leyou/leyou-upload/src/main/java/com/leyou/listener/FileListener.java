package com.leyou.listener;

import com.github.tobato.fastdfs.service.FastFileStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author zhangfeixiang
 * @Date 2019/12/12 16:43
 * @Version 1.0
 */
@Component
@Slf4j
public class FileListener {
	@Autowired
	private FastFileStorageClient storageClient;
	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(value = "leyou.delete.file.queue", durable = "true"),
			exchange = @Exchange(
					value = "leyou.item.exchange",
					ignoreDeclarationExceptions = "true",
					type = ExchangeTypes.TOPIC
			),
			key = {"file.delete", "flie.update"}
	))
	public void deleteFile(List<String> images){
		if (images.size() >1){
			for (String image : images) {
				String[] files = image.split(",");
				if (files.length > 1){
					for (int i = 0; i < files.length; i++) {
						storageClient.deleteFile(files[i]);
						log.info("{}:文件删除成功", StringUtils.substringAfterLast(files[i], "/"));
					}
				}else{
					storageClient.deleteFile(image);
					log.info("{}:文件删除成功", StringUtils.substringAfterLast(image, "/"));
				}
			}
		}
		storageClient.deleteFile(images.get(0));
		log.info("{}:文件删除成功", StringUtils.substringAfterLast(images.get(0), "/"));
	}
}
