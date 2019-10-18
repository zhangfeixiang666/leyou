package com.leyou.controller;

import com.leyou.service.IUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author zhangfeixiang
 * @Date 2019/10/18 16:47
 * @Version 1.0
 */
@Controller
@RequestMapping("upload")
public class UploadController {
	@Autowired
	private IUploadService uploadService;
	@PostMapping("image")
	public ResponseEntity<String> uploadImage(@RequestParam("file")MultipartFile file){
		return ResponseEntity.ok(uploadService.uploadImage(file));
	}
}
