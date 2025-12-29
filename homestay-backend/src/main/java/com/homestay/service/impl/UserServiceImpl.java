package com.homestay.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.homestay.common.PageResult;
import com.homestay.dto.LoginDTO;
import com.homestay.dto.LoginVO;
import com.homestay.dto.RegisterDTO;
import com.homestay.dto.UserDTO;
import com.homestay.entity.User;
import com.homestay.exception.BusinessException;
import com.homestay.mapper.UserMapper;
import com.homestay.service.UserService;
import com.homestay.utils.JwtUtils;
import com.homestay.utils.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 用户服务实现
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    
    private final JwtUtils jwtUtils;
    
    @Override
    public LoginVO login(LoginDTO dto) {
        // 查询用户
        User user = lambdaQuery()
                .eq(User::getUserName, dto.getUsername())
                .one();
        
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }
        
        // 验证密码
        if (!BCrypt.checkpw(dto.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        
        // 检查状态
        if (user.getStatus() != 1) {
            throw new BusinessException("账号已被禁用");
        }
        
        // 生成Token
        String token = jwtUtils.generateToken(user.getId(), user.getUserName(), user.getRole());
        
        // 构建响应
        LoginVO vo = new LoginVO();
        vo.setToken(token);
        vo.setUserId(user.getId());
        vo.setUsername(user.getUserName());
        vo.setNickName(user.getNickName());
        vo.setAvatar(user.getAvatarUrl());
        vo.setRole(user.getRole());
        
        return vo;
    }
    
    @Override
    public void register(RegisterDTO dto) {
        // 检查用户名是否存在
        long count = lambdaQuery()
                .eq(User::getUserName, dto.getUsername())
                .count();
        if (count > 0) {
            throw new BusinessException("用户名已存在");
        }
        
        // 创建用户
        User user = new User();
        user.setUserName(dto.getUsername());
        user.setPassword(BCrypt.hashpw(dto.getPassword()));
        user.setPhoneNumber(dto.getPhone());
        user.setNickName(StringUtils.hasText(dto.getNickName()) ? dto.getNickName() : dto.getUsername());
        user.setRole(0); // 普通用户
        user.setStatus(1); // 启用
        
        save(user);
    }
    
    @Override
    public User getCurrentUser() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw BusinessException.unauthorized();
        }
        User user = getById(userId);
        if (user == null) {
            throw BusinessException.unauthorized();
        }
        user.setPassword(null); // 不返回密码
        return user;
    }
    
    @Override
    public void updateUser(UserDTO dto) {
        Long userId = UserContext.getUserId();
        User user = getById(userId);
        if (user == null) {
            throw BusinessException.unauthorized();
        }
        
        // 更新允许修改的字段
        if (StringUtils.hasText(dto.getNickName())) {
            user.setNickName(dto.getNickName());
        }
        if (StringUtils.hasText(dto.getPhoneNumber())) {
            user.setPhoneNumber(dto.getPhoneNumber());
        }
        if (StringUtils.hasText(dto.getAvatarUrl())) {
            user.setAvatarUrl(dto.getAvatarUrl());
        }
        if (StringUtils.hasText(dto.getRealName())) {
            user.setRealName(dto.getRealName());
        }
        if (dto.getGender() != null) {
            user.setGender(dto.getGender());
        }
        if (StringUtils.hasText(dto.getEmail())) {
            user.setEmail(dto.getEmail());
        }
        
        updateById(user);
    }
    
    @Override
    public void updatePassword(String oldPassword, String newPassword) {
        Long userId = UserContext.getUserId();
        User user = getById(userId);
        if (user == null) {
            throw BusinessException.unauthorized();
        }
        
        // 验证旧密码
        if (!BCrypt.checkpw(oldPassword, user.getPassword())) {
            throw new BusinessException("原密码错误");
        }
        
        // 更新密码
        user.setPassword(BCrypt.hashpw(newPassword));
        updateById(user);
    }
    
    @Override
    public PageResult<User> pageUsers(Integer pageNum, Integer pageSize, String keyword, Integer role, Integer status) {
        Page<User> page = new Page<>(pageNum, pageSize);
        
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(keyword), User::getUserName, keyword)
                .or()
                .like(StringUtils.hasText(keyword), User::getPhoneNumber, keyword);
        wrapper.eq(role != null, User::getRole, role);
        wrapper.eq(status != null, User::getStatus, status);
        wrapper.orderByDesc(User::getCreateTime);
        
        Page<User> result = page(page, wrapper);
        
        // 隐藏密码
        result.getRecords().forEach(u -> u.setPassword(null));
        
        return PageResult.of(result);
    }
    
    @Override
    public void createUser(UserDTO dto) {
        // 检查用户名是否存在
        long count = lambdaQuery()
                .eq(User::getUserName, dto.getUserName())
                .count();
        if (count > 0) {
            throw new BusinessException("用户名已存在");
        }
        
        User user = new User();
        BeanUtil.copyProperties(dto, user);
        user.setPassword(BCrypt.hashpw(dto.getPassword()));
        if (user.getStatus() == null) {
            user.setStatus(1);
        }
        if (user.getRole() == null) {
            user.setRole(0);
        }
        
        save(user);
    }
    
    @Override
    public void updateStatus(Long userId, Integer status) {
        User user = getById(userId);
        if (user == null) {
            throw BusinessException.notFound("用户不存在");
        }
        
        // 不能禁用超级管理员
        if (user.getRole() == 2 && status == 0) {
            throw new BusinessException("不能禁用超级管理员");
        }
        
        user.setStatus(status);
        updateById(user);
    }
    
    @Override
    public void deleteUser(Long userId) {
        User user = getById(userId);
        if (user == null) {
            throw BusinessException.notFound("用户不存在");
        }
        
        // 不能删除超级管理员
        if (user.getRole() == 2) {
            throw new BusinessException("不能删除超级管理员");
        }
        
        removeById(userId);
    }
}

