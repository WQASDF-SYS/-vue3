package com.homestay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.homestay.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 评论Mapper
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
    
    /**
     * 分页查询评论（带用户信息）
     */
    @Select("<script>" +
            "SELECT c.*, u.nick_name as user_name, u.avatar_url as user_avatar, h.name as homestay_name " +
            "FROM homestay_comment c " +
            "LEFT JOIN sys_user u ON c.user_id = u.id " +
            "LEFT JOIN homestay h ON c.homestay_id = h.id " +
            "WHERE c.deleted = 0 " +
            "<if test='homestayId != null'> AND c.homestay_id = #{homestayId} </if>" +
            "<if test='userId != null'> AND c.user_id = #{userId} </if>" +
            "<if test='status != null'> AND c.status = #{status} </if>" +
            "ORDER BY c.create_time DESC" +
            "</script>")
    Page<Comment> selectPageWithUser(Page<Comment> page,
                                      @Param("homestayId") Long homestayId,
                                      @Param("userId") Long userId,
                                      @Param("status") Integer status);
}

