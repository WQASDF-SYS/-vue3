package com.homestay;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 曲靖民宿管理系统启动类
 * 
 * @author Homestay
 */
@SpringBootApplication
@MapperScan("com.homestay.mapper")
public class HomestayApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(HomestayApplication.class, args);
        System.out.println("========================================");
        System.out.println("  曲靖民宿管理系统启动成功！");
        System.out.println("  接口地址: http://localhost:8080");
        System.out.println("========================================");
    }
}

