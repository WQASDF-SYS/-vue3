-- =============================================
-- 曲靖民宿管理系统数据库脚本
-- 数据库: homestay_db
-- =============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS homestay_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE homestay_db;

-- =============================================
-- 1. 用户表 (sys_user)
-- =============================================
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `user_name` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(100) NOT NULL COMMENT '密码(BCrypt加密)',
    `phone_number` VARCHAR(11) DEFAULT NULL COMMENT '手机号码',
    `nick_name` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    `avatar_url` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    `real_name` VARCHAR(50) DEFAULT NULL COMMENT '真实姓名',
    `gender` TINYINT DEFAULT 0 COMMENT '性别(0-未知 1-男 2-女)',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `role` TINYINT NOT NULL DEFAULT 0 COMMENT '角色(0-普通用户 1-管理员 2-超级管理员)',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态(0-禁用 1-启用)',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除(0-否 1-是)',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_name` (`user_name`),
    KEY `idx_phone` (`phone_number`),
    KEY `idx_role` (`role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 插入默认管理员账号 (密码: admin123)
-- 插入测试普通用户账号 (密码: user123)
INSERT INTO `sys_user` (`user_name`, `password`, `nick_name`, `role`, `status`) VALUES 
('admin', '$2a$10$AZ57aGElZbyHFR.lbeoLSe26LE3qlFRgxfpNw6g2Myfm1CyIwuL1q', '超级管理员', 2, 1),
('user', '$2a$10$Mbv1XJ3FXI3NMd4/VSi42O2MHV2Dyk4wLyBVo675kNe0IOL7ErF5y', '测试用户', 0, 1);

