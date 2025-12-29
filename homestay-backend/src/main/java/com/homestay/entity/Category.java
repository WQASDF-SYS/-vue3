package com.homestay.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 民宿分类实体
 */
@Data
@TableName("homestay_category")
public class Category implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /** 分类ID */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /** 分类名称 */
    private String name;
    
    /** 分类图标 */
    private String icon;
    
    /** 排序号 */
    private Integer sortOrder;
    
    /** 状态(0-禁用 1-启用) */
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
}

