package com.homestay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.homestay.entity.Category;

import java.util.List;

/**
 * 分类服务接口
 */
public interface CategoryService extends IService<Category> {
    
    /**
     * 获取所有启用的分类
     */
    List<Category> listEnabled();
    
    /**
     * 获取所有分类（管理端）
     */
    List<Category> listAll();
    
    /**
     * 创建分类
     */
    void createCategory(Category category);
    
    /**
     * 更新分类
     */
    void updateCategory(Category category);
    
    /**
     * 删除分类
     */
    void deleteCategory(Long id);
    
    /**
     * 更新状态
     */
    void updateStatus(Long id, Integer status);
}

