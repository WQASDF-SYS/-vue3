package com.homestay.controller;

import com.homestay.common.PageResult;
import com.homestay.common.Result;
import com.homestay.dto.UserDTO;
import com.homestay.entity.User;
import com.homestay.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    /**
     * 获取当前用户信息
     */
    @GetMapping("/info")
    public Result<User> getCurrentUser() {
        User user = userService.getCurrentUser();
        return Result.ok(user);
    }
    
    /**
     * 更新用户信息
     */
    @PutMapping("/info")
    public Result<Void> updateUser(@Valid @RequestBody UserDTO dto) {
        userService.updateUser(dto);
        return Result.success("更新成功");
    }
    
    /**
     * 修改密码
     */
    @PutMapping("/password")
    public Result<Void> updatePassword(@RequestParam String oldPassword, 
                                        @RequestParam String newPassword) {
        userService.updatePassword(oldPassword, newPassword);
        return Result.success("密码修改成功");
    }
    
    // ==================== 管理端接口 ====================
    
    /**
     * 分页查询用户
     */
    @GetMapping("/admin/list")
    public Result<PageResult<User>> pageUsers(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer role,
            @RequestParam(required = false) Integer status) {
        PageResult<User> result = userService.pageUsers(pageNum, pageSize, keyword, role, status);
        return Result.ok(result);
    }
    
    /**
     * 创建用户
     */
    @PostMapping("/admin")
    public Result<Void> createUser(@Valid @RequestBody UserDTO dto) {
        userService.createUser(dto);
        return Result.success("创建成功");
    }
    
    /**
     * 更新用户（管理端）
     */
    @PutMapping("/admin/{id}")
    public Result<Void> updateUserAdmin(@PathVariable Long id, @Valid @RequestBody UserDTO dto) {
        dto.setId(id);
        User user = new User();
        user.setId(id);
        user.setNickName(dto.getNickName());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        user.setStatus(dto.getStatus());
        userService.updateById(user);
        return Result.success("更新成功");
    }
    
    /**
     * 更新用户状态
     */
    @PutMapping("/admin/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        userService.updateStatus(id, status);
        return Result.success("操作成功");
    }
    
    /**
     * 删除用户
     */
    @DeleteMapping("/admin/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success("删除成功");
    }
}

