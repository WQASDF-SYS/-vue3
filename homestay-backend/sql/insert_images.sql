-- =============================================
-- 插入AI生成的民宿主题图片数据
-- 执行时间: 2024-12-29
-- =============================================

USE homestay_db;

-- =============================================
-- 1. 插入轮播图
-- =============================================
-- 先清空旧数据（可选）
-- DELETE FROM carousel WHERE deleted = 0;

INSERT INTO `carousel` (`title`, `image_url`, `link_type`, `sort_order`, `status`) VALUES 
('曲靖传统庭院民宿', '/uploads/images/20251229/carousel_1.jpg', 0, 1, 1),
('山景木屋民宿', '/uploads/images/20251229/carousel_2.jpg', 0, 2, 1),
('现代简约民宿', '/uploads/images/20251229/carousel_3.jpg', 0, 3, 1);

-- =============================================
-- 2. 插入民宿数据
-- =============================================
INSERT INTO `homestay` (
    `name`, `category_id`, `cover_image`, `images`, `description`, 
    `price`, `address`, `location`, `room_count`, `max_guests`,
    `facilities`, `contact_phone`, `is_recommend`, `status`
) VALUES 
-- 传统白族风格民宿
(
    '云岭白韵庭院',
    1,  -- 精品民宿
    '/uploads/images/20251229/homestay_traditional.jpg',
    '["/uploads/images/20251229/homestay_traditional.jpg", "/uploads/images/20251229/room_bedroom.jpg", "/uploads/images/20251229/room_living.jpg"]',
    '位于曲靖市中心的传统白族风格民宿，青瓦白墙，古色古香。庭院中繁花似锦，是体验云南民族文化的绝佳选择。民宿提供地道的云南早餐，让您感受最正宗的云南风情。',
    388.00,
    '曲靖市麒麟区文化路88号',
    '麒麟区',
    3,
    6,
    '["WiFi", "空调", "停车场", "早餐", "洗衣机", "24小时热水"]',
    '13888888001',
    1,
    1
),
-- 现代精品民宿
(
    '悦享精品公寓',
    1,  -- 精品民宿
    '/uploads/images/20251229/homestay_modern.jpg',
    '["/uploads/images/20251229/homestay_modern.jpg", "/uploads/images/20251229/room_bedroom.jpg", "/uploads/images/20251229/room_bathroom.jpg"]',
    '现代简约风格的精品公寓，位于市中心商业区，交通便利。房间宽敞明亮，配备高端家电和舒适床品，是商务出行和城市旅游的理想选择。',
    268.00,
    '曲靖市麒麟区南宁东路168号',
    '麒麟区',
    2,
    4,
    '["WiFi", "空调", "智能门锁", "投影仪", "厨房", "洗衣机"]',
    '13888888002',
    1,
    1
),
-- 山景度假别墅
(
    '云端山居别墅',
    3,  -- 度假别墅
    '/uploads/images/20251229/homestay_mountain.jpg',
    '["/uploads/images/20251229/homestay_mountain.jpg", "/uploads/images/20251229/room_living.jpg", "/uploads/images/20251229/room_bedroom.jpg"]',
    '坐落于曲靖山间的豪华度假别墅，拥有无边泳池和绝美山景。日出云海，日落晚霞，让您在大自然中放松身心。适合家庭度假和团队聚会。',
    1288.00,
    '曲靖市马龙区云景山庄16号',
    '马龙区',
    5,
    12,
    '["WiFi", "空调", "停车场", "泳池", "BBQ", "KTV", "棋牌室", "早餐"]',
    '13888888003',
    1,
    1
),
-- 花园民宿
(
    '竹韵雅居',
    2,  -- 特色客栈
    '/uploads/images/20251229/homestay_garden.jpg',
    '["/uploads/images/20251229/homestay_garden.jpg", "/uploads/images/20251229/room_bedroom.jpg", "/uploads/images/20251229/room_living.jpg"]',
    '隐于闹市的静谧之所，竹林环绕，鸟语花香。传统中式建筑配以精心打造的园林景观，让您体验诗情画意的慢生活。提供茶艺体验和中式早餐。',
    458.00,
    '曲靖市沾益区珠江源大道66号',
    '沾益区',
    4,
    8,
    '["WiFi", "空调", "停车场", "茶室", "花园", "早餐", "管家服务"]',
    '13888888004',
    1,
    1
),
-- 湖景民宿
(
    '临湖小筑',
    2,  -- 特色客栈
    '/uploads/images/20251229/homestay_lakeside.jpg',
    '["/uploads/images/20251229/homestay_lakeside.jpg", "/uploads/images/20251229/room_bedroom.jpg", "/uploads/images/20251229/room_bathroom.jpg"]',
    '临湖而建的木屋民宿，推窗即见碧波荡漾。清晨可观湖面晨雾，傍晚可赏湖畔落日。配备私人码头，可体验划船、垂钓等水上活动。',
    528.00,
    '曲靖市陆良县南盘江景区内',
    '陆良县',
    3,
    6,
    '["WiFi", "空调", "停车场", "私人码头", "垂钓", "划船", "早餐"]',
    '13888888005',
    1,
    1
);

-- =============================================
-- 查看插入结果
-- =============================================
SELECT '轮播图' as 类型, COUNT(*) as 数量 FROM carousel WHERE deleted = 0
UNION ALL
SELECT '民宿' as 类型, COUNT(*) as 数量 FROM homestay WHERE deleted = 0;

