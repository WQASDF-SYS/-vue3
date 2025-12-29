package com.homestay.controller;

import com.homestay.common.PageResult;
import com.homestay.common.Result;
import com.homestay.entity.Favorite;
import com.homestay.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 收藏控制器
 */
@RestController
@RequestMapping("/api/favorite")
@RequiredArgsConstructor
public class FavoriteController {
    
    private final FavoriteService favoriteService;
    
    /**
     * 切换收藏状态
     */
    @PostMapping("/toggle/{homestayId}")
    public Result<Map<String, Object>> toggleFavorite(@PathVariable Long homestayId) {
        boolean isFavorite = favoriteService.toggleFavorite(homestayId);
        Map<String, Object> data = new HashMap<>();
        data.put("isFavorite", isFavorite);
        return Result.ok(isFavorite ? "收藏成功" : "取消收藏成功", data);
    }
    
    /**
     * 检查是否已收藏
     */
    @GetMapping("/check/{homestayId}")
    public Result<Map<String, Object>> checkFavorite(@PathVariable Long homestayId) {
        boolean isFavorite = favoriteService.isFavorite(homestayId);
        Map<String, Object> data = new HashMap<>();
        data.put("isFavorite", isFavorite);
        return Result.ok(data);
    }
    
    /**
     * 查询我的收藏
     */
    @GetMapping("/my")
    public Result<PageResult<Favorite>> myFavorites(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageResult<Favorite> result = favoriteService.myFavorites(pageNum, pageSize);
        return Result.ok(result);
    }
}

