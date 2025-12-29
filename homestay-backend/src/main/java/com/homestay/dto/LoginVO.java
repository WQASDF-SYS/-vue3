package com.homestay.dto;

import lombok.Data;

/**
 * 登录响应VO
 */
@Data
public class LoginVO {
    
    /** Token */
    private String token;
    
    /** 用户ID */
    private Long userId;
    
    /** 用户名 */
    private String username;
    
    /** 昵称 */
    private String nickName;
    
    /** 头像 */
    private String avatar;
    
    /** 角色 */
    private Integer role;
}

