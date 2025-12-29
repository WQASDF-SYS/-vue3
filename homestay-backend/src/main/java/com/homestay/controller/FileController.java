package com.homestay.controller;

import com.homestay.common.Result;
import com.homestay.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件控制器
 */
@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileController {
    
    private final FileService fileService;
    
    /**
     * 上传图片
     */
    @PostMapping("/upload/image")
    public Result<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        String url = fileService.upload(file, "images");
        Map<String, String> data = new HashMap<>();
        data.put("url", url);
        return Result.ok("上传成功", data);
    }
    
    /**
     * 上传文件
     */
    @PostMapping("/upload/file")
    public Result<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        String url = fileService.upload(file, "files");
        Map<String, String> data = new HashMap<>();
        data.put("url", url);
        return Result.ok("上传成功", data);
    }
    
    /**
     * 删除文件
     */
    @DeleteMapping("/delete")
    public Result<Void> deleteFile(@RequestParam String url) {
        fileService.delete(url);
        return Result.success("删除成功");
    }
}

