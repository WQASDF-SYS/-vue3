package com.homestay.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.homestay.common.PageResult;
import com.homestay.dto.HomestayDTO;
import com.homestay.entity.Homestay;
import com.homestay.exception.BusinessException;
import com.homestay.mapper.HomestayMapper;
import com.homestay.service.HomestayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 民宿服务实现
 */
@Service
@RequiredArgsConstructor
public class HomestayServiceImpl extends ServiceImpl<HomestayMapper, Homestay> implements HomestayService {
    
    @Override
    public PageResult<Homestay> pageHomestays(Integer pageNum, Integer pageSize, Long categoryId, 
                                               String keyword, Integer status, Integer isRecommend) {
        Page<Homestay> page = new Page<>(pageNum, pageSize);
        Page<Homestay> result = baseMapper.selectPageWithCategory(page, categoryId, keyword, status, isRecommend);
        return PageResult.of(result);
    }
    
    @Override
    public Homestay getDetail(Long id) {
        Homestay homestay = baseMapper.selectDetailById(id);
        if (homestay == null) {
            throw BusinessException.notFound("民宿不存在");
        }
        return homestay;
    }
    
    @Override
    public void createHomestay(HomestayDTO dto) {
        Homestay homestay = new Homestay();
        BeanUtil.copyProperties(dto, homestay);
        
        // 设置默认值
        if (homestay.getRoomCount() == null) {
            homestay.setRoomCount(1);
        }
        if (homestay.getMaxGuests() == null) {
            homestay.setMaxGuests(2);
        }
        if (homestay.getViewCount() == null) {
            homestay.setViewCount(0);
        }
        if (homestay.getIsRecommend() == null) {
            homestay.setIsRecommend(0);
        }
        if (homestay.getStatus() == null) {
            homestay.setStatus(1);
        }
        
        save(homestay);
    }
    
    @Override
    public void updateHomestay(HomestayDTO dto) {
        if (dto.getId() == null) {
            throw BusinessException.badRequest("ID不能为空");
        }
        
        Homestay homestay = getById(dto.getId());
        if (homestay == null) {
            throw BusinessException.notFound("民宿不存在");
        }
        
        BeanUtil.copyProperties(dto, homestay, "id", "viewCount", "createTime");
        updateById(homestay);
    }
    
    @Override
    public void updateStatus(Long id, Integer status) {
        Homestay homestay = getById(id);
        if (homestay == null) {
            throw BusinessException.notFound("民宿不存在");
        }
        
        homestay.setStatus(status);
        updateById(homestay);
    }
    
    @Override
    public void updateRecommend(Long id, Integer isRecommend) {
        Homestay homestay = getById(id);
        if (homestay == null) {
            throw BusinessException.notFound("民宿不存在");
        }
        
        homestay.setIsRecommend(isRecommend);
        updateById(homestay);
    }
    
    @Override
    public void deleteHomestay(Long id) {
        Homestay homestay = getById(id);
        if (homestay == null) {
            throw BusinessException.notFound("民宿不存在");
        }
        
        removeById(id);
    }
    
    @Override
    public void increaseViewCount(Long id) {
        lambdaUpdate()
                .eq(Homestay::getId, id)
                .setSql("view_count = view_count + 1")
                .update();
    }
}

