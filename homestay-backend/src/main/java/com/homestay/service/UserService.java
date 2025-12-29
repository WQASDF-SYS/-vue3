package com.homestay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.homestay.common.PageResult;
import com.homestay.dto.LoginDTO;
import com.homestay.dto.LoginVO;
import com.homestay.dto.RegisterDTO;
import com.homestay.dto.UserDTO;
import com.homestay.entity.User;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {
    
    /**
     * 用户登录
     */
    LoginVO login(LoginDTO dto);
    
    /**
     * 用户注册
     */
    void register(RegisterDTO dto);
    
    /**
     * 获取当前用户信息
     */
    User getCurrentUser();
    
    /**
     * 更新用户信息
     */
    void updateUser(UserDTO dto);
    
    /**
     * 修改密码
     */
    void updatePassword(String oldPassword, String newPassword);
    
    /**
     * 分页查询用户（管理端）
     */
    PageResult<User> pageUsers(Integer pageNum, Integer pageSize, String keyword, Integer role, Integer status);
    
    /**
     * 创建用户（管理端）
     */
    void createUser(UserDTO dto);
    
    /**
     * 更新用户状态（管理端）
     */
    void updateStatus(Long userId, Integer status);
    
    /**
     * 删除用户（管理端）
     */
    void deleteUser(Long userId);
}

