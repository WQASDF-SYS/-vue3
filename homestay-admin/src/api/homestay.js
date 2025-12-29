import request from '@/utils/request'

// 分页查询民宿
export function getHomestays(params) {
  return request.get('/homestay/list', { params })
}

// 获取民宿详情
export function getHomestay(id) {
  return request.get(`/homestay/${id}`)
}

// 创建民宿
export function createHomestay(data) {
  return request.post('/homestay/admin', data)
}

// 更新民宿
export function updateHomestay(id, data) {
  return request.put(`/homestay/admin/${id}`, data)
}

// 更新民宿状态
export function updateHomestayStatus(id, status) {
  return request.put(`/homestay/admin/${id}/status`, null, { params: { status } })
}

// 更新推荐状态
export function updateHomestayRecommend(id, isRecommend) {
  return request.put(`/homestay/admin/${id}/recommend`, null, { params: { isRecommend } })
}

// 删除民宿
export function deleteHomestay(id) {
  return request.delete(`/homestay/admin/${id}`)
}

