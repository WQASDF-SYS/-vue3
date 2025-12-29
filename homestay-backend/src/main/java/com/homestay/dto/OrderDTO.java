package com.homestay.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * 订单DTO
 */
@Data
public class OrderDTO {
    
    @NotNull(message = "民宿ID不能为空")
    private Long homestayId;
    
    @NotNull(message = "入住日期不能为空")
    private LocalDate checkInDate;
    
    @NotNull(message = "退房日期不能为空")
    @Future(message = "退房日期必须是将来的日期")
    private LocalDate checkOutDate;
    
    @Min(value = 1, message = "入住人数至少为1人")
    private Integer guests = 1;
    
    @NotBlank(message = "入住人姓名不能为空")
    private String guestName;
    
    @NotBlank(message = "入住人电话不能为空")
    private String guestPhone;
    
    private String remark;
}

