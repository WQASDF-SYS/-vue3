package com.homestay.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 民宿实体
 */
@Data
@TableName("homestay")
public class Homestay implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /** 民宿ID */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /** 民宿名称 */
    private String name;
    
    /** 分类ID */
    private Long categoryId;
    
    /** 封面图片 */
    private String coverImage;
    
    /** 图片列表(JSON数组) */
    private String images;
    
    /** 详细描述 */
    private String description;
    
    /** 每晚价格 */
    private BigDecimal price;
    
    /** 详细地址 */
    private String address;
    
    /** 位置/区域 */
    private String location;
    
    /** 纬度 */
    private BigDecimal latitude;
    
    /** 经度 */
    private BigDecimal longitude;
    
    /** 房间数量 */
    private Integer roomCount;
    
    /** 最大入住人数 */
    private Integer maxGuests;
    
    /** 设施(JSON数组) */
    private String facilities;
    
    /** 联系电话 */
    private String contactPhone;
    
    /** 浏览次数 */
    private Integer viewCount;
    
    /** 是否推荐 */
    private Integer isRecommend;
    
    /** 状态(0-下架 1-上架) */
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
    
    /** 分类名称 */
    @TableField(exist = false)
    private String categoryName;
    
    /** 是否已收藏 */
    @TableField(exist = false)
    private Boolean isFavorite;
    
    /** 评分 */
    @TableField(exist = false)
    private Double rating;
    
    /** 评论数量 */
    @TableField(exist = false)
    private Integer commentCount;
}

