package com.homestay.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

/**
 * 用户Excel导入导出模型
 */
@Data
public class UserExcel {
    
    @ExcelProperty("用户名")
    @ColumnWidth(15)
    private String userName;
    
    @ExcelProperty("昵称")
    @ColumnWidth(15)
    private String nickName;
    
    @ExcelProperty("手机号码")
    @ColumnWidth(15)
    private String phoneNumber;
    
    @ExcelProperty("邮箱")
    @ColumnWidth(25)
    private String email;
    
    @ExcelProperty("真实姓名")
    @ColumnWidth(12)
    private String realName;
    
    @ExcelProperty("性别")
    @ColumnWidth(8)
    private String gender;
    
    @ExcelProperty("角色")
    @ColumnWidth(12)
    private String role;
    
    @ExcelProperty("状态")
    @ColumnWidth(8)
    private String status;
}

