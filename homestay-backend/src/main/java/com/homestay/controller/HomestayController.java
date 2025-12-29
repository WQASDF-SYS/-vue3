package com.homestay.controller;

import com.homestay.common.PageResult;
import com.homestay.common.Result;
import com.homestay.dto.HomestayDTO;
import com.homestay.entity.Homestay;
import com.homestay.service.FavoriteService;
import com.homestay.service.HomestayService;
import com.homestay.utils.UserContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 民宿控制器
 */
@RestController
@RequestMapping("/api/homestay")
@RequiredArgsConstructor
public class HomestayController {
    
    private final HomestayService homestayService;
    private final FavoriteService favoriteService;
    
    /**
     * 分页查询民宿
     */
    @GetMapping("/list")
    public Result<PageResult<Homestay>> pageHomestays(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer isRecommend) {
        PageResult<Homestay> result = homestayService.pageHomestays(
                pageNum, pageSize, categoryId, keyword, status, isRecommend);
        return Result.ok(result);
    }
    
    /**
     * 获取民宿详情
     */
    @GetMapping("/{id}")
    public Result<Homestay> getDetail(@PathVariable Long id) {
        Homestay homestay = homestayService.getDetail(id);
        
        // 增加浏览次数
        homestayService.increaseViewCount(id);
        
        // 检查是否已收藏
        if (UserContext.getUserId() != null) {
            homestay.setIsFavorite(favoriteService.isFavorite(id));
        }
        
        return Result.ok(homestay);
    }
    
    // ==================== 管理端接口 ====================
    
    /**
     * 创建民宿
     */
    @PostMapping("/admin")
    public Result<Void> createHomestay(@Valid @RequestBody HomestayDTO dto) {
        homestayService.createHomestay(dto);
        return Result.success("创建成功");
    }
    
    /**
     * 更新民宿
     */
    @PutMapping("/admin/{id}")
    public Result<Void> updateHomestay(@PathVariable Long id, @Valid @RequestBody HomestayDTO dto) {
        dto.setId(id);
        homestayService.updateHomestay(dto);
        return Result.success("更新成功");
    }
    
    /**
     * 更新状态（上架/下架）
     */
    @PutMapping("/admin/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        homestayService.updateStatus(id, status);
        return Result.success("操作成功");
    }
    
    /**
     * 设置/取消推荐
     */
    @PutMapping("/admin/{id}/recommend")
    public Result<Void> updateRecommend(@PathVariable Long id, @RequestParam Integer isRecommend) {
        homestayService.updateRecommend(id, isRecommend);
        return Result.success("操作成功");
    }
    
    /**
     * 删除民宿
     */
    @DeleteMapping("/admin/{id}")
    public Result<Void> deleteHomestay(@PathVariable Long id) {
        homestayService.deleteHomestay(id);
        return Result.success("删除成功");
    }
}

