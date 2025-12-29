package com.homestay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.homestay.common.PageResult;
import com.homestay.entity.Announcement;
import com.homestay.exception.BusinessException;
import com.homestay.mapper.AnnouncementMapper;
import com.homestay.service.AnnouncementService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 公告服务实现
 */
@Service
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement> implements AnnouncementService {
    
    @Override
    public List<Announcement> listLatest(Integer limit) {
        return lambdaQuery()
                .eq(Announcement::getStatus, 1)
                .orderByDesc(Announcement::getIsTop)
                .orderByDesc(Announcement::getCreateTime)
                .last("LIMIT " + limit)
                .list();
    }
    
    @Override
    public PageResult<Announcement> pageEnabled(Integer pageNum, Integer pageSize, Integer type) {
        Page<Announcement> page = new Page<>(pageNum, pageSize);
        
        LambdaQueryWrapper<Announcement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Announcement::getStatus, 1);
        wrapper.eq(type != null, Announcement::getType, type);
        wrapper.orderByDesc(Announcement::getIsTop);
        wrapper.orderByDesc(Announcement::getCreateTime);
        
        return PageResult.of(page(page, wrapper));
    }
    
    @Override
    public PageResult<Announcement> pageAll(Integer pageNum, Integer pageSize, Integer type, Integer status) {
        Page<Announcement> page = new Page<>(pageNum, pageSize);
        
        LambdaQueryWrapper<Announcement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(type != null, Announcement::getType, type);
        wrapper.eq(status != null, Announcement::getStatus, status);
        wrapper.orderByDesc(Announcement::getIsTop);
        wrapper.orderByDesc(Announcement::getCreateTime);
        
        return PageResult.of(page(page, wrapper));
    }
    
    @Override
    public Announcement getDetail(Long id) {
        Announcement announcement = getById(id);
        if (announcement == null) {
            throw BusinessException.notFound("公告不存在");
        }
        
        // 增加浏览次数
        lambdaUpdate()
                .eq(Announcement::getId, id)
                .setSql("view_count = view_count + 1")
                .update();
        
        return announcement;
    }
    
    @Override
    public void createAnnouncement(Announcement announcement) {
        if (announcement.getType() == null) {
            announcement.setType(0);
        }
        if (announcement.getIsTop() == null) {
            announcement.setIsTop(0);
        }
        if (announcement.getViewCount() == null) {
            announcement.setViewCount(0);
        }
        if (announcement.getStatus() == null) {
            announcement.setStatus(1);
        }
        
        save(announcement);
    }
    
    @Override
    public void updateAnnouncement(Announcement announcement) {
        if (announcement.getId() == null) {
            throw BusinessException.badRequest("ID不能为空");
        }
        
        Announcement exists = getById(announcement.getId());
        if (exists == null) {
            throw BusinessException.notFound("公告不存在");
        }
        
        updateById(announcement);
    }
    
    @Override
    public void deleteAnnouncement(Long id) {
        Announcement announcement = getById(id);
        if (announcement == null) {
            throw BusinessException.notFound("公告不存在");
        }
        
        removeById(id);
    }
    
    @Override
    public void updateStatus(Long id, Integer status) {
        Announcement announcement = getById(id);
        if (announcement == null) {
            throw BusinessException.notFound("公告不存在");
        }
        
        announcement.setStatus(status);
        updateById(announcement);
    }
    
    @Override
    public void setTop(Long id, Integer isTop) {
        Announcement announcement = getById(id);
        if (announcement == null) {
            throw BusinessException.notFound("公告不存在");
        }
        
        announcement.setIsTop(isTop);
        updateById(announcement);
    }
}

