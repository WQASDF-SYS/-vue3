package com.homestay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.homestay.common.PageResult;
import com.homestay.dto.CommentDTO;
import com.homestay.entity.Comment;

/**
 * 评论服务接口
 */
public interface CommentService extends IService<Comment> {
    
    /**
     * 发表评论
     */
    void addComment(CommentDTO dto);
    
    /**
     * 分页查询民宿评论
     */
    PageResult<Comment> pageByHomestay(Long homestayId, Integer pageNum, Integer pageSize);
    
    /**
     * 查询我的评论
     */
    PageResult<Comment> myComments(Integer pageNum, Integer pageSize);
    
    /**
     * 分页查询所有评论（管理端）
     */
    PageResult<Comment> pageComments(Integer pageNum, Integer pageSize, Long homestayId, Integer status);
    
    /**
     * 审核评论
     */
    void auditComment(Long id, Integer status);
    
    /**
     * 删除评论
     */
    void deleteComment(Long id);
}

