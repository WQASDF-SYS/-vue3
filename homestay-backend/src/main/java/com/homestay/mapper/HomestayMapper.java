package com.homestay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.homestay.entity.Homestay;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 民宿Mapper
 */
@Mapper
public interface HomestayMapper extends BaseMapper<Homestay> {
    
    /**
     * 分页查询民宿（带分类名称）
     */
    @Select("<script>" +
            "SELECT h.*, c.name as category_name " +
            "FROM homestay h " +
            "LEFT JOIN homestay_category c ON h.category_id = c.id " +
            "WHERE h.deleted = 0 " +
            "<if test='categoryId != null'> AND h.category_id = #{categoryId} </if>" +
            "<if test='keyword != null and keyword != \"\"'> AND h.name LIKE CONCAT('%', #{keyword}, '%') </if>" +
            "<if test='status != null'> AND h.status = #{status} </if>" +
            "<if test='isRecommend != null'> AND h.is_recommend = #{isRecommend} </if>" +
            "ORDER BY h.is_recommend DESC, h.create_time DESC" +
            "</script>")
    Page<Homestay> selectPageWithCategory(Page<Homestay> page,
                                            @Param("categoryId") Long categoryId,
                                            @Param("keyword") String keyword,
                                            @Param("status") Integer status,
                                            @Param("isRecommend") Integer isRecommend);
    
    /**
     * 查询民宿详情（带分类名称和统计信息）
     */
    @Select("SELECT h.*, c.name as category_name, " +
            "(SELECT IFNULL(AVG(rating), 0) FROM homestay_comment WHERE homestay_id = h.id AND status = 1 AND deleted = 0) as rating, " +
            "(SELECT COUNT(*) FROM homestay_comment WHERE homestay_id = h.id AND status = 1 AND deleted = 0) as comment_count " +
            "FROM homestay h " +
            "LEFT JOIN homestay_category c ON h.category_id = c.id " +
            "WHERE h.id = #{id} AND h.deleted = 0")
    Homestay selectDetailById(@Param("id") Long id);
}

