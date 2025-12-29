package com.homestay.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.homestay.common.PageResult;
import com.homestay.dto.CommentDTO;
import com.homestay.entity.Comment;
import com.homestay.exception.BusinessException;
import com.homestay.mapper.CommentMapper;
import com.homestay.service.CommentService;
import com.homestay.utils.UserContext;
import org.springframework.stereotype.Service;

/**
 * 评论服务实现
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    
    @Override
    public void addComment(CommentDTO dto) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw BusinessException.unauthorized();
        }
        
        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setHomestayId(dto.getHomestayId());
        comment.setOrderId(dto.getOrderId());
        comment.setContent(dto.getContent());
        comment.setRating(dto.getRating());
        comment.setImages(dto.getImages());
        comment.setStatus(1); // 默认通过，可改为0待审核
        
        save(comment);
    }
    
    @Override
    public PageResult<Comment> pageByHomestay(Long homestayId, Integer pageNum, Integer pageSize) {
        Page<Comment> page = new Page<>(pageNum, pageSize);
        Page<Comment> result = baseMapper.selectPageWithUser(page, homestayId, null, 1);
        return PageResult.of(result);
    }
    
    @Override
    public PageResult<Comment> myComments(Integer pageNum, Integer pageSize) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw BusinessException.unauthorized();
        }
        
        Page<Comment> page = new Page<>(pageNum, pageSize);
        Page<Comment> result = baseMapper.selectPageWithUser(page, null, userId, null);
        return PageResult.of(result);
    }
    
    @Override
    public PageResult<Comment> pageComments(Integer pageNum, Integer pageSize, Long homestayId, Integer status) {
        Page<Comment> page = new Page<>(pageNum, pageSize);
        Page<Comment> result = baseMapper.selectPageWithUser(page, homestayId, null, status);
        return PageResult.of(result);
    }
    
    @Override
    public void auditComment(Long id, Integer status) {
        Comment comment = getById(id);
        if (comment == null) {
            throw BusinessException.notFound("评论不存在");
        }
        
        comment.setStatus(status);
        updateById(comment);
    }
    
    @Override
    public void deleteComment(Long id) {
        Comment comment = getById(id);
        if (comment == null) {
            throw BusinessException.notFound("评论不存在");
        }
        
        // 普通用户只能删除自己的评论
        if (!UserContext.isAdmin() && !comment.getUserId().equals(UserContext.getUserId())) {
            throw BusinessException.forbidden();
        }
        
        removeById(id);
    }
}

