package com.homestay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.homestay.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 订单Mapper
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {
    
    /**
     * 分页查询订单（带用户和民宿信息）
     */
    @Select("<script>" +
            "SELECT o.*, u.user_name, h.cover_image as homestay_cover " +
            "FROM homestay_order o " +
            "LEFT JOIN sys_user u ON o.user_id = u.id " +
            "LEFT JOIN homestay h ON o.homestay_id = h.id " +
            "WHERE o.deleted = 0 " +
            "<if test='userId != null'> AND o.user_id = #{userId} </if>" +
            "<if test='status != null'> AND o.status = #{status} </if>" +
            "<if test='orderNo != null and orderNo != \"\"'> AND o.order_no LIKE CONCAT('%', #{orderNo}, '%') </if>" +
            "ORDER BY o.create_time DESC" +
            "</script>")
    Page<Order> selectPageWithDetail(Page<Order> page,
                                      @Param("userId") Long userId,
                                      @Param("status") Integer status,
                                      @Param("orderNo") String orderNo);
    
    /**
     * 统计各状态订单数量
     */
    @Select("SELECT status, COUNT(*) as count FROM homestay_order WHERE deleted = 0 GROUP BY status")
    List<Map<String, Object>> countByStatus();
    
    /**
     * 统计今日订单
     */
    @Select("SELECT COUNT(*) FROM homestay_order WHERE deleted = 0 AND DATE(create_time) = CURDATE()")
    Integer countToday();
    
    /**
     * 统计本月订单金额
     */
    @Select("SELECT IFNULL(SUM(total_price), 0) FROM homestay_order WHERE deleted = 0 AND status != 4 AND MONTH(create_time) = MONTH(CURDATE())")
    Double sumMonthAmount();
    
    /**
     * 近7天订单趋势
     */
    @Select("SELECT DATE(create_time) as date, COUNT(*) as count " +
            "FROM homestay_order WHERE deleted = 0 AND create_time >= DATE_SUB(CURDATE(), INTERVAL 6 DAY) " +
            "GROUP BY DATE(create_time) ORDER BY date")
    List<Map<String, Object>> countWeekTrend();
    
    /**
     * 近7天收入趋势
     */
    @Select("SELECT DATE(create_time) as date, IFNULL(SUM(total_price), 0) as amount " +
            "FROM homestay_order WHERE deleted = 0 AND status != 4 AND create_time >= DATE_SUB(CURDATE(), INTERVAL 6 DAY) " +
            "GROUP BY DATE(create_time) ORDER BY date")
    List<Map<String, Object>> sumWeekAmount();
}

