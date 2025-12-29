package com.homestay.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一响应结果
 * 
 * @param <T> 数据类型
 */
@Data
public class Result<T> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /** 状态码 */
    private Integer code;
    
    /** 消息 */
    private String message;
    
    /** 数据 */
    private T data;
    
    /** 时间戳 */
    private Long timestamp;
    
    public Result() {
        this.timestamp = System.currentTimeMillis();
    }
    
    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }
    
    /**
     * 成功
     */
    public static Result<Void> success() {
        return new Result<>(200, "操作成功", null);
    }
    
    /**
     * 成功（带消息）
     */
    public static Result<Void> success(String message) {
        return new Result<>(200, message, null);
    }
    
    /**
     * 成功（带数据）
     */
    public static <T> Result<T> ok(T data) {
        return new Result<>(200, "操作成功", data);
    }
    
    /**
     * 成功（带消息和数据）
     */
    public static <T> Result<T> ok(String message, T data) {
        return new Result<>(200, message, data);
    }
    
    /**
     * 失败
     */
    public static <T> Result<T> error() {
        return new Result<>(500, "操作失败", null);
    }
    
    /**
     * 失败（带消息）
     */
    public static <T> Result<T> error(String message) {
        return new Result<>(500, message, null);
    }
    
    /**
     * 失败（带状态码和消息）
     */
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message, null);
    }
    
    /**
     * 自定义结果
     */
    public static <T> Result<T> of(Integer code, String message, T data) {
        return new Result<>(code, message, data);
    }
}

