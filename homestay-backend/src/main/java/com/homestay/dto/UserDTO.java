package com.homestay.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 用户信息DTO
 */
@Data
public class UserDTO {
    
    private Long id;
    
    private String userName;
    
    private String password;
    
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phoneNumber;
    
    private String nickName;
    
    private String avatarUrl;
    
    private String realName;
    
    private Integer gender;
    
    @Email(message = "邮箱格式不正确")
    private String email;
    
    private Integer role;
    
    private Integer status;
}

