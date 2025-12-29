package com.homestay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.homestay.entity.Category;
import com.homestay.exception.BusinessException;
import com.homestay.mapper.CategoryMapper;
import com.homestay.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 分类服务实现
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    
    @Override
    public List<Category> listEnabled() {
        return lambdaQuery()
                .eq(Category::getStatus, 1)
                .orderByAsc(Category::getSortOrder)
                .list();
    }
    
    @Override
    public List<Category> listAll() {
        return lambdaQuery()
                .orderByAsc(Category::getSortOrder)
                .list();
    }
    
    @Override
    public void createCategory(Category category) {
        // 检查名称是否存在
        long count = lambdaQuery()
                .eq(Category::getName, category.getName())
                .count();
        if (count > 0) {
            throw new BusinessException("分类名称已存在");
        }
        
        if (category.getSortOrder() == null) {
            category.setSortOrder(0);
        }
        if (category.getStatus() == null) {
            category.setStatus(1);
        }
        
        save(category);
    }
    
    @Override
    public void updateCategory(Category category) {
        if (category.getId() == null) {
            throw BusinessException.badRequest("ID不能为空");
        }
        
        Category exists = getById(category.getId());
        if (exists == null) {
            throw BusinessException.notFound("分类不存在");
        }
        
        // 检查名称是否重复
        long count = lambdaQuery()
                .eq(Category::getName, category.getName())
                .ne(Category::getId, category.getId())
                .count();
        if (count > 0) {
            throw new BusinessException("分类名称已存在");
        }
        
        updateById(category);
    }
    
    @Override
    public void deleteCategory(Long id) {
        Category category = getById(id);
        if (category == null) {
            throw BusinessException.notFound("分类不存在");
        }
        
        removeById(id);
    }
    
    @Override
    public void updateStatus(Long id, Integer status) {
        Category category = getById(id);
        if (category == null) {
            throw BusinessException.notFound("分类不存在");
        }
        
        category.setStatus(status);
        updateById(category);
    }
}

