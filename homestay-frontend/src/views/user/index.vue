<template>
  <div class="user-center">
    <div class="container">
      <div class="user-layout">
        <aside class="user-sidebar">
          <div class="user-info">
            <el-avatar :size="64" :src="userStore.userInfo.avatarUrl">
              {{ userStore.userInfo.nickName?.charAt(0) || 'U' }}
            </el-avatar>
            <h3>{{ userStore.userInfo.nickName || '用户' }}</h3>
            <p>{{ userStore.userInfo.userName }}</p>
          </div>
          <el-menu :default-active="$route.path" router>
            <el-menu-item index="/user/profile">
              <el-icon><User /></el-icon>个人信息
            </el-menu-item>
            <el-menu-item index="/user/orders">
              <el-icon><List /></el-icon>我的订单
            </el-menu-item>
            <el-menu-item index="/user/favorites">
              <el-icon><Star /></el-icon>我的收藏
            </el-menu-item>
            <el-menu-item index="/user/comments">
              <el-icon><ChatDotRound /></el-icon>我的评论
            </el-menu-item>
          </el-menu>
        </aside>
        <main class="user-main">
          <router-view />
        </main>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useUserStore } from '@/stores/user'
const userStore = useUserStore()
</script>

<style lang="scss" scoped>
.user-center {
  padding: 40px 0;
  min-height: calc(100vh - 200px);
}

.user-layout {
  display: grid;
  grid-template-columns: 240px 1fr;
  gap: 24px;
}

.user-sidebar {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  height: fit-content;
  
  .user-info {
    padding: 32px;
    text-align: center;
    background: linear-gradient(135deg, var(--primary-color), var(--primary-dark));
    color: white;
    
    h3 {
      margin-top: 12px;
      font-size: 18px;
    }
    
    p {
      margin-top: 4px;
      opacity: 0.8;
      font-size: 14px;
    }
  }
  
  .el-menu {
    border-right: none;
  }
}

.user-main {
  background: white;
  border-radius: 12px;
  padding: 24px;
  min-height: 500px;
}

@media (max-width: 768px) {
  .user-layout {
    grid-template-columns: 1fr;
  }
}
</style>

