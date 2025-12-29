package com.homestay.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.homestay.common.PageResult;
import com.homestay.dto.OrderDTO;
import com.homestay.entity.Homestay;
import com.homestay.entity.Order;
import com.homestay.exception.BusinessException;
import com.homestay.mapper.OrderMapper;
import com.homestay.service.HomestayService;
import com.homestay.service.OrderService;
import com.homestay.utils.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单服务实现
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    
    private final HomestayService homestayService;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order createOrder(OrderDTO dto) {
        // 验证日期
        if (dto.getCheckOutDate().isBefore(dto.getCheckInDate()) || 
            dto.getCheckOutDate().isEqual(dto.getCheckInDate())) {
            throw BusinessException.badRequest("退房日期必须晚于入住日期");
        }
        
        if (dto.getCheckInDate().isBefore(LocalDate.now())) {
            throw BusinessException.badRequest("入住日期不能早于今天");
        }
        
        // 获取民宿信息
        Homestay homestay = homestayService.getById(dto.getHomestayId());
        if (homestay == null) {
            throw BusinessException.notFound("民宿不存在");
        }
        if (homestay.getStatus() != 1) {
            throw new BusinessException("该民宿已下架");
        }
        
        // 计算天数和价格
        long days = ChronoUnit.DAYS.between(dto.getCheckInDate(), dto.getCheckOutDate());
        BigDecimal totalPrice = homestay.getPrice().multiply(BigDecimal.valueOf(days));
        
        // 创建订单
        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setUserId(UserContext.getUserId());
        order.setHomestayId(dto.getHomestayId());
        order.setHomestayName(homestay.getName());
        order.setCheckInDate(dto.getCheckInDate());
        order.setCheckOutDate(dto.getCheckOutDate());
        order.setDays((int) days);
        order.setGuests(dto.getGuests());
        order.setUnitPrice(homestay.getPrice());
        order.setTotalPrice(totalPrice);
        order.setGuestName(dto.getGuestName());
        order.setGuestPhone(dto.getGuestPhone());
        order.setRemark(dto.getRemark());
        order.setStatus(0); // 待审核
        order.setPayStatus(0); // 未支付
        
        save(order);
        return order;
    }
    
    @Override
    public PageResult<Order> pageOrders(Integer pageNum, Integer pageSize, Long userId, 
                                         Integer status, String orderNo) {
        Page<Order> page = new Page<>(pageNum, pageSize);
        Page<Order> result = baseMapper.selectPageWithDetail(page, userId, status, orderNo);
        return PageResult.of(result);
    }
    
    @Override
    public PageResult<Order> myOrders(Integer pageNum, Integer pageSize, Integer status) {
        Long userId = UserContext.getUserId();
        return pageOrders(pageNum, pageSize, userId, status, null);
    }
    
    @Override
    public Order getDetail(Long id) {
        Order order = getById(id);
        if (order == null) {
            throw BusinessException.notFound("订单不存在");
        }
        
        // 普通用户只能查看自己的订单
        if (!UserContext.isAdmin() && !order.getUserId().equals(UserContext.getUserId())) {
            throw BusinessException.forbidden();
        }
        
        return order;
    }
    
    @Override
    public void updateStatus(Long id, Integer status, String reason) {
        Order order = getById(id);
        if (order == null) {
            throw BusinessException.notFound("订单不存在");
        }
        
        order.setStatus(status);
        if (status == 4) { // 取消
            order.setCancelReason(reason);
        }
        
        updateById(order);
    }
    
    @Override
    public void cancelOrder(Long id, String reason) {
        Order order = getById(id);
        if (order == null) {
            throw BusinessException.notFound("订单不存在");
        }
        
        // 普通用户只能取消自己的订单
        if (!UserContext.isAdmin() && !order.getUserId().equals(UserContext.getUserId())) {
            throw BusinessException.forbidden();
        }
        
        // 只有待审核和已确认状态可以取消
        if (order.getStatus() > 1) {
            throw new BusinessException("当前订单状态不可取消");
        }
        
        order.setStatus(4);
        order.setCancelReason(reason);
        updateById(order);
    }
    
    @Override
    public void deleteOrder(Long id) {
        Order order = getById(id);
        if (order == null) {
            throw BusinessException.notFound("订单不存在");
        }
        
        removeById(id);
    }
    
    @Override
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // 各状态订单数量
        List<Map<String, Object>> statusCount = baseMapper.countByStatus();
        stats.put("statusCount", statusCount);
        
        // 今日订单数
        stats.put("todayCount", baseMapper.countToday());
        
        // 本月金额
        stats.put("monthAmount", baseMapper.sumMonthAmount());
        
        // 总订单数
        stats.put("totalCount", count());
        
        // 近7天订单趋势
        List<Map<String, Object>> weekTrend = baseMapper.countWeekTrend();
        stats.put("weekTrend", weekTrend);
        
        // 近7天收入趋势
        List<Map<String, Object>> weekAmount = baseMapper.sumWeekAmount();
        stats.put("weekAmount", weekAmount);
        
        return stats;
    }
    
    /**
     * 生成订单编号
     */
    private String generateOrderNo() {
        return "HS" + System.currentTimeMillis() + IdUtil.fastSimpleUUID().substring(0, 4).toUpperCase();
    }
}

