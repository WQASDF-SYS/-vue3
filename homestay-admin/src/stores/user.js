import { defineStore } from 'pinia'
import { ref } from 'vue'
import { login as loginApi, getUserInfo } from '@/api/auth'

// 管理端使用独立的存储键，避免与用户端冲突
const STORAGE_KEY_TOKEN = 'admin_token'
const STORAGE_KEY_USER = 'admin_userInfo'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem(STORAGE_KEY_TOKEN) || '')
  const userInfo = ref(JSON.parse(localStorage.getItem(STORAGE_KEY_USER) || '{}'))
  
  // 登录
  const login = async (loginData) => {
    const res = await loginApi(loginData)
    token.value = res.data.token
    userInfo.value = {
      id: res.data.userId,
      username: res.data.username,
      nickName: res.data.nickName,
      avatar: res.data.avatar,
      role: res.data.role
    }
    localStorage.setItem(STORAGE_KEY_TOKEN, token.value)
    localStorage.setItem(STORAGE_KEY_USER, JSON.stringify(userInfo.value))
    return userInfo.value  // 返回用户信息（包含role）
  }
  
  // 获取用户信息
  const fetchUserInfo = async () => {
    try {
      const res = await getUserInfo()
      userInfo.value = res.data
      localStorage.setItem(STORAGE_KEY_USER, JSON.stringify(userInfo.value))
    } catch (e) {
      logout()
    }
  }
  
  // 退出登录
  const logout = () => {
    token.value = ''
    userInfo.value = {}
    localStorage.removeItem(STORAGE_KEY_TOKEN)
    localStorage.removeItem(STORAGE_KEY_USER)
  }
  
  return {
    token,
    userInfo,
    login,
    fetchUserInfo,
    logout
  }
})

