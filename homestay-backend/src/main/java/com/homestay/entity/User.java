package com.homestay.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户实体
 */
@Data
@TableName("sys_user")
public class User implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /** 用户ID */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /** 用户名 */
    private String userName;
    
    /** 密码 */
    private String password;
    
    /** 手机号码 */
    private String phoneNumber;
    
    /** 昵称 */
    private String nickName;
    
    /** 头像URL */
    private String avatarUrl;
    
    /** 真实姓名 */
    private String realName;
    
    /** 性别(0-未知 1-男 2-女) */
    private Integer gender;
    
    /** 邮箱 */
    private String email;
    
    /** 角色(0-普通用户 1-管理员 2-超级管理员) */
    private Integer role;
    
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

