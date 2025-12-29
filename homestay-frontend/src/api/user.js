import request from '@/utils/request'

// 创建订单
export function createOrder(data) {
  return request.post('/order', data)
}

// 获取我的订单
export function getMyOrders(params) {
  return request.get('/order/my', { params })
}

// 取消订单
export function cancelOrder(id, reason) {
  return request.put(`/order/${id}/cancel`, null, { params: { reason } })
}

// 切换收藏
export function toggleFavorite(homestayId) {
  return request.post(`/favorite/toggle/${homestayId}`)
}

// 检查是否收藏
export function checkFavorite(homestayId) {
  return request.get(`/favorite/check/${homestayId}`)
}

// 获取我的收藏
export function getMyFavorites(params) {
  return request.get('/favorite/my', { params })
}

// 发表评论
export function addComment(data) {
  return request.post('/comment', data)
}

// 获取我的评论
export function getMyComments(params) {
  return request.get('/comment/my', { params })
}

// 删除评论
export function deleteComment(id) {
  return request.delete(`/comment/${id}`)
}

