package com.homestay.controller;

import com.homestay.common.PageResult;
import com.homestay.common.Result;
import com.homestay.dto.OrderDTO;
import com.homestay.entity.Order;
import com.homestay.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 订单控制器
 */
@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    
    private final OrderService orderService;
    
    /**
     * 创建订单
     */
    @PostMapping
    public Result<Order> createOrder(@Valid @RequestBody OrderDTO dto) {
        Order order = orderService.createOrder(dto);
        return Result.ok("预订成功", order);
    }
    
    /**
     * 查询我的订单
     */
    @GetMapping("/my")
    public Result<PageResult<Order>> myOrders(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer status) {
        PageResult<Order> result = orderService.myOrders(pageNum, pageSize, status);
        return Result.ok(result);
    }
    
    /**
     * 获取订单详情
     */
    @GetMapping("/{id}")
    public Result<Order> getDetail(@PathVariable Long id) {
        Order order = orderService.getDetail(id);
        return Result.ok(order);
    }
    
    /**
     * 取消订单
     */
    @PutMapping("/{id}/cancel")
    public Result<Void> cancelOrder(@PathVariable Long id, 
                                    @RequestParam(required = false) String reason) {
        orderService.cancelOrder(id, reason);
        return Result.success("取消成功");
    }
    
    // ==================== 管理端接口 ====================
    
    /**
     * 分页查询订单（管理端）
     */
    @GetMapping("/admin/list")
    public Result<PageResult<Order>> pageOrders(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String orderNo) {
        PageResult<Order> result = orderService.pageOrders(pageNum, pageSize, userId, status, orderNo);
        return Result.ok(result);
    }
    
    /**
     * 更新订单状态（管理端）
     */
    @PutMapping("/admin/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, 
                                     @RequestParam Integer status,
                                     @RequestParam(required = false) String reason) {
        orderService.updateStatus(id, status, reason);
        return Result.success("操作成功");
    }
    
    /**
     * 删除订单（管理端）
     */
    @DeleteMapping("/admin/{id}")
    public Result<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return Result.success("删除成功");
    }
    
    /**
     * 获取订单统计（管理端）
     */
    @GetMapping("/admin/statistics")
    public Result<Map<String, Object>> getStatistics() {
        Map<String, Object> stats = orderService.getStatistics();
        return Result.ok(stats);
    }
}

