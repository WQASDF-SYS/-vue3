-- =============================================
-- 曲靖民宿管理系统 - TiDB Cloud 初始化脚本
-- =============================================

-- 使用 homestay 数据库
USE homestay;

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
    UNIQUE KEY `uk_user_name` (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 默认账号: admin/admin123, user/user123
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
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='民宿分类表';

INSERT INTO `homestay_category` (`name`, `icon`, `sort_order`) VALUES 
('精品民宿', 'https://images.unsplash.com/photo-1566073771259-6a8506099945?w=200', 1),
('度假别墅', 'https://images.unsplash.com/photo-1613490493576-7fde63acd811?w=200', 2),
('农家乐', 'https://images.unsplash.com/photo-1499793983690-e29da59ef1c2?w=200', 3),
('青年旅舍', 'https://images.unsplash.com/photo-1555854877-bab0e564b8d5?w=200', 4),
('特色客栈', 'https://images.unsplash.com/photo-1582719508461-905c673771fd?w=200', 5);

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
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='民宿表';

-- 插入民宿数据
INSERT INTO `homestay` (`name`, `category_id`, `cover_image`, `description`, `price`, `address`, `room_count`, `max_guests`, `facilities`, `contact_phone`, `view_count`, `is_recommend`) VALUES 
('云端山居', 1, 'https://images.unsplash.com/photo-1566073771259-6a8506099945?w=800', '位于曲靖市区的精品民宿，环境优雅，设施齐全，是您出行的理想选择。', 388.00, '曲靖市麒麟区珠江源大道88号', 5, 2, '["WiFi","空调","停车场","早餐"]', '13800138001', 1263, 1),
('古镇客栈', 5, 'https://images.unsplash.com/photo-1582719508461-905c673771fd?w=800', '坐落于会泽古城内，体验传统与现代的完美融合。', 268.00, '曲靖市会泽县古城街道18号', 8, 2, '["WiFi","空调","茶室"]', '13800138002', 894, 1),
('田园度假屋', 2, 'https://images.unsplash.com/photo-1613490493576-7fde63acd811?w=800', '远离城市喧嚣，享受田园生活的宁静与美好。', 1288.00, '曲靖市沾益区花山镇幸福路1号', 3, 8, '["WiFi","空调","停车场","BBQ","游泳池"]', '13800138003', 568, 1),
('罗平油菜花民宿', 1, 'https://images.unsplash.com/photo-1499793983690-e29da59ef1c2?w=800', '每年2-3月油菜花盛开，推窗即见金色花海。', 328.00, '曲靖市罗平县金鸡岭景区旁', 6, 2, '["WiFi","空调","观景阳台"]', '13800138004', 2342, 1),
('马龙野花沟度假村', 2, 'https://images.unsplash.com/photo-1564013799919-ab600027ffc6?w=800', '依山傍水的度假别墅，享受大自然的馈赠。', 1688.00, '曲靖市马龙区野花沟景区', 2, 5, '["WiFi","空调","停车场","钓鱼","登山"]', '13800138005', 234, 1),
('宣威火腿主题民宿', 5, 'https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?w=800', '以宣威火腿文化为主题的特色民宿，品味地道美食。', 258.00, '曲靖市宣威市火腿美食街28号', 6, 2, '["WiFi","空调","美食体验"]', '13800138006', 567, 1),
('师宗凤凰谷山庄', 3, 'https://images.unsplash.com/photo-1571003123894-1f0594d2b5d9?w=800', '位于凤凰谷景区内，感受原生态的自然风光。', 198.00, '曲靖市师宗县凤凰谷景区内', 10, 4, '["WiFi","农家饭","采摘"]', '13800138007', 447, 0),
('云岭白韵庭院', 1, 'https://images.unsplash.com/photo-1566073771259-6a8506099945?w=800', '白族风格的精品庭院民宿，感受民族文化魅力。', 388.00, '曲靖市麒麟区文化路88号', 3, 6, '["WiFi","空调","庭院","茶艺"]', '13800138008', 0, 1),
('悦享精品公寓', 1, 'https://images.unsplash.com/photo-1502672260266-1c1ef2d93688?w=800', '现代简约风格的精品公寓，商务出行首选。', 268.00, '曲靖市麒麟区南宁东路168号', 2, 4, '["WiFi","空调","洗衣机","厨房"]', '13800138009', 0, 1),
('云端山居别墅', 2, 'https://images.unsplash.com/photo-1613490493576-7fde63acd811?w=800', '山顶独栋别墅，俯瞰整个曲靖城区，视野开阔。', 1288.00, '曲靖市马龙区云景山庄16号', 5, 12, '["WiFi","空调","停车场","KTV","游泳池","健身房"]', '13800138010', 0, 1),
('竹韵雅居', 5, 'https://images.unsplash.com/photo-1582719508461-905c673771fd?w=800', '竹林环绕的特色客栈，清新雅致。', 458.00, '曲靖市沾益区珠江源大道66号', 4, 8, '["WiFi","空调","茶室","棋牌室"]', '13800138011', 0, 1),
('临湖小筑', 5, 'https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?w=800', '临湖而建的小筑，推窗即可欣赏湖光山色。', 528.00, '曲靖市陆良县南盘江景区内', 3, 6, '["WiFi","空调","钓鱼","划船"]', '13800138012', 0, 1);

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
    UNIQUE KEY `uk_order_no` (`order_no`)
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
    UNIQUE KEY `uk_user_homestay` (`user_id`, `homestay_id`)
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
    PRIMARY KEY (`id`)
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
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='轮播图表';

INSERT INTO `carousel` (`title`, `image_url`, `sort_order`) VALUES 
('发现曲靖最美民宿', 'https://images.unsplash.com/photo-1566073771259-6a8506099945?w=1200', 1),
('山间别墅 尽享自然', 'https://images.unsplash.com/photo-1613490493576-7fde63acd811?w=1200', 2),
('特色客栈 古镇风情', 'https://images.unsplash.com/photo-1582719508461-905c673771fd?w=1200', 3);

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
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='公告/资讯表';

INSERT INTO `announcement` (`title`, `content`, `type`, `is_top`, `view_count`) VALUES 
('欢迎使用曲靖民宿管理系统', '亲爱的用户，欢迎来到曲靖民宿管理系统！\n我们致力于为您提供最优质的民宿预订服务，让您的曲靖之旅更加便捷、舒适。\n如有任何问题，请联系客服：400-888-8888', 0, 1, 525),
('罗平油菜花节即将开幕', '一年一度的罗平油菜花节将于2月中旬盛大开幕！\n届时将有：\n- 万亩油菜花海观赏\n- 热气球体验\n- 摄影大赛\n- 民俗文化表演\n\n推荐入住罗平油菜花民宿，尽享花海美景！', 1, 1, 1256),
('2024年春节营业公告', '尊敬的用户：\n2024年春节期间（2月9日-2月17日），本平台正常营业。\n部分民宿可能调整价格，请以实际预订页面显示为准。\n提前预订可享受优惠，祝您新春愉快！', 0, 0, 328),
('会泽古城文化节活动预告', '会泽古城文化节将于3月举办，届时将有：\n- 传统手工艺展示\n- 古城灯会\n- 地方戏曲表演\n- 特色美食节\n\n入住古镇客栈，感受历史与现代的交融！', 1, 0, 445),
('新用户注册送优惠券', '即日起至本月底，新用户注册即可获得：\n- 50元无门槛优惠券 x 1\n- 满200减30优惠券 x 2\n- 满500减80优惠券 x 1\n\n快来注册领取吧！', 0, 0, 892),
('曲靖旅游攻略：三日游推荐路线', 'Day 1：罗平油菜花海\n上午抵达罗平，游览金鸡岭、螺丝田等经典观景点。推荐入住罗平油菜花民宿。\n\nDay 2：九龙瀑布群\n前往九龙瀑布群，感受瀑布的壮观。下午可体验漂流项目。\n\nDay 3：珠江源\n探访珠江源头，了解珠江文化。返程前可游览曲靖市区。', 1, 0, 678);

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

INSERT INTO `sys_config` (`config_key`, `config_value`, `config_name`, `remark`) VALUES 
('ai_enabled', 'true', 'AI功能开关', '是否启用AI智能客服'),
('ai_api_key', '', 'AI API Key', '智谱AI API密钥'),
('ai_model', 'glm-4-flash', 'AI模型', '使用的AI模型'),
('ai_welcome_message', '您好！我是曲靖民宿智能客服，有什么可以帮您的吗？', 'AI欢迎语', '智能客服欢迎语');

