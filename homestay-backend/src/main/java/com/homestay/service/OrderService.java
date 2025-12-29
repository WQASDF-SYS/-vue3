package com.homestay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.homestay.common.PageResult;
import com.homestay.dto.OrderDTO;
import com.homestay.entity.Order;

import java.util.Map;

/**
 * 订单服务接口
 */
public interface OrderService extends IService<Order> {
    
    /**
     * 创建订单
     */
    Order createOrder(OrderDTO dto);
    
    /**
     * 分页查询订单
     */
    PageResult<Order> pageOrders(Integer pageNum, Integer pageSize, Long userId, 
                                  Integer status, String orderNo);
    
    /**
     * 查询我的订单
     */
    PageResult<Order> myOrders(Integer pageNum, Integer pageSize, Integer status);
    
    /**
     * 获取订单详情
     */
    Order getDetail(Long id);
    
    /**
     * 更新订单状态（管理端）
     */
    void updateStatus(Long id, Integer status, String reason);
    
    /**
     * 取消订单
     */
    void cancelOrder(Long id, String reason);
    
    /**
     * 删除订单
     */
    void deleteOrder(Long id);
    
    /**
     * 获取订单统计数据
     */
    Map<String, Object> getStatistics();
}

