package com.homestay.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.homestay.common.PageResult;
import com.homestay.entity.Favorite;
import com.homestay.exception.BusinessException;
import com.homestay.mapper.FavoriteMapper;
import com.homestay.service.FavoriteService;
import com.homestay.utils.UserContext;
import org.springframework.stereotype.Service;

/**
 * 收藏服务实现
 */
@Service
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite> implements FavoriteService {
    
    @Override
    public void addFavorite(Long homestayId) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw BusinessException.unauthorized();
        }
        
        // 检查是否已收藏
        long count = lambdaQuery()
                .eq(Favorite::getUserId, userId)
                .eq(Favorite::getHomestayId, homestayId)
                .count();
        
        if (count > 0) {
            throw new BusinessException("已收藏过该民宿");
        }
        
        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setHomestayId(homestayId);
        save(favorite);
    }
    
    @Override
    public void removeFavorite(Long homestayId) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw BusinessException.unauthorized();
        }
        
        lambdaUpdate()
                .eq(Favorite::getUserId, userId)
                .eq(Favorite::getHomestayId, homestayId)
                .remove();
    }
    
    @Override
    public boolean toggleFavorite(Long homestayId) {
        if (isFavorite(homestayId)) {
            removeFavorite(homestayId);
            return false;
        } else {
            addFavorite(homestayId);
            return true;
        }
    }
    
    @Override
    public boolean isFavorite(Long homestayId) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return false;
        }
        
        return lambdaQuery()
                .eq(Favorite::getUserId, userId)
                .eq(Favorite::getHomestayId, homestayId)
                .count() > 0;
    }
    
    @Override
    public PageResult<Favorite> myFavorites(Integer pageNum, Integer pageSize) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw BusinessException.unauthorized();
        }
        
        Page<Favorite> page = new Page<>(pageNum, pageSize);
        Page<Favorite> result = baseMapper.selectPageWithHomestay(page, userId);
        return PageResult.of(result);
    }
}

