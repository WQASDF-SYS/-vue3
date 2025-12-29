package com.homestay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.homestay.entity.Favorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 收藏Mapper
 */
@Mapper
public interface FavoriteMapper extends BaseMapper<Favorite> {
    
    /**
     * 查询用户收藏列表（带民宿信息）
     */
    @Select("SELECT f.*, h.id as 'homestay.id', h.name as 'homestay.name', h.cover_image as 'homestay.cover_image', " +
            "h.price as 'homestay.price', h.address as 'homestay.address', h.status as 'homestay.status' " +
            "FROM user_favorite f " +
            "LEFT JOIN homestay h ON f.homestay_id = h.id " +
            "WHERE f.user_id = #{userId} AND h.deleted = 0 " +
            "ORDER BY f.create_time DESC")
    Page<Favorite> selectPageWithHomestay(Page<Favorite> page, @Param("userId") Long userId);
}

