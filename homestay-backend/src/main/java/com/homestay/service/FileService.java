package com.homestay.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件服务接口
 */
public interface FileService {
    
    /**
     * 上传文件
     * @param file 文件
     * @param folder 子文件夹
     * @return 文件访问URL
     */
    String upload(MultipartFile file, String folder);
    
    /**
     * 删除文件
     * @param fileUrl 文件URL
     */
    void delete(String fileUrl);
}

