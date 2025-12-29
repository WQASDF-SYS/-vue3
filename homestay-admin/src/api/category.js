import request from '@/utils/request'

// 获取所有分类
export function getCategories() {
  return request.get('/category/admin/list')
}

// 创建分类
export function createCategory(data) {
  return request.post('/category/admin', data)
}

// 更新分类
export function updateCategory(id, data) {
  return request.put(`/category/admin/${id}`, data)
}

// 更新分类状态
export function updateCategoryStatus(id, status) {
  return request.put(`/category/admin/${id}/status`, null, { params: { status } })
}

// 删除分类
export function deleteCategory(id) {
  return request.delete(`/category/admin/${id}`)
}

