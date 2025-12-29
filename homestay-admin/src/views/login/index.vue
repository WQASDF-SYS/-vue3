<template>
  <div class="login-container">
    <div class="login-bg"></div>
    <div class="login-card">
      <div class="login-header">
        <el-icon size="40" color="#667eea"><House /></el-icon>
        <h1>曲靖民宿管理系统</h1>
        <p>管理员与用户统一登录入口</p>
      </div>
      
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        size="large"
        @submit.prevent="handleLogin"
      >
        <el-form-item prop="username">
          <el-input
            v-model="form.username"
            placeholder="请输入用户名"
            prefix-icon="User"
            clearable
          />
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input
            v-model="form.password"
            type="password"
            placeholder="请输入密码"
            prefix-icon="Lock"
            show-password
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        
        <el-form-item>
          <el-button
            type="primary"
            :loading="loading"
            class="login-btn"
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
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { getUserInfo } from '@/api/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const formRef = ref()
const loading = ref(false)

const form = reactive({
  username: '',
  password: ''
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

// 检查URL中是否有token（从用户端跳转过来的管理员）
onMounted(async () => {
  const token = route.query.token
  if (token) {
    try {
      // 保存token并获取用户信息
      userStore.token = token
      localStorage.setItem('admin_token', token)
      const res = await getUserInfo()
      userStore.userInfo = res.data
      localStorage.setItem('admin_userInfo', JSON.stringify(res.data))
      
      // 检查是否是管理员
      if (res.data.role >= 1) {
        ElMessage.success('登录成功')
        router.replace('/dashboard')
      } else {
        // 不是管理员，清除token并提示
        userStore.logout()
        ElMessage.warning('您不是管理员，请使用管理员账号登录')
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
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    
    loading.value = true
    try {
      const result = await userStore.login(form)
      
      // 根据角色判断跳转
      // role: 0-普通用户 1-管理员 2-超级管理员
      if (result.role >= 1) {
        ElMessage.success('管理员登录成功')
        router.push('/dashboard')
      } else {
        ElMessage.warning('普通用户请使用用户端登录')
        // 普通用户跳转到用户前台，传递token
        const token = userStore.token
        userStore.logout() // 清除本地token
        window.location.href = `http://localhost:3000/login?token=${encodeURIComponent(token)}`
      }
    } catch (error) {
      console.error('登录失败:', error)
    } finally {
      loading.value = false
    }
  })
}
</script>

<style lang="scss" scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-bg {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: url("data:image/svg+xml,%3Csvg width='60' height='60' viewBox='0 0 60 60' xmlns='http://www.w3.org/2000/svg'%3E%3Cg fill='none' fill-rule='evenodd'%3E%3Cg fill='%23ffffff' fill-opacity='0.08'%3E%3Cpath d='M36 34v-4h-2v4h-4v2h4v4h2v-4h4v-2h-4zm0-30V0h-2v4h-4v2h4v4h2V6h4V4h-4zM6 34v-4H4v4H0v2h4v4h2v-4h4v-2H6zM6 4V0H4v4H0v2h4v4h2V6h4V4H6z'/%3E%3C/g%3E%3C/g%3E%3C/svg%3E");
}

.login-card {
  position: relative;
  width: 420px;
  padding: 40px;
  background: #fff;
  border-radius: 20px;
  box-shadow: 0 25px 80px rgba(0, 0, 0, 0.25);
}

.login-header {
  text-align: center;
  margin-bottom: 36px;
  
  h1 {
    font-size: 24px;
    color: #303133;
    margin: 16px 0 8px;
    font-weight: 600;
  }
  
  p {
    color: #909399;
    font-size: 14px;
  }
}

.login-btn {
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
</style>
