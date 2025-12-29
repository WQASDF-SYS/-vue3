package com.homestay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.homestay.entity.Carousel;
import com.homestay.exception.BusinessException;
import com.homestay.mapper.CarouselMapper;
import com.homestay.service.CarouselService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 轮播图服务实现
 */
@Service
public class CarouselServiceImpl extends ServiceImpl<CarouselMapper, Carousel> implements CarouselService {
    
    @Override
    public List<Carousel> listEnabled() {
        return lambdaQuery()
                .eq(Carousel::getStatus, 1)
                .orderByAsc(Carousel::getSortOrder)
                .list();
    }
    
    @Override
    public List<Carousel> listAll() {
        return lambdaQuery()
                .orderByAsc(Carousel::getSortOrder)
                .list();
    }
    
    @Override
    public void createCarousel(Carousel carousel) {
        if (carousel.getSortOrder() == null) {
            carousel.setSortOrder(0);
        }
        if (carousel.getStatus() == null) {
            carousel.setStatus(1);
        }
        if (carousel.getLinkType() == null) {
            carousel.setLinkType(0);
        }
        
        save(carousel);
    }
    
    @Override
    public void updateCarousel(Carousel carousel) {
        if (carousel.getId() == null) {
            throw BusinessException.badRequest("ID不能为空");
        }
        
        Carousel exists = getById(carousel.getId());
        if (exists == null) {
            throw BusinessException.notFound("轮播图不存在");
        }
        
        updateById(carousel);
    }
    
    @Override
    public void deleteCarousel(Long id) {
        Carousel carousel = getById(id);
        if (carousel == null) {
            throw BusinessException.notFound("轮播图不存在");
        }
        
        removeById(id);
    }
    
    @Override
    public void updateStatus(Long id, Integer status) {
        Carousel carousel = getById(id);
        if (carousel == null) {
            throw BusinessException.notFound("轮播图不存在");
        }
        
        carousel.setStatus(status);
        updateById(carousel);
    }
    
    @Override
    public void updateSort(Long id, Integer sortOrder) {
        Carousel carousel = getById(id);
        if (carousel == null) {
            throw BusinessException.notFound("轮播图不存在");
        }
        
        carousel.setSortOrder(sortOrder);
        updateById(carousel);
    }
}

