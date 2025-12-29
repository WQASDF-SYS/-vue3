<template>
  <div class="login-page">
    <div class="login-card">
      <div class="login-header">
        <div class="logo">
          <el-icon size="40"><House /></el-icon>
          <span>曲靖民宿</span>
        </div>
        <h2>欢迎登录</h2>
        <p>管理员与用户统一入口</p>
      </div>
      
      <el-form ref="formRef" :model="form" :rules="rules" @submit.prevent="handleLogin">
        <el-form-item prop="username">
          <el-input 
            v-model="form.username" 
            placeholder="请输入用户名" 
            prefix-icon="User" 
            size="large"
            clearable
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input 
            v-model="form.password" 
            type="password" 
            placeholder="请输入密码" 
            prefix-icon="Lock"
            size="large"
            show-password
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        <el-form-item>
          <el-button 
            type="primary" 
            size="large" 
            :loading="loading" 
            class="btn-login" 
            @click="handleLogin"
          >
            {{ loading ? '登录中...' : '登 录' }}
          </el-button>
        </el-form-item>
      </el-form>
      
      <!-- 快捷登录 -->
      <div class="quick-login">
        <p class="quick-title">快捷登录</p>
        <div class="quick-btns">
          <el-button @click="quickLogin('admin')" size="small" type="danger" plain>
            管理员登录
          </el-button>
          <el-button @click="quickLogin('user')" size="small" type="success" plain>
            用户登录
          </el-button>
        </div>
      </div>
      
      <!-- 账号信息 -->
      <div class="account-info">
        <el-divider>测试账号</el-divider>
        <div class="info-row">
          <el-tag type="danger" size="small">管理员</el-tag>
          <span>admin / admin123</span>
        </div>
        <div class="info-row">
          <el-tag type="success" size="small">用户</el-tag>
          <span>user / user123</span>
        </div>
      </div>
      
      <div class="login-footer">
        <span>还没有账号？</span>
        <router-link to="/register">立即注册</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { getUserInfo } from '@/api/auth'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const formRef = ref()
const loading = ref(false)

const form = reactive({ username: '', password: '' })
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

// 检查URL中是否有token（从管理端跳转过来的普通用户）
onMounted(async () => {
  const token = route.query.token
  if (token) {
    try {
      // 保存token并获取用户信息
      userStore.token = token
      localStorage.setItem('frontend_token', token)
      const res = await getUserInfo()
      userStore.userInfo = res.data
      localStorage.setItem('frontend_userInfo', JSON.stringify(res.data))
      
      // 检查是否是普通用户
      if (res.data.role === 0) {
        ElMessage.success('登录成功')
        router.replace('/')
      } else {
        // 是管理员，清除token并提示
        userStore.logout()
        ElMessage.warning('管理员请使用管理后台登录')
      }
    } catch (error) {
      console.error('Token验证失败:', error)
      userStore.logout()
    }
  }
})

// 快捷登录
const quickLogin = (type) => {
  if (type === 'admin') {
    form.username = 'admin'
    form.password = 'admin123'
  } else {
    form.username = 'user'
    form.password = 'user123'
  }
  handleLogin()
}

const handleLogin = async () => {
  await formRef.value?.validate(async (valid) => {
    if (!valid) return
    loading.value = true
    try {
      const result = await userStore.login(form)
      
      // 根据角色跳转不同页面
      // role: 0-普通用户 1-管理员 2-超级管理员
      if (result.role >= 1) {
        ElMessage.success('管理员登录成功，正在跳转到管理后台...')
        // 管理员跳转到管理后台，传递token
        const token = userStore.token
        const adminUrl = import.meta.env.VITE_ADMIN_URL || 'http://localhost:3001'
        window.location.href = `${adminUrl}/login?token=${encodeURIComponent(token)}`
      } else {
        ElMessage.success('登录成功')
        // 普通用户跳转到用户首页
        const redirect = route.query.redirect || '/'
        router.push(redirect)
      }
    } finally {
      loading.value = false
    }
  })
}
</script>

<style lang="scss" scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  position: relative;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: url("data:image/svg+xml,%3Csvg width='60' height='60' viewBox='0 0 60 60' xmlns='http://www.w3.org/2000/svg'%3E%3Cg fill='none' fill-rule='evenodd'%3E%3Cg fill='%23ffffff' fill-opacity='0.08'%3E%3Cpath d='M36 34v-4h-2v4h-4v2h4v4h2v-4h4v-2h-4zm0-30V0h-2v4h-4v2h4v4h2V6h4V4h-4zM6 34v-4H4v4H0v2h4v4h2v-4h4v-2H6zM6 4V0H4v4H0v2h4v4h2V6h4V4H6z'/%3E%3C/g%3E%3C/g%3E%3C/svg%3E");
  }
}

.login-card {
  position: relative;
  width: 420px;
  padding: 40px;
  background: white;
  border-radius: 20px;
  box-shadow: 0 25px 80px rgba(0, 0, 0, 0.25);
}

.login-header {
  text-align: center;
  margin-bottom: 32px;
  
  .logo {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 10px;
    color: #667eea;
    font-size: 24px;
    font-weight: 700;
    margin-bottom: 20px;
  }
  
  h2 {
    font-size: 26px;
    margin-bottom: 8px;
    color: #303133;
  }
  
  p {
    color: #909399;
    font-size: 14px;
  }
}

.btn-login {
  width: 100%;
  height: 44px;
  font-size: 16px;
}

.quick-login {
  margin-top: 20px;
  
  .quick-title {
    text-align: center;
    color: #909399;
    font-size: 13px;
    margin-bottom: 12px;
  }
  
  .quick-btns {
    display: flex;
    gap: 12px;
    justify-content: center;
    
    .el-button {
      flex: 1;
    }
  }
}

.account-info {
  margin-top: 20px;
  
  .el-divider {
    margin: 16px 0;
  }
  
  .info-row {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 10px;
    margin-bottom: 8px;
    font-size: 13px;
    color: #606266;
    
    .el-tag {
      min-width: 50px;
      text-align: center;
    }
  }
}

.login-footer {
  text-align: center;
  margin-top: 24px;
  color: #909399;
  font-size: 14px;
  
  a {
    color: #667eea;
    text-decoration: none;
    font-weight: 500;
    margin-left: 4px;
    
    &:hover {
      text-decoration: underline;
    }
  }
}
</style>
