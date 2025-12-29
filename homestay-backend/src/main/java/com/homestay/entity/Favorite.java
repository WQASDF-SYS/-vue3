package com.homestay.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 收藏实体
 */
@Data
@TableName("user_favorite")
public class Favorite implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /** 用户ID */
    private Long userId;
    
    /** 民宿ID */
    private Long homestayId;
    
    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    // ========== 非数据库字段 ==========
    
    /** 民宿信息 */
    @TableField(exist = false)
    private Homestay homestay;
}

