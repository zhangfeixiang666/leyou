package com.leyou.service.impl;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.domain.ThumbImageConfig;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.leyou.common.Exception.LyException;
import com.leyou.common.ExceptionEnum;
import com.leyou.service.IUploadService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @Author zhangfeixiang
 * @Date 2019/10/18 16:51
 * @Version 1.0
 */
@Service
public class UploadServiceImpl implements IUploadService {
	//支持的文件类型
	private static final List<String> suffixes = Arrays.asList("image/png", "image/jpeg");
	@Autowired
	private FastFileStorageClient storageClient;
	@Autowired
	private ThumbImageConfig thumbImageConfig;
	@Override
	public String uploadImage(MultipartFile file) {
		//1图片信息校验
		//校验文件类型
		String type = file.getContentType();
		if (!suffixes.contains(type)){
			throw new LyException(ExceptionEnum.INVALID_FILE_TYPE);
		}
		//校验图片内容
		try {
			BufferedImage image = ImageIO.read(file.getInputStream());
			if (image == null){
				throw new LyException(ExceptionEnum.INVALID_FILE_TYPE);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		//2将图片上传到FastDFS
		//获取文件后缀名
		String extension = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
		//上传
		StorePath storePath = null;
		try {
		 storePath = storageClient.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(), extension, null);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "http://image.leyou.com/"+storePath.getFullPath();
	}
}
