package com.homestay.service;

import com.homestay.excel.HomestayExcel;
import com.homestay.excel.OrderExcel;
import com.homestay.excel.UserExcel;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Excel导入导出服务接口
 */
public interface ExcelService {
    
    /**
     * 导出用户数据
     */
    void exportUsers(HttpServletResponse response, String keyword, Integer role, Integer status);
    
    /**
     * 导入用户数据
     */
    int importUsers(MultipartFile file);
    
    /**
     * 下载用户导入模板
     */
    void downloadUserTemplate(HttpServletResponse response);
    
    /**
     * 导出民宿数据
     */
    void exportHomestays(HttpServletResponse response, String keyword, Long categoryId, Integer status);
    
    /**
     * 导入民宿数据
     */
    int importHomestays(MultipartFile file);
    
    /**
     * 下载民宿导入模板
     */
    void downloadHomestayTemplate(HttpServletResponse response);
    
    /**
     * 导出订单数据
     */
    void exportOrders(HttpServletResponse response, String keyword, Integer status, String startDate, String endDate);
}

