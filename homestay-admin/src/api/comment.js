import request from '@/utils/request'

// 分页查询评论
export function getComments(params) {
  return request.get('/comment/admin/list', { params })
}

// 审核评论
export function auditComment(id, status) {
  return request.put(`/comment/admin/${id}/audit`, null, { params: { status } })
}

// 删除评论
export function deleteComment(id) {
  return request.delete(`/comment/admin/${id}`)
}

