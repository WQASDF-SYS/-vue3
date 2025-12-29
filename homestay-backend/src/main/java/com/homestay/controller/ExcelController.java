package com.homestay.controller;

import com.homestay.common.Result;
import com.homestay.service.ExcelService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * Excel导入导出控制器
 */
@RestController
@RequestMapping("/api/excel")
@RequiredArgsConstructor
public class ExcelController {
    
    private final ExcelService excelService;
    
    // ==================== 用户相关 ====================
    
    /**
     * 导出用户数据
     */
    @GetMapping("/user/export")
    public void exportUsers(HttpServletResponse response,
                           @RequestParam(required = false) String keyword,
                           @RequestParam(required = false) Integer role,
                           @RequestParam(required = false) Integer status) {
        excelService.exportUsers(response, keyword, role, status);
    }
    
    /**
     * 导入用户数据
     */
    @PostMapping("/user/import")
    public Result<Map<String, Object>> importUsers(@RequestParam("file") MultipartFile file) {
        int count = excelService.importUsers(file);
        Map<String, Object> data = new HashMap<>();
        data.put("count", count);
        return Result.ok("成功导入 " + count + " 条数据", data);
    }
    
    /**
     * 下载用户导入模板
     */
    @GetMapping("/user/template")
    public void downloadUserTemplate(HttpServletResponse response) {
        excelService.downloadUserTemplate(response);
    }
    
    // ==================== 民宿相关 ====================
    
    /**
     * 导出民宿数据
     */
    @GetMapping("/homestay/export")
    public void exportHomestays(HttpServletResponse response,
                               @RequestParam(required = false) String keyword,
                               @RequestParam(required = false) Long categoryId,
                               @RequestParam(required = false) Integer status) {
        excelService.exportHomestays(response, keyword, categoryId, status);
    }
    
    /**
     * 导入民宿数据
     */
    @PostMapping("/homestay/import")
    public Result<Map<String, Object>> importHomestays(@RequestParam("file") MultipartFile file) {
        int count = excelService.importHomestays(file);
        Map<String, Object> data = new HashMap<>();
        data.put("count", count);
        return Result.ok("成功导入 " + count + " 条数据", data);
    }
    
    /**
     * 下载民宿导入模板
     */
    @GetMapping("/homestay/template")
    public void downloadHomestayTemplate(HttpServletResponse response) {
        excelService.downloadHomestayTemplate(response);
    }
    
    // ==================== 订单相关 ====================
    
    /**
     * 导出订单数据
     */
    @GetMapping("/order/export")
    public void exportOrders(HttpServletResponse response,
                            @RequestParam(required = false) String keyword,
                            @RequestParam(required = false) Integer status,
                            @RequestParam(required = false) String startDate,
                            @RequestParam(required = false) String endDate) {
        excelService.exportOrders(response, keyword, status, startDate, endDate);
    }
}

