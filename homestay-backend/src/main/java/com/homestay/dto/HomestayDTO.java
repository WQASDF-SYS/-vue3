package com.homestay.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 民宿DTO
 */
@Data
public class HomestayDTO {
    
    private Long id;
    
    @NotBlank(message = "民宿名称不能为空")
    private String name;
    
    @NotNull(message = "分类不能为空")
    private Long categoryId;
    
    private String coverImage;
    
    private String images;
    
    private String description;
    
    @NotNull(message = "价格不能为空")
    @DecimalMin(value = "0.01", message = "价格必须大于0")
    private BigDecimal price;
    
    @NotBlank(message = "地址不能为空")
    private String address;
    
    private String location;
    
    private BigDecimal latitude;
    
    private BigDecimal longitude;
    
    @Min(value = 1, message = "房间数量至少为1")
    private Integer roomCount;
    
    @Min(value = 1, message = "最大入住人数至少为1")
    private Integer maxGuests;
    
    private String facilities;
    
    private String contactPhone;
    
    private Integer isRecommend;
    
    private Integer status;
}

