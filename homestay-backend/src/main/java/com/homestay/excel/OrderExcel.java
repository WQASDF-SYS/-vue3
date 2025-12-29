package com.homestay.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单Excel导出模型
 */
@Data
public class OrderExcel {
    
    @ExcelProperty("订单号")
    @ColumnWidth(25)
    private String orderNo;
    
    @ExcelProperty("民宿名称")
    @ColumnWidth(20)
    private String homestayName;
    
    @ExcelProperty("用户名")
    @ColumnWidth(15)
    private String userName;
    
    @ExcelProperty("联系人")
    @ColumnWidth(12)
    private String contactName;
    
    @ExcelProperty("联系电话")
    @ColumnWidth(15)
    private String contactPhone;
    
    @ExcelProperty("入住日期")
    @ColumnWidth(12)
    private String checkInDate;
    
    @ExcelProperty("退房日期")
    @ColumnWidth(12)
    private String checkOutDate;
    
    @ExcelProperty("入住人数")
    @ColumnWidth(10)
    private Integer guestCount;
    
    @ExcelProperty("总价(元)")
    @ColumnWidth(12)
    private BigDecimal totalPrice;
    
    @ExcelProperty("订单状态")
    @ColumnWidth(10)
    private String status;
    
    @ExcelProperty("创建时间")
    @ColumnWidth(20)
    private String createTime;
}

