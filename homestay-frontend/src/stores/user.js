import { defineStore } from 'pinia'
import { ref } from 'vue'
import { login as loginApi, register as registerApi, getUserInfo } from '@/api/auth'

// 用户端使用独立的存储键，避免与管理端冲突
const STORAGE_KEY_TOKEN = 'frontend_token'
const STORAGE_KEY_USER = 'frontend_userInfo'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem(STORAGE_KEY_TOKEN) || '')
  const userInfo = ref(JSON.parse(localStorage.getItem(STORAGE_KEY_USER) || '{}'))
  
  const login = async (data) => {
    const res = await loginApi(data)
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
  
  const register = async (data) => {
    await registerApi(data)
  }
  
  const fetchUserInfo = async () => {
    try {
      const res = await getUserInfo()
      userInfo.value = res.data
      localStorage.setItem(STORAGE_KEY_USER, JSON.stringify(userInfo.value))
    } catch {
      logout()
    }
  }
  
  const logout = () => {
    token.value = ''
    userInfo.value = {}
    localStorage.removeItem(STORAGE_KEY_TOKEN)
    localStorage.removeItem(STORAGE_KEY_USER)
  }
  
  return { token, userInfo, login, register, fetchUserInfo, logout }
})

