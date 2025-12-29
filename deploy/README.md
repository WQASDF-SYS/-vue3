# 曲靖民宿管理系统 - 免费部署指南

## 项目结构
```
├── homestay-backend/    # Spring Boot 后端 (端口 8080)
├── homestay-front/      # Vue3 用户前端 (端口 3000)
├── homestay-admin/      # Vue3 管理后台 (端口 3001)
└── homestay-backend/sql/ # 数据库SQL文件
```

## 方案一：Vercel + Railway + PlanetScale（推荐）

### 1. 数据库 - PlanetScale (免费 5GB)

1. 访问 https://planetscale.com 注册账号
2. 创建新数据库，选择离你最近的区域
3. 获取连接信息，导入 `homestay-backend/sql/homestay.sql`

### 2. 后端 - Railway (免费 500小时/月)

1. 访问 https://railway.app 用 GitHub 登录
2. 新建项目 → Deploy from GitHub repo
3. 选择包含后端代码的仓库
4. 设置环境变量：
   ```
   SPRING_DATASOURCE_URL=jdbc:mysql://你的PlanetScale地址/数据库名?sslMode=VERIFY_IDENTITY
   SPRING_DATASOURCE_USERNAME=你的用户名
   SPRING_DATASOURCE_PASSWORD=你的密码
   ```
5. 部署完成后获取域名，如：`https://xxx.railway.app`

### 3. 前端 - Vercel (完全免费)

#### 用户前端
1. 修改 `homestay-front/.env.production`：
   ```
   VITE_API_BASE_URL=https://你的Railway域名
   ```
2. 构建：`npm run build`
3. 访问 https://vercel.com 部署 `dist` 目录

#### 管理后台
1. 修改 `homestay-admin/.env.production`：
   ```
   VITE_API_BASE_URL=https://你的Railway域名
   ```
2. 构建：`npm run build`
3. 部署到 Vercel

---

## 方案二：腾讯云/阿里云学生优惠

如果你是学生，可以申请：
- 阿里云学生机：https://developer.aliyun.com/plan/student
- 腾讯云学生机：https://cloud.tencent.com/act/campus

然后使用 Docker 一键部署整个项目。

---

## 方案三：本地演示（内网穿透）

适合课程答辩临时演示：

### 使用 ngrok（国外，免费）
```bash
# 1. 下载 ngrok: https://ngrok.com/download
# 2. 启动后端穿透
ngrok http 8080

# 3. 获取公网地址，修改前端API地址后重新构建
```

### 使用 cpolar（国内，免费）
```bash
# 1. 下载 cpolar: https://www.cpolar.com/
# 2. 登录并创建隧道
cpolar http 8080
cpolar http 3000
cpolar http 3001
```

---

## Docker 部署（需要有服务器）

如果你有自己的服务器，可以使用 Docker Compose 一键部署：

```bash
docker-compose up -d
```

---

## 常见问题

### Q: 为什么部署后图片不显示？
A: 图片存储在本地 `uploads/` 目录，部署到云端后需要使用对象存储（如阿里云OSS、七牛云）。

### Q: 跨域问题怎么解决？
A: 后端已配置CORS，确保前端的 `VITE_API_BASE_URL` 指向正确的后端地址。

### Q: 数据库连接失败？
A: 检查云数据库的白名单设置，确保允许你的服务器IP访问。

