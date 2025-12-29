<template>
  <div class="layout">
    <header class="header">
      <div class="container header-inner">
        <router-link to="/" class="logo">
          <el-icon size="28"><House /></el-icon>
          <span>曲靖民宿</span>
        </router-link>
        
        <nav class="nav">
          <router-link to="/" class="nav-link" :class="{ active: route.path === '/' }">首页</router-link>
          <router-link to="/homestay" class="nav-link" :class="{ active: route.path.startsWith('/homestay') }">民宿</router-link>
          <router-link to="/announcement" class="nav-link" :class="{ active: route.path.startsWith('/announcement') }">公告</router-link>
        </nav>
        
        <div class="header-right">
          <template v-if="userStore.token">
            <el-dropdown trigger="click" @command="handleCommand">
              <div class="user-info">
                <el-avatar :size="32" :src="userStore.userInfo.avatar || userStore.userInfo.avatarUrl">
                  {{ (userStore.userInfo.nickName || userStore.userInfo.username || 'U').charAt(0) }}
                </el-avatar>
                <span class="username">{{ userStore.userInfo.nickName || userStore.userInfo.username || '用户' }}</span>
                <el-icon><ArrowDown /></el-icon>
              </div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="profile">
                    <el-icon><User /></el-icon>个人中心
                  </el-dropdown-item>
                  <el-dropdown-item command="orders">
                    <el-icon><List /></el-icon>我的订单
                  </el-dropdown-item>
                  <el-dropdown-item command="favorites">
                    <el-icon><Star /></el-icon>我的收藏
                  </el-dropdown-item>
                  <el-dropdown-item divided command="logout">
                    <el-icon><SwitchButton /></el-icon>退出登录
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          <template v-else>
            <router-link to="/login" class="btn-login">登录</router-link>
            <router-link to="/register" class="btn-register">注册</router-link>
          </template>
        </div>
      </div>
    </header>
    
    <main class="main">
      <router-view />
    </main>
    
    <footer class="footer">
      <div class="container">
        <div class="footer-content">
          <div class="footer-logo">
            <el-icon size="32"><House /></el-icon>
            <span>曲靖民宿</span>
          </div>
          <p class="footer-desc">发现最美的住处，体验不一样的旅行</p>
          <p class="copyright">© 2024 曲靖民宿管理系统 | 课程设计项目</p>
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ArrowDown, User, List, Star, SwitchButton } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const handleCommand = (command) => {
  if (command === 'logout') {
    ElMessageBox.confirm('确定要退出登录吗？', '提示', { type: 'warning' })
      .then(() => {
        userStore.logout()
        router.push('/')
        ElMessage.success('已退出登录')
      })
      .catch(() => {})
  } else if (command === 'profile') {
    router.push('/user/profile')
  } else if (command === 'orders') {
    router.push('/user/orders')
  } else if (command === 'favorites') {
    router.push('/user/favorites')
  }
}
</script>

<style lang="scss" scoped>
.layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.header {
  height: 70px;
  background: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  position: sticky;
  top: 0;
  z-index: 100;
  
  .header-inner {
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
  
  .logo {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 20px;
    font-weight: 600;
    color: var(--primary-color);
    text-decoration: none;
  }
  
  .nav {
    display: flex;
    gap: 32px;
    
    .nav-link {
      color: var(--text-color);
      text-decoration: none;
      font-weight: 500;
      padding: 8px 0;
      position: relative;
      
      &::after {
        content: '';
        position: absolute;
        bottom: 0;
        left: 0;
        width: 0;
        height: 2px;
        background: var(--primary-color);
        transition: width 0.3s;
      }
      
      &:hover, &.active {
        color: var(--primary-color);
        
        &::after {
          width: 100%;
        }
      }
    }
  }
  
  .header-right {
    display: flex;
    align-items: center;
    gap: 12px;
    
    .btn-login, .btn-register {
      padding: 8px 20px;
      border-radius: 20px;
      text-decoration: none;
      font-weight: 500;
      transition: all 0.3s;
    }
    
    .btn-login {
      color: var(--primary-color);
      border: 1px solid var(--primary-color);
      
      &:hover {
        background: rgba(16, 185, 129, 0.1);
      }
    }
    
    .btn-register {
      color: white;
      background: var(--primary-color);
      
      &:hover {
        background: var(--primary-dark);
      }
    }
    
    .user-info {
      display: flex;
      align-items: center;
      gap: 8px;
      cursor: pointer;
      padding: 6px 12px;
      border-radius: 20px;
      transition: background 0.3s;
      
      &:hover {
        background: #f5f5f5;
      }
      
      .username {
        font-weight: 500;
        max-width: 80px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
      
      .el-icon {
        font-size: 12px;
        color: #909399;
      }
    }
  }
}

.main {
  flex: 1;
}

.footer {
  background: #1f2937;
  color: #9ca3af;
  padding: 40px 0;
  
  .footer-content {
    text-align: center;
  }
  
  .footer-logo {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
    font-size: 20px;
    font-weight: 600;
    color: white;
    margin-bottom: 12px;
  }
  
  .footer-desc {
    margin-bottom: 16px;
  }
  
  .copyright {
    font-size: 14px;
  }
}

@media (max-width: 768px) {
  .nav {
    display: none !important;
  }
}
</style>

