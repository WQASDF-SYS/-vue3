package com.homestay.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.homestay.exception.BusinessException;
import com.homestay.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

/**
 * 文件服务实现
 */
@Slf4j
@Service
public class FileServiceImpl implements FileService {
    
    @Value("${upload.path}")
    private String uploadPath;
    
    @Value("${upload.url-prefix}")
    private String urlPrefix;
    
    /** 允许的图片类型 */
    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "image/jpeg", "image/png", "image/gif", "image/webp"
    );
    
    /** 允许的文件类型 */
    private static final List<String> ALLOWED_FILE_TYPES = Arrays.asList(
            "image/jpeg", "image/png", "image/gif", "image/webp",
            "application/pdf", "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/vnd.ms-excel",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    );
    
    @Override
    public String upload(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) {
            throw BusinessException.badRequest("文件不能为空");
        }
        
        // 验证文件类型
        String contentType = file.getContentType();
        if (!ALLOWED_FILE_TYPES.contains(contentType)) {
            throw BusinessException.badRequest("不支持的文件类型");
        }
        
        // 生成文件名
        String originalFilename = file.getOriginalFilename();
        String extension = FileUtil.extName(originalFilename);
        String newFilename = IdUtil.fastSimpleUUID() + "." + extension;
        
        // 按日期分目录
        String dateFolder = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String relativePath = folder + "/" + dateFolder;
        String fullPath = uploadPath + "/" + relativePath;
        
        // 创建目录
        File dir = new File(fullPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        // 保存文件
        try {
            File destFile = new File(fullPath + "/" + newFilename);
            file.transferTo(destFile);
            log.info("文件上传成功: {}", destFile.getAbsolutePath());
            
            // 返回访问URL
            return urlPrefix + "/" + relativePath + "/" + newFilename;
        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new BusinessException("文件上传失败");
        }
    }
    
    @Override
    public void delete(String fileUrl) {
        if (fileUrl == null || !fileUrl.startsWith(urlPrefix)) {
            return;
        }
        
        String relativePath = fileUrl.substring(urlPrefix.length());
        String fullPath = uploadPath + relativePath;
        
        File file = new File(fullPath);
        if (file.exists()) {
            file.delete();
            log.info("文件删除成功: {}", fullPath);
        }
    }
}

