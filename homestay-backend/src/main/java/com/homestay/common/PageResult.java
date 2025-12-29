package com.homestay.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果
 * 
 * @param <T> 数据类型
 */
@Data
public class PageResult<T> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /** 数据列表 */
    private List<T> records;
    
    /** 总记录数 */
    private Long total;
    
    /** 每页数量 */
    private Long size;
    
    /** 当前页码 */
    private Long current;
    
    /** 总页数 */
    private Long pages;
    
    public PageResult() {
    }
    
    public PageResult(List<T> records, Long total, Long size, Long current) {
        this.records = records;
        this.total = total;
        this.size = size;
        this.current = current;
        this.pages = (total + size - 1) / size;
    }
    
    /**
     * 从MyBatis Plus分页对象转换
     */
    public static <T> PageResult<T> of(IPage<T> page) {
        PageResult<T> result = new PageResult<>();
        result.setRecords(page.getRecords());
        result.setTotal(page.getTotal());
        result.setSize(page.getSize());
        result.setCurrent(page.getCurrent());
        result.setPages(page.getPages());
        return result;
    }
}