-- =============================================
-- 2. 民宿分类表 (homestay_category)
-- =============================================
DROP TABLE IF EXISTS `homestay_category`;
CREATE TABLE `homestay_category` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '分类ID',
    `name` VARCHAR(50) NOT NULL COMMENT '分类名称',
    `icon` VARCHAR(255) DEFAULT NULL COMMENT '分类图标',
    `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序号',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态(0-禁用 1-启用)',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除(0-否 1-是)',
    PRIMARY KEY (`id`),
    KEY `idx_sort` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='民宿分类表';

-- 插入默认分类
INSERT INTO `homestay_category` (`name`, `sort_order`) VALUES 
('精品民宿', 1),
('特色客栈', 2),
('度假别墅', 3),
('农家乐', 4),
('青年旅舍', 5);

-- =============================================
-- 3. 民宿表 (homestay)
-- =============================================
DROP TABLE IF EXISTS `homestay`;
CREATE TABLE `homestay` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '民宿ID',
    `name` VARCHAR(100) NOT NULL COMMENT '民宿名称',
    `category_id` BIGINT NOT NULL COMMENT '分类ID',
    `cover_image` VARCHAR(255) DEFAULT NULL COMMENT '封面图片',
    `images` TEXT DEFAULT NULL COMMENT '图片列表(JSON数组)',
    `description` TEXT DEFAULT NULL COMMENT '详细描述',
    `price` DECIMAL(10,2) NOT NULL COMMENT '每晚价格',
    `address` VARCHAR(255) NOT NULL COMMENT '详细地址',
    `location` VARCHAR(100) DEFAULT NULL COMMENT '位置/区域',
    `latitude` DECIMAL(10,7) DEFAULT NULL COMMENT '纬度',
    `longitude` DECIMAL(10,7) DEFAULT NULL COMMENT '经度',
    `room_count` INT NOT NULL DEFAULT 1 COMMENT '房间数量',
    `max_guests` INT NOT NULL DEFAULT 2 COMMENT '最大入住人数',
    `facilities` VARCHAR(500) DEFAULT NULL COMMENT '设施(JSON数组)',
    `contact_phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    `view_count` INT NOT NULL DEFAULT 0 COMMENT '浏览次数',
    `is_recommend` TINYINT NOT NULL DEFAULT 0 COMMENT '是否推荐(0-否 1-是)',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态(0-下架 1-上架)',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除(0-否 1-是)',
    PRIMARY KEY (`id`),
    KEY `idx_category` (`category_id`),
    KEY `idx_price` (`price`),
    KEY `idx_recommend` (`is_recommend`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='民宿表';

-- =============================================
-- 4. 订单表 (homestay_order)
-- =============================================
DROP TABLE IF EXISTS `homestay_order`;
CREATE TABLE `homestay_order` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `order_no` VARCHAR(32) NOT NULL COMMENT '订单编号',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `homestay_id` BIGINT NOT NULL COMMENT '民宿ID',
    `homestay_name` VARCHAR(100) NOT NULL COMMENT '民宿名称(冗余)',
    `check_in_date` DATE NOT NULL COMMENT '入住日期',
    `check_out_date` DATE NOT NULL COMMENT '退房日期',
    `days` INT NOT NULL COMMENT '入住天数',
    `guests` INT NOT NULL DEFAULT 1 COMMENT '入住人数',
    `unit_price` DECIMAL(10,2) NOT NULL COMMENT '单价',
    `total_price` DECIMAL(10,2) NOT NULL COMMENT '总价',
    `guest_name` VARCHAR(50) DEFAULT NULL COMMENT '入住人姓名',
    `guest_phone` VARCHAR(20) DEFAULT NULL COMMENT '入住人电话',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态(0-待审核 1-已确认 2-已入住 3-已完成 4-已取消)',
    `pay_status` TINYINT NOT NULL DEFAULT 0 COMMENT '支付状态(0-未支付 1-已支付 2-已退款)',
    `cancel_reason` VARCHAR(255) DEFAULT NULL COMMENT '取消原因',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除(0-否 1-是)',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_user` (`user_id`),
    KEY `idx_homestay` (`homestay_id`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单表';

-- =============================================
-- 5. 收藏表 (user_favorite)
-- =============================================
DROP TABLE IF EXISTS `user_favorite`;
CREATE TABLE `user_favorite` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `homestay_id` BIGINT NOT NULL COMMENT '民宿ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_homestay` (`user_id`, `homestay_id`),
    KEY `idx_user` (`user_id`),
    KEY `idx_homestay` (`homestay_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户收藏表';

-- =============================================
-- 6. 评论表 (homestay_comment)
-- =============================================
DROP TABLE IF EXISTS `homestay_comment`;
CREATE TABLE `homestay_comment` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '评论ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `homestay_id` BIGINT NOT NULL COMMENT '民宿ID',
    `order_id` BIGINT DEFAULT NULL COMMENT '关联订单ID',
    `content` TEXT NOT NULL COMMENT '评论内容',
    `rating` TINYINT NOT NULL DEFAULT 5 COMMENT '评分(1-5)',
    `images` TEXT DEFAULT NULL COMMENT '评论图片(JSON数组)',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态(0-待审核 1-已通过 2-已拒绝)',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除(0-否 1-是)',
    PRIMARY KEY (`id`),
    KEY `idx_user` (`user_id`),
    KEY `idx_homestay` (`homestay_id`),
    KEY `idx_order` (`order_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评论表';

-- =============================================
-- 7. 轮播图表 (carousel)
-- =============================================
DROP TABLE IF EXISTS `carousel`;
CREATE TABLE `carousel` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `title` VARCHAR(100) DEFAULT NULL COMMENT '标题',
    `image_url` VARCHAR(255) NOT NULL COMMENT '图片URL',
    `link_url` VARCHAR(255) DEFAULT NULL COMMENT '跳转链接',
    `link_type` TINYINT NOT NULL DEFAULT 0 COMMENT '链接类型(0-无 1-民宿详情 2-外部链接)',
    `target_id` BIGINT DEFAULT NULL COMMENT '目标ID(民宿ID等)',
    `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序号',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态(0-禁用 1-启用)',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除(0-否 1-是)',
    PRIMARY KEY (`id`),
    KEY `idx_sort` (`sort_order`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='轮播图表';

-- =============================================
-- 8. 公告表 (announcement)
-- =============================================
DROP TABLE IF EXISTS `announcement`;
CREATE TABLE `announcement` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `title` VARCHAR(200) NOT NULL COMMENT '标题',
    `content` TEXT NOT NULL COMMENT '内容',
    `type` TINYINT NOT NULL DEFAULT 0 COMMENT '类型(0-公告 1-资讯)',
    `cover_image` VARCHAR(255) DEFAULT NULL COMMENT '封面图片',
    `is_top` TINYINT NOT NULL DEFAULT 0 COMMENT '是否置顶(0-否 1-是)',
    `view_count` INT NOT NULL DEFAULT 0 COMMENT '浏览次数',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态(0-隐藏 1-显示)',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除(0-否 1-是)',
    PRIMARY KEY (`id`),
    KEY `idx_type` (`type`),
    KEY `idx_top` (`is_top`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='公告/资讯表';

-- =============================================
-- 9. 系统配置表 (sys_config)
-- =============================================
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `config_key` VARCHAR(100) NOT NULL COMMENT '配置键',
    `config_value` TEXT NOT NULL COMMENT '配置值',
    `config_name` VARCHAR(100) DEFAULT NULL COMMENT '配置名称',
    `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统配置表';

-- 插入AI配置
INSERT INTO `sys_config` (`config_key`, `config_value`, `config_name`, `remark`) VALUES 
('ai_enabled', 'true', 'AI功能开关', '是否启用AI智能客服'),
('ai_api_key', '', 'AI API Key', '智谱AI API密钥'),
('ai_model', 'glm-4-flash', 'AI模型', '使用的AI模型'),
('ai_welcome_message', '您好！我是曲靖民宿智能客服，有什么可以帮您的吗？', 'AI欢迎语', '智能客服欢迎语');

