package com.homestay.controller;

import com.homestay.common.Result;
import com.homestay.entity.Carousel;
import com.homestay.service.CarouselService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 轮播图控制器
 */
@RestController
@RequestMapping("/api/carousel")
@RequiredArgsConstructor
public class CarouselController {
    
    private final CarouselService carouselService;
    
    /**
     * 获取启用的轮播图（前台）
     */
    @GetMapping("/list")
    public Result<List<Carousel>> listEnabled() {
        List<Carousel> list = carouselService.listEnabled();
        return Result.ok(list);
    }
    
    // ==================== 管理端接口 ====================
    
    /**
     * 获取所有轮播图（管理端）
     */
    @GetMapping("/admin/list")
    public Result<List<Carousel>> listAll() {
        List<Carousel> list = carouselService.listAll();
        return Result.ok(list);
    }
    
    /**
     * 创建轮播图
     */
    @PostMapping("/admin")
    public Result<Void> createCarousel(@RequestBody Carousel carousel) {
        carouselService.createCarousel(carousel);
        return Result.success("创建成功");
    }
    
    /**
     * 更新轮播图
     */
    @PutMapping("/admin/{id}")
    public Result<Void> updateCarousel(@PathVariable Long id, @RequestBody Carousel carousel) {
        carousel.setId(id);
        carouselService.updateCarousel(carousel);
        return Result.success("更新成功");
    }
    
    /**
     * 更新状态
     */
    @PutMapping("/admin/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        carouselService.updateStatus(id, status);
        return Result.success("操作成功");
    }
    
    /**
     * 更新排序
     */
    @PutMapping("/admin/{id}/sort")
    public Result<Void> updateSort(@PathVariable Long id, @RequestParam Integer sortOrder) {
        carouselService.updateSort(id, sortOrder);
        return Result.success("操作成功");
    }
    
    /**
     * 删除轮播图
     */
    @DeleteMapping("/admin/{id}")
    public Result<Void> deleteCarousel(@PathVariable Long id) {
        carouselService.deleteCarousel(id);
        return Result.success("删除成功");
    }
}

