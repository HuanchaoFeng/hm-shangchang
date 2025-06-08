package com.example.controller;
import com.example.Service.Impl.MinIOFileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/minio")
public class MinioController {

    @Autowired
    private MinIOFileStorageService minIOFileStorageService;

    @PostMapping("/fileupload")
    public String minIo(MultipartFile multipartFile){

        // 检查multipartFile是否为空
        if (multipartFile == null || multipartFile.isEmpty()) {
            return "文件为空，无法处理。";
        }
        try(InputStream inputStream = multipartFile.getInputStream()) {  // 将MultipartFile转换为InputStream
            // 上传到MinIO服务器
            // 这里的文件名可以生成随机的名称，防止重复
            String url = minIOFileStorageService.uploadImgFile("testjpg", "test1.jpg", inputStream);
            return "上传成功";
        } catch (IOException e) {
            // 处理异常，可能是getInputStream()失败
            return "获取InputStream失败：";
        }
    }

}


