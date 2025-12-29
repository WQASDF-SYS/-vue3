package com.homestay.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 分页查询参数
 */
@Data
public class PageQuery implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /** 当前页码，默认1 */
    private Integer pageNum = 1;
    
    /** 每页数量，默认10 */
    private Integer pageSize = 10;
    
    /** 排序字段 */
    private String orderBy;
    
    /** 排序方式：asc/desc */
    private String orderType = "desc";
    
    /**
     * 获取偏移量
     */
    public int getOffset() {
        return (pageNum - 1) * pageSize;
    }
}

