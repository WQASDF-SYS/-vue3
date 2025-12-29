package com.homestay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.homestay.common.PageResult;
import com.homestay.entity.Announcement;

import java.util.List;

/**
 * 公告服务接口
 */
public interface AnnouncementService extends IService<Announcement> {
    
    /**
     * 获取最新公告（前台）
     */
    List<Announcement> listLatest(Integer limit);
    
    /**
     * 分页查询公告（前台）
     */
    PageResult<Announcement> pageEnabled(Integer pageNum, Integer pageSize, Integer type);
    
    /**
     * 分页查询公告（管理端）
     */
    PageResult<Announcement> pageAll(Integer pageNum, Integer pageSize, Integer type, Integer status);
    
    /**
     * 获取公告详情
     */
    Announcement getDetail(Long id);
    
    /**
     * 创建公告
     */
    void createAnnouncement(Announcement announcement);
    
    /**
     * 更新公告
     */
    void updateAnnouncement(Announcement announcement);
    
    /**
     * 删除公告
     */
    void deleteAnnouncement(Long id);
    
    /**
     * 更新状态
     */
    void updateStatus(Long id, Integer status);
    
    /**
     * 设置置顶
     */
    void setTop(Long id, Integer isTop);
}

