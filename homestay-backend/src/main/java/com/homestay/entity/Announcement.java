package com.homestay.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 公告/资讯实体
 */
@Data
@TableName("announcement")
public class Announcement implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /** 标题 */
    private String title;
    
    /** 内容 */
    private String content;
    
    /** 类型(0-公告 1-资讯) */
    private Integer type;
    
    /** 封面图片 */
    private String coverImage;
    
    /** 是否置顶 */
    private Integer isTop;
    
    /** 浏览次数 */
    private Integer viewCount;
    
    /** 状态(0-隐藏 1-显示) */
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

