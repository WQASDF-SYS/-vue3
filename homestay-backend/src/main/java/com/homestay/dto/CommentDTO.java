package com.homestay.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 评论DTO
 */
@Data
public class CommentDTO {
    
    @NotNull(message = "民宿ID不能为空")
    private Long homestayId;
    
    private Long orderId;
    
    @NotBlank(message = "评论内容不能为空")
    private String content;
    
    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分最低为1")
    @Max(value = 5, message = "评分最高为5")
    private Integer rating;
    
    private String images;
}

