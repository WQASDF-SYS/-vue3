package com.homestay.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 订单实体
 */
@Data
@TableName("homestay_order")
public class Order implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /** 订单ID */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /** 订单编号 */
    private String orderNo;
    
    /** 用户ID */
    private Long userId;
    
    /** 民宿ID */
    private Long homestayId;
    
    /** 民宿名称 */
    private String homestayName;
    
    /** 入住日期 */
    private LocalDate checkInDate;
    
    /** 退房日期 */
    private LocalDate checkOutDate;
    
    /** 入住天数 */
    private Integer days;
    
    /** 入住人数 */
    private Integer guests;
    
    /** 单价 */
    private BigDecimal unitPrice;
    
    /** 总价 */
    private BigDecimal totalPrice;
    
    /** 入住人姓名 */
    private String guestName;
    
    /** 入住人电话 */
    private String guestPhone;
    
    /** 备注 */
    private String remark;
    
    /** 状态(0-待审核 1-已确认 2-已入住 3-已完成 4-已取消) */
    private Integer status;
    
    /** 支付状态(0-未支付 1-已支付 2-已退款) */
    private Integer payStatus;
    
    /** 取消原因 */
    private String cancelReason;
    
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
    
    /** 民宿封面 */
    @TableField(exist = false)
    private String homestayCover;
}

