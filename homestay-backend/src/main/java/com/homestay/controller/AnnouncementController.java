package com.homestay.controller;

import com.homestay.common.PageResult;
import com.homestay.common.Result;
import com.homestay.entity.Announcement;
import com.homestay.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 公告控制器
 */
@RestController
@RequestMapping("/api/announcement")
@RequiredArgsConstructor
public class AnnouncementController {
    
    private final AnnouncementService announcementService;
    
    /**
     * 获取最新公告（前台）
     */
    @GetMapping("/latest")
    public Result<List<Announcement>> listLatest(@RequestParam(defaultValue = "5") Integer limit) {
        List<Announcement> list = announcementService.listLatest(limit);
        return Result.ok(list);
    }
    
    /**
     * 分页查询公告（前台）
     */
    @GetMapping("/list")
    public Result<PageResult<Announcement>> pageEnabled(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer type) {
        PageResult<Announcement> result = announcementService.pageEnabled(pageNum, pageSize, type);
        return Result.ok(result);
    }
    
    /**
     * 获取公告详情
     */
    @GetMapping("/{id}")
    public Result<Announcement> getDetail(@PathVariable Long id) {
        Announcement announcement = announcementService.getDetail(id);
        return Result.ok(announcement);
    }
    
    // ==================== 管理端接口 ====================
    
    /**
     * 分页查询公告（管理端）
     */
    @GetMapping("/admin/list")
    public Result<PageResult<Announcement>> pageAll(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) Integer status) {
        PageResult<Announcement> result = announcementService.pageAll(pageNum, pageSize, type, status);
        return Result.ok(result);
    }
    
    /**
     * 创建公告
     */
    @PostMapping("/admin")
    public Result<Void> createAnnouncement(@RequestBody Announcement announcement) {
        announcementService.createAnnouncement(announcement);
        return Result.success("创建成功");
    }
    
    /**
     * 更新公告
     */
    @PutMapping("/admin/{id}")
    public Result<Void> updateAnnouncement(@PathVariable Long id, @RequestBody Announcement announcement) {
        announcement.setId(id);
        announcementService.updateAnnouncement(announcement);
        return Result.success("更新成功");
    }
    
    /**
     * 更新状态
     */
    @PutMapping("/admin/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        announcementService.updateStatus(id, status);
        return Result.success("操作成功");
    }
    
    /**
     * 设置置顶
     */
    @PutMapping("/admin/{id}/top")
    public Result<Void> setTop(@PathVariable Long id, @RequestParam Integer isTop) {
        announcementService.setTop(id, isTop);
        return Result.success("操作成功");
    }
    
    /**
     * 删除公告
     */
    @DeleteMapping("/admin/{id}")
    public Result<Void> deleteAnnouncement(@PathVariable Long id) {
        announcementService.deleteAnnouncement(id);
        return Result.success("删除成功");
    }
}

