package com.homestay.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 轮播图实体
 */
@Data
@TableName("carousel")
public class Carousel implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /** 标题 */
    private String title;
    
    /** 图片URL */
    private String imageUrl;
    
    /** 跳转链接 */
    private String linkUrl;
    
    /** 链接类型(0-无 1-民宿详情 2-外部链接) */
    private Integer linkType;
    
    /** 目标ID */
    private Long targetId;
    
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

