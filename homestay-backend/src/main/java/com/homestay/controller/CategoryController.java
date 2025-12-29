package com.homestay.controller;

import com.homestay.common.Result;
import com.homestay.entity.Category;
import com.homestay.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类控制器
 */
@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {
    
    private final CategoryService categoryService;
    
    /**
     * 获取启用的分类列表（前台）
     */
    @GetMapping("/list")
    public Result<List<Category>> listEnabled() {
        List<Category> list = categoryService.listEnabled();
        return Result.ok(list);
    }
    
    // ==================== 管理端接口 ====================
    
    /**
     * 获取所有分类（管理端）
     */
    @GetMapping("/admin/list")
    public Result<List<Category>> listAll() {
        List<Category> list = categoryService.listAll();
        return Result.ok(list);
    }
    
    /**
     * 创建分类
     */
    @PostMapping("/admin")
    public Result<Void> createCategory(@RequestBody Category category) {
        categoryService.createCategory(category);
        return Result.success("创建成功");
    }
    
    /**
     * 更新分类
     */
    @PutMapping("/admin/{id}")
    public Result<Void> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        category.setId(id);
        categoryService.updateCategory(category);
        return Result.success("更新成功");
    }
    
    /**
     * 更新状态
     */
    @PutMapping("/admin/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        categoryService.updateStatus(id, status);
        return Result.success("操作成功");
    }
    
    /**
     * 删除分类
     */
    @DeleteMapping("/admin/{id}")
    public Result<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return Result.success("删除成功");
    }
}

