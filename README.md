# 曲靖民宿管理系统

基于 Spring Boot 3 + Vue 3 的民宿管理系统，包含前台用户端和后台管理端。

## 项目结构

```
├── homestay-backend/      # Spring Boot 后端
├── homestay-admin/        # Vue3 后台管理端 (端口 3001)
├── homestay-frontend/     # Vue3 前台用户端 (端口 3000)
└── README.md
```

## 技术栈

### 后端
- Spring Boot 3.2
- MyBatis Plus 3.5
- MySQL 8.0
- JWT 认证
- EasyExcel
- 智谱AI API

### 前端
- Vue 3.4
- Vue Router 4
- Pinia
- Element Plus
- Axios
- ECharts

## 快速开始

### 1. 数据库配置

```bash
# 创建数据库并导入数据
mysql -u root -p < homestay-backend/sql/homestay.sql
```

### 2. 后端启动

```bash
cd homestay-backend

# 修改数据库配置 (src/main/resources/application.yml)
# 修改 spring.datasource.username 和 password

# 使用 Maven 启动
mvn spring-boot:run

# 或打包后运行
mvn clean package
java -jar target/homestay-backend-1.0.0.jar
```

后端服务地址: http://localhost:8080

### 3. 后台管理端启动

```bash
cd homestay-admin
npm install
npm run dev
```

后台管理地址: http://localhost:3001
- 默认账号: admin
- 默认密码: admin123

### 4. 前台用户端启动

```bash
cd homestay-frontend
npm install
npm run dev
```

前台地址: http://localhost:3000

## 功能模块

### 前台用户端
- 首页展示（轮播图、推荐民宿、公告）
- 民宿列表（分类筛选、搜索）
- 民宿详情（预订、收藏、评论）
- 用户注册/登录
- 个人中心（订单、收藏、评论）
- AI智能客服

### 后台管理端
- 首页仪表盘（数据统计、图表）
- 用户管理（CRUD、状态管理）
- 民宿管理（CRUD、上架/下架、推荐）
- 订单管理（审核、状态变更）
- 分类管理
- 轮播图管理
- 公告管理
- 评论管理

## API 接口

### 公开接口（无需登录）
- POST /api/auth/login - 登录
- POST /api/auth/register - 注册
- GET /api/front/* - 前台数据接口

### 用户接口（需登录）
- GET /api/user/info - 获取用户信息
- PUT /api/user/info - 更新用户信息
- POST /api/order - 创建订单
- GET /api/order/my - 我的订单
- POST /api/favorite/toggle/{id} - 收藏/取消
- POST /api/comment - 发表评论
- POST /api/ai/chat - AI对话

### 管理接口（需管理员权限）
- /api/user/admin/* - 用户管理
- /api/homestay/admin/* - 民宿管理
- /api/order/admin/* - 订单管理
- /api/category/admin/* - 分类管理
- /api/carousel/admin/* - 轮播图管理
- /api/announcement/admin/* - 公告管理
- /api/comment/admin/* - 评论管理

## 数据库表

1. sys_user - 用户表
2. homestay_category - 分类表
3. homestay - 民宿表
4. homestay_order - 订单表
5. user_favorite - 收藏表
6. homestay_comment - 评论表
7. carousel - 轮播图表
8. announcement - 公告表
9. sys_config - 系统配置表

## 注意事项

1. 确保 MySQL 服务已启动
2. 修改 `application.yml` 中的数据库连接信息
3. 智谱AI API Key 已配置在 `application.yml` 中
4. 上传文件默认保存在 `./uploads` 目录

## 开发者

滇池学院 - 计算机科学与技术专业
课程设计项目 2025

