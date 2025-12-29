package com.homestay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.homestay.entity.Carousel;

import java.util.List;

/**
 * 轮播图服务接口
 */
public interface CarouselService extends IService<Carousel> {
    
    /**
     * 获取启用的轮播图（前台）
     */
    List<Carousel> listEnabled();
    
    /**
     * 获取所有轮播图（管理端）
     */
    List<Carousel> listAll();
    
    /**
     * 创建轮播图
     */
    void createCarousel(Carousel carousel);
    
    /**
     * 更新轮播图
     */
    void updateCarousel(Carousel carousel);
    
    /**
     * 删除轮播图
     */
    void deleteCarousel(Long id);
    
    /**
     * 更新状态
     */
    void updateStatus(Long id, Integer status);
    
    /**
     * 更新排序
     */
    void updateSort(Long id, Integer sortOrder);
}

