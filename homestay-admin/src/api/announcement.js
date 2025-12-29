import request from '@/utils/request'

// 分页查询公告
export function getAnnouncements(params) {
  return request.get('/announcement/admin/list', { params })
}

// 获取公告详情
export function getAnnouncement(id) {
  return request.get(`/announcement/${id}`)
}

// 创建公告
export function createAnnouncement(data) {
  return request.post('/announcement/admin', data)
}

// 更新公告
export function updateAnnouncement(id, data) {
  return request.put(`/announcement/admin/${id}`, data)
}

// 更新公告状态
export function updateAnnouncementStatus(id, status) {
  return request.put(`/announcement/admin/${id}/status`, null, { params: { status } })
}

// 设置置顶
export function setAnnouncementTop(id, isTop) {
  return request.put(`/announcement/admin/${id}/top`, null, { params: { isTop } })
}

// 删除公告
export function deleteAnnouncement(id) {
  return request.delete(`/announcement/admin/${id}`)
}

