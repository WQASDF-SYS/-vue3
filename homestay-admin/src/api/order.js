import request from '@/utils/request'

// 分页查询订单
export function getOrders(params) {
  return request.get('/order/admin/list', { params })
}

// 获取订单详情
export function getOrder(id) {
  return request.get(`/order/${id}`)
}

// 更新订单状态
export function updateOrderStatus(id, status, reason = '') {
  return request.put(`/order/admin/${id}/status`, null, { 
    params: { status, reason } 
  })
}

// 删除订单
export function deleteOrder(id) {
  return request.delete(`/order/admin/${id}`)
}

// 获取订单统计
export function getOrderStatistics() {
  return request.get('/order/admin/statistics')
}

