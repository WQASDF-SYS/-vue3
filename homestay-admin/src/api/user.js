import request from '@/utils/request'

// 获取当前用户信息
export function getUserInfo() {
  return request.get('/user/info')
}

// 分页查询用户
export function getUsers(params) {
  return request.get('/user/admin/list', { params })
}

// 创建用户
export function createUser(data) {
  return request.post('/user/admin', data)
}

// 更新用户
export function updateUser(id, data) {
  return request.put(`/user/admin/${id}`, data)
}

// 更新用户状态
export function updateUserStatus(id, status) {
  return request.put(`/user/admin/${id}/status`, null, { params: { status } })
}

// 删除用户
export function deleteUser(id) {
  return request.delete(`/user/admin/${id}`)
}

