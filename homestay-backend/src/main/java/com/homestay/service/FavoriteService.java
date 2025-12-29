package com.homestay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.homestay.common.PageResult;
import com.homestay.entity.Favorite;

/**
 * 收藏服务接口
 */
public interface FavoriteService extends IService<Favorite> {
    
    /**
     * 添加收藏
     */
    void addFavorite(Long homestayId);
    
    /**
     * 取消收藏
     */
    void removeFavorite(Long homestayId);
    
    /**
     * 切换收藏状态
     */
    boolean toggleFavorite(Long homestayId);
    
    /**
     * 检查是否已收藏
     */
    boolean isFavorite(Long homestayId);
    
    /**
     * 查询我的收藏
     */
    PageResult<Favorite> myFavorites(Integer pageNum, Integer pageSize);
}

