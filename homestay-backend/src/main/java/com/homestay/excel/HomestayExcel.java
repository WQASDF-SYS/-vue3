package com.homestay.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 民宿Excel导入导出模型
 */
@Data
public class HomestayExcel {
    
    @ExcelProperty("民宿名称")
    @ColumnWidth(20)
    private String name;
    
    @ExcelProperty("分类")
    @ColumnWidth(12)
    private String categoryName;
    
    @ExcelProperty("价格(元/晚)")
    @ColumnWidth(12)
    private BigDecimal price;
    
    @ExcelProperty("地址")
    @ColumnWidth(35)
    private String address;
    
    @ExcelProperty("位置/区域")
    @ColumnWidth(15)
    private String location;
    
    @ExcelProperty("房间数量")
    @ColumnWidth(10)
    private Integer roomCount;
    
    @ExcelProperty("最大入住人数")
    @ColumnWidth(12)
    private Integer maxGuests;
    
    @ExcelProperty("联系电话")
    @ColumnWidth(15)
    private String contactPhone;
    
    @ExcelProperty("浏览次数")
    @ColumnWidth(10)
    private Integer viewCount;
    
    @ExcelProperty("是否推荐")
    @ColumnWidth(10)
    private String isRecommend;
    
    @ExcelProperty("状态")
    @ColumnWidth(8)
    private String status;
}

