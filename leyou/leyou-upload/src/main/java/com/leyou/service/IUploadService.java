package com.leyou.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Author zhangfeixiang
 * @Date 2019/10/18 16:50
 * @Version 1.0
 */
public interface IUploadService {
	String uploadImage(MultipartFile file);
}
