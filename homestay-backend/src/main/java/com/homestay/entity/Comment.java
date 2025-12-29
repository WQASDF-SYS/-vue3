package com.homestay.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 评论实体
 */
@Data
@TableName("homestay_comment")
public class Comment implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /** 评论ID */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /** 用户ID */
    private Long userId;
    
    /** 民宿ID */
    private Long homestayId;
    
    /** 关联订单ID */
    private Long orderId;
    
    /** 评论内容 */
    private String content;
    
    /** 评分(1-5) */
    private Integer rating;
    
    /** 评论图片 */
    private String images;
    
    /** 状态(0-待审核 1-已通过 2-已拒绝) */
    private Integer status;
    
    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    /** 是否删除 */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;
    
    // ========== 非数据库字段 ==========
    
    /** 用户名 */
    @TableField(exist = false)
    private String userName;
    
    /** 用户头像 */
    @TableField(exist = false)
    private String userAvatar;
    
    /** 民宿名称 */
    @TableField(exist = false)
    private String homestayName;
}

