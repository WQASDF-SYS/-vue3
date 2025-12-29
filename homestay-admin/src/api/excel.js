import request from '@/utils/request'

const BASE_URL = import.meta.env.VITE_API_URL || ''

// ==================== 用户相关 ====================

// 导出用户数据
export function exportUsers(params) {
  return `${BASE_URL}/api/excel/user/export?${new URLSearchParams(params).toString()}`
}

// 导入用户数据
export function importUsers(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request.post('/excel/user/import', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

// 下载用户导入模板
export function userTemplateUrl() {
  return `${BASE_URL}/api/excel/user/template`
}

// ==================== 民宿相关 ====================

// 导出民宿数据
export function exportHomestays(params) {
  return `${BASE_URL}/api/excel/homestay/export?${new URLSearchParams(params).toString()}`
}

// 导入民宿数据
export function importHomestays(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request.post('/excel/homestay/import', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

// 下载民宿导入模板
export function homestayTemplateUrl() {
  return `${BASE_URL}/api/excel/homestay/template`
}

// ==================== 订单相关 ====================

// 导出订单数据
export function exportOrders(params) {
  return `${BASE_URL}/api/excel/order/export?${new URLSearchParams(params).toString()}`
}

