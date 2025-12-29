package com.homestay.controller;

import com.homestay.common.PageResult;
import com.homestay.common.Result;
import com.homestay.dto.CommentDTO;
import com.homestay.entity.Comment;
import com.homestay.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 评论控制器
 */
@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {
    
    private final CommentService commentService;
    
    /**
     * 发表评论
     */
    @PostMapping
    public Result<Void> addComment(@Valid @RequestBody CommentDTO dto) {
        commentService.addComment(dto);
        return Result.success("评论成功");
    }
    
    /**
     * 查询民宿评论
     */
    @GetMapping("/homestay/{homestayId}")
    public Result<PageResult<Comment>> pageByHomestay(
            @PathVariable Long homestayId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageResult<Comment> result = commentService.pageByHomestay(homestayId, pageNum, pageSize);
        return Result.ok(result);
    }
    
    /**
     * 查询我的评论
     */
    @GetMapping("/my")
    public Result<PageResult<Comment>> myComments(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageResult<Comment> result = commentService.myComments(pageNum, pageSize);
        return Result.ok(result);
    }
    
    /**
     * 删除评论
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return Result.success("删除成功");
    }
    
    // ==================== 管理端接口 ====================
    
    /**
     * 分页查询评论（管理端）
     */
    @GetMapping("/admin/list")
    public Result<PageResult<Comment>> pageComments(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long homestayId,
            @RequestParam(required = false) Integer status) {
        PageResult<Comment> result = commentService.pageComments(pageNum, pageSize, homestayId, status);
        return Result.ok(result);
    }
    
    /**
     * 审核评论
     */
    @PutMapping("/admin/{id}/audit")
    public Result<Void> auditComment(@PathVariable Long id, @RequestParam Integer status) {
        commentService.auditComment(id, status);
        return Result.success("操作成功");
    }
    
    /**
     * 删除评论（管理端）
     */
    @DeleteMapping("/admin/{id}")
    public Result<Void> adminDeleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return Result.success("删除成功");
    }
}

