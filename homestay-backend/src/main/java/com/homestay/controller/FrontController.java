package com.homestay.controller;

import com.homestay.common.PageResult;
import com.homestay.common.Result;
import com.homestay.entity.*;
import com.homestay.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 前台公开接口控制器（无需登录）
 */
@RestController
@RequestMapping("/api/front")
@RequiredArgsConstructor
public class FrontController {
    
    private final HomestayService homestayService;
    private final CategoryService categoryService;
    private final CarouselService carouselService;
    private final AnnouncementService announcementService;
    private final CommentService commentService;
    
    /**
     * 获取首页数据
     */
    @GetMapping("/home")
    public Result<Map<String, Object>> getHomeData() {
        Map<String, Object> data = new HashMap<>();
        
        // 轮播图
        data.put("carousels", carouselService.listEnabled());
        
        // 分类列表
        data.put("categories", categoryService.listEnabled());
        
        // 推荐民宿
        PageResult<Homestay> recommendList = homestayService.pageHomestays(1, 8, null, null, 1, 1);
        data.put("recommendList", recommendList.getRecords());
        
        // 最新民宿
        PageResult<Homestay> latestList = homestayService.pageHomestays(1, 8, null, null, 1, null);
        data.put("latestList", latestList.getRecords());
        
        // 最新公告
        data.put("announcements", announcementService.listLatest(5));
        
        return Result.ok(data);
    }
    
    /**
     * 获取轮播图
     */
    @GetMapping("/carousel")
    public Result<List<Carousel>> getCarousels() {
        return Result.ok(carouselService.listEnabled());
    }
    
    /**
     * 获取分类列表
     */
    @GetMapping("/category")
    public Result<List<Category>> getCategories() {
        return Result.ok(categoryService.listEnabled());
    }
    
    /**
     * 分页查询民宿
     */
    @GetMapping("/homestay/list")
    public Result<PageResult<Homestay>> pageHomestays(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword) {
        // 只查询上架的民宿
        PageResult<Homestay> result = homestayService.pageHomestays(
                pageNum, pageSize, categoryId, keyword, 1, null);
        return Result.ok(result);
    }
    
    /**
     * 获取民宿详情
     */
    @GetMapping("/homestay/{id}")
    public Result<Homestay> getHomestayDetail(@PathVariable Long id) {
        Homestay homestay = homestayService.getDetail(id);
        homestayService.increaseViewCount(id);
        return Result.ok(homestay);
    }
    
    /**
     * 获取民宿评论
     */
    @GetMapping("/homestay/{id}/comments")
    public Result<PageResult<Comment>> getHomestayComments(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageResult<Comment> result = commentService.pageByHomestay(id, pageNum, pageSize);
        return Result.ok(result);
    }
    
    /**
     * 获取公告列表
     */
    @GetMapping("/announcement/list")
    public Result<PageResult<Announcement>> pageAnnouncements(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer type) {
        PageResult<Announcement> result = announcementService.pageEnabled(pageNum, pageSize, type);
        return Result.ok(result);
    }
    
    /**
     * 获取公告详情
     */
    @GetMapping("/announcement/{id}")
    public Result<Announcement> getAnnouncementDetail(@PathVariable Long id) {
        Announcement announcement = announcementService.getDetail(id);
        return Result.ok(announcement);
    }
}

