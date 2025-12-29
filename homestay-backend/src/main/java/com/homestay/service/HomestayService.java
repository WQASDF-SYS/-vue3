package com.homestay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.homestay.common.PageResult;
import com.homestay.dto.HomestayDTO;
import com.homestay.entity.Homestay;

/**
 * 民宿服务接口
 */
public interface HomestayService extends IService<Homestay> {
    
    /**
     * 分页查询民宿
     */
    PageResult<Homestay> pageHomestays(Integer pageNum, Integer pageSize, Long categoryId, 
                                        String keyword, Integer status, Integer isRecommend);
    
    /**
     * 获取民宿详情
     */
    Homestay getDetail(Long id);
    
    /**
     * 创建民宿
     */
    void createHomestay(HomestayDTO dto);
    
    /**
     * 更新民宿
     */
    void updateHomestay(HomestayDTO dto);
    
    /**
     * 更新民宿状态（上架/下架）
     */
    void updateStatus(Long id, Integer status);
    
    /**
     * 设置/取消推荐
     */
    void updateRecommend(Long id, Integer isRecommend);
    
    /**
     * 删除民宿
     */
    void deleteHomestay(Long id);
    
    /**
     * 增加浏览次数
     */
    void increaseViewCount(Long id);
}

