package com.homestay.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.homestay.entity.Category;
import com.homestay.entity.Homestay;
import com.homestay.entity.Order;
import com.homestay.entity.User;
import com.homestay.excel.HomestayExcel;
import com.homestay.excel.OrderExcel;
import com.homestay.excel.UserExcel;
import com.homestay.exception.BusinessException;
import com.homestay.mapper.HomestayMapper;
import com.homestay.mapper.CategoryMapper;
import com.homestay.mapper.OrderMapper;
import com.homestay.mapper.UserMapper;
import com.homestay.service.ExcelService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Excel导入导出服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExcelServiceImpl implements ExcelService {
    
    private final UserMapper userMapper;
    private final HomestayMapper homestayMapper;
    private final CategoryMapper categoryMapper;
    private final OrderMapper orderMapper;
    
    @Override
    public void exportUsers(HttpServletResponse response, String keyword, Integer role, Integer status) {
        try {
            // 查询数据
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            if (StringUtils.hasText(keyword)) {
                wrapper.and(w -> w.like(User::getUserName, keyword)
                        .or().like(User::getNickName, keyword)
                        .or().like(User::getPhoneNumber, keyword));
            }
            if (role != null) {
                wrapper.eq(User::getRole, role);
            }
            if (status != null) {
                wrapper.eq(User::getStatus, status);
            }
            List<User> users = userMapper.selectList(wrapper);
            
            // 转换为Excel对象
            List<UserExcel> excelList = users.stream().map(u -> {
                UserExcel excel = new UserExcel();
                excel.setUserName(u.getUserName());
                excel.setNickName(u.getNickName());
                excel.setPhoneNumber(u.getPhoneNumber());
                excel.setEmail(u.getEmail());
                excel.setRealName(u.getRealName());
                excel.setGender(convertGender(u.getGender()));
                excel.setRole(convertRole(u.getRole()));
                excel.setStatus(u.getStatus() == 1 ? "启用" : "禁用");
                return excel;
            }).collect(Collectors.toList());
            
            // 设置响应头
            setExcelResponse(response, "用户数据");
            
            // 写入Excel
            EasyExcel.write(response.getOutputStream(), UserExcel.class)
                    .sheet("用户列表")
                    .doWrite(excelList);
        } catch (IOException e) {
            log.error("导出用户数据失败", e);
            throw new BusinessException("导出失败");
        }
    }
    
    @Override
    public int importUsers(MultipartFile file) {
        List<User> importList = new ArrayList<>();
        try {
            EasyExcel.read(file.getInputStream(), UserExcel.class, new ReadListener<UserExcel>() {
                @Override
                public void invoke(UserExcel data, AnalysisContext context) {
                    User user = new User();
                    user.setUserName(data.getUserName());
                    user.setNickName(data.getNickName());
                    user.setPhoneNumber(data.getPhoneNumber());
                    user.setEmail(data.getEmail());
                    user.setRealName(data.getRealName());
                    user.setGender(parseGender(data.getGender()));
                    user.setRole(parseRole(data.getRole()));
                    user.setStatus(1);
                    // 默认密码123456，MD5加密
                    user.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
                    importList.add(user);
                }
                
                @Override
                public void doAfterAllAnalysed(AnalysisContext context) {
                }
            }).sheet().doRead();
            
            // 批量保存
            for (User user : importList) {
                // 检查用户名是否存在
                if (userMapper.selectCount(new LambdaQueryWrapper<User>()
                        .eq(User::getUserName, user.getUserName())) == 0) {
                    userMapper.insert(user);
                }
            }
            return importList.size();
        } catch (IOException e) {
            log.error("导入用户数据失败", e);
            throw new BusinessException("导入失败");
        }
    }
    
    @Override
    public void downloadUserTemplate(HttpServletResponse response) {
        try {
            setExcelResponse(response, "用户导入模板");
            
            // 写入示例数据
            List<UserExcel> templateList = new ArrayList<>();
            UserExcel example = new UserExcel();
            example.setUserName("zhangsan");
            example.setNickName("张三");
            example.setPhoneNumber("13800138000");
            example.setEmail("zhangsan@example.com");
            example.setRealName("张三");
            example.setGender("男");
            example.setRole("普通用户");
            example.setStatus("启用");
            templateList.add(example);
            
            EasyExcel.write(response.getOutputStream(), UserExcel.class)
                    .sheet("用户模板")
                    .doWrite(templateList);
        } catch (IOException e) {
            log.error("下载用户模板失败", e);
            throw new BusinessException("下载失败");
        }
    }
    
    @Override
    public void exportHomestays(HttpServletResponse response, String keyword, Long categoryId, Integer status) {
        try {
            // 查询数据
            LambdaQueryWrapper<Homestay> wrapper = new LambdaQueryWrapper<>();
            if (StringUtils.hasText(keyword)) {
                wrapper.like(Homestay::getName, keyword);
            }
            if (categoryId != null) {
                wrapper.eq(Homestay::getCategoryId, categoryId);
            }
            if (status != null) {
                wrapper.eq(Homestay::getStatus, status);
            }
            List<Homestay> homestays = homestayMapper.selectList(wrapper);
            
            // 获取分类映射
            Map<Long, String> categoryMap = categoryMapper.selectList(null).stream()
                    .collect(Collectors.toMap(Category::getId, Category::getName));
            
            // 转换为Excel对象
            List<HomestayExcel> excelList = homestays.stream().map(h -> {
                HomestayExcel excel = new HomestayExcel();
                excel.setName(h.getName());
                excel.setCategoryName(categoryMap.getOrDefault(h.getCategoryId(), ""));
                excel.setPrice(h.getPrice());
                excel.setAddress(h.getAddress());
                excel.setLocation(h.getLocation());
                excel.setRoomCount(h.getRoomCount());
                excel.setMaxGuests(h.getMaxGuests());
                excel.setContactPhone(h.getContactPhone());
                excel.setViewCount(h.getViewCount());
                excel.setIsRecommend(h.getIsRecommend() == 1 ? "是" : "否");
                excel.setStatus(h.getStatus() == 1 ? "上架" : "下架");
                return excel;
            }).collect(Collectors.toList());
            
            setExcelResponse(response, "民宿数据");
            
            EasyExcel.write(response.getOutputStream(), HomestayExcel.class)
                    .sheet("民宿列表")
                    .doWrite(excelList);
        } catch (IOException e) {
            log.error("导出民宿数据失败", e);
            throw new BusinessException("导出失败");
        }
    }
    
    @Override
    public int importHomestays(MultipartFile file) {
        // 获取分类映射
        Map<String, Long> categoryMap = categoryMapper.selectList(null).stream()
                .collect(Collectors.toMap(Category::getName, Category::getId));
        
        List<Homestay> importList = new ArrayList<>();
        try {
            EasyExcel.read(file.getInputStream(), HomestayExcel.class, new ReadListener<HomestayExcel>() {
                @Override
                public void invoke(HomestayExcel data, AnalysisContext context) {
                    Homestay homestay = new Homestay();
                    homestay.setName(data.getName());
                    homestay.setCategoryId(categoryMap.getOrDefault(data.getCategoryName(), 1L));
                    homestay.setPrice(data.getPrice());
                    homestay.setAddress(data.getAddress());
                    homestay.setLocation(data.getLocation());
                    homestay.setRoomCount(data.getRoomCount());
                    homestay.setMaxGuests(data.getMaxGuests());
                    homestay.setContactPhone(data.getContactPhone());
                    homestay.setViewCount(0);
                    homestay.setIsRecommend("是".equals(data.getIsRecommend()) ? 1 : 0);
                    homestay.setStatus("上架".equals(data.getStatus()) ? 1 : 0);
                    importList.add(homestay);
                }
                
                @Override
                public void doAfterAllAnalysed(AnalysisContext context) {
                }
            }).sheet().doRead();
            
            // 批量保存
            for (Homestay homestay : importList) {
                homestayMapper.insert(homestay);
            }
            return importList.size();
        } catch (IOException e) {
            log.error("导入民宿数据失败", e);
            throw new BusinessException("导入失败");
        }
    }
    
    @Override
    public void downloadHomestayTemplate(HttpServletResponse response) {
        try {
            setExcelResponse(response, "民宿导入模板");
            
            List<HomestayExcel> templateList = new ArrayList<>();
            HomestayExcel example = new HomestayExcel();
            example.setName("示例民宿");
            example.setCategoryName("精品民宿");
            example.setPrice(new java.math.BigDecimal("299"));
            example.setAddress("曲靖市麒麟区示例路1号");
            example.setLocation("麒麟区");
            example.setRoomCount(3);
            example.setMaxGuests(6);
            example.setContactPhone("13800138000");
            example.setViewCount(0);
            example.setIsRecommend("否");
            example.setStatus("上架");
            templateList.add(example);
            
            EasyExcel.write(response.getOutputStream(), HomestayExcel.class)
                    .sheet("民宿模板")
                    .doWrite(templateList);
        } catch (IOException e) {
            log.error("下载民宿模板失败", e);
            throw new BusinessException("下载失败");
        }
    }
    
    @Override
    public void exportOrders(HttpServletResponse response, String keyword, Integer status, String startDate, String endDate) {
        try {
            // 查询数据
            LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
            if (StringUtils.hasText(keyword)) {
                wrapper.and(w -> w.like(Order::getOrderNo, keyword)
                        .or().like(Order::getHomestayName, keyword)
                        .or().like(Order::getGuestName, keyword));
            }
            if (status != null) {
                wrapper.eq(Order::getStatus, status);
            }
            if (StringUtils.hasText(startDate)) {
                wrapper.ge(Order::getCheckInDate, LocalDate.parse(startDate));
            }
            if (StringUtils.hasText(endDate)) {
                wrapper.le(Order::getCheckOutDate, LocalDate.parse(endDate));
            }
            wrapper.orderByDesc(Order::getCreateTime);
            List<Order> orders = orderMapper.selectList(wrapper);
            
            // 获取用户映射
            Map<Long, String> userMap = userMapper.selectList(null).stream()
                    .collect(Collectors.toMap(User::getId, User::getUserName));
            
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            
            // 转换为Excel对象
            List<OrderExcel> excelList = orders.stream().map(o -> {
                OrderExcel excel = new OrderExcel();
                excel.setOrderNo(o.getOrderNo());
                excel.setHomestayName(o.getHomestayName());
                excel.setUserName(userMap.getOrDefault(o.getUserId(), ""));
                excel.setContactName(o.getGuestName());
                excel.setContactPhone(o.getGuestPhone());
                excel.setCheckInDate(o.getCheckInDate() != null ? o.getCheckInDate().format(df) : "");
                excel.setCheckOutDate(o.getCheckOutDate() != null ? o.getCheckOutDate().format(df) : "");
                excel.setGuestCount(o.getGuests());
                excel.setTotalPrice(o.getTotalPrice());
                excel.setStatus(convertOrderStatus(o.getStatus()));
                excel.setCreateTime(o.getCreateTime() != null ? o.getCreateTime().format(dtf) : "");
                return excel;
            }).collect(Collectors.toList());
            
            setExcelResponse(response, "订单数据");
            
            EasyExcel.write(response.getOutputStream(), OrderExcel.class)
                    .sheet("订单列表")
                    .doWrite(excelList);
        } catch (IOException e) {
            log.error("导出订单数据失败", e);
            throw new BusinessException("导出失败");
        }
    }
    
    // ==================== 辅助方法 ====================
    
    private void setExcelResponse(HttpServletResponse response, String fileName) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + encodedFileName + ".xlsx");
    }
    
    private String convertGender(Integer gender) {
        if (gender == null) return "未知";
        return switch (gender) {
            case 1 -> "男";
            case 2 -> "女";
            default -> "未知";
        };
    }
    
    private Integer parseGender(String gender) {
        if (gender == null) return 0;
        return switch (gender) {
            case "男" -> 1;
            case "女" -> 2;
            default -> 0;
        };
    }
    
    private String convertRole(Integer role) {
        if (role == null) return "普通用户";
        return switch (role) {
            case 1 -> "管理员";
            case 2 -> "超级管理员";
            default -> "普通用户";
        };
    }
    
    private Integer parseRole(String role) {
        if (role == null) return 0;
        return switch (role) {
            case "管理员" -> 1;
            case "超级管理员" -> 2;
            default -> 0;
        };
    }
    
    private String convertOrderStatus(Integer status) {
        if (status == null) return "未知";
        return switch (status) {
            case 0 -> "待审核";
            case 1 -> "已确认";
            case 2 -> "已入住";
            case 3 -> "已完成";
            case 4 -> "已取消";
            default -> "未知";
        };
    }
}

