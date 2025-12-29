import request from '@/utils/request'

// 获取所有轮播图
export function getCarousels() {
  return request.get('/carousel/admin/list')
}

// 创建轮播图
export function createCarousel(data) {
  return request.post('/carousel/admin', data)
}

// 更新轮播图
export function updateCarousel(id, data) {
  return request.put(`/carousel/admin/${id}`, data)
}

// 更新轮播图状态
export function updateCarouselStatus(id, status) {
  return request.put(`/carousel/admin/${id}/status`, null, { params: { status } })
}

// 更新排序
export function updateCarouselSort(id, sortOrder) {
  return request.put(`/carousel/admin/${id}/sort`, null, { params: { sortOrder } })
}

// 删除轮播图
export function deleteCarousel(id) {
  return request.delete(`/carousel/admin/${id}`)
}

