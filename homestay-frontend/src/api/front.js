import request from '@/utils/request'

// 获取首页数据
export function getHomeData() {
  return request.get('/front/home')
}

// 获取轮播图
export function getCarousels() {
  return request.get('/front/carousel')
}

// 获取分类列表
export function getCategories() {
  return request.get('/front/category')
}

// 分页查询民宿
export function getHomestays(params) {
  return request.get('/front/homestay/list', { params })
}

// 获取民宿详情
export function getHomestayDetail(id) {
  return request.get(`/front/homestay/${id}`)
}

// 获取民宿评论
export function getHomestayComments(id, params) {
  return request.get(`/front/homestay/${id}/comments`, { params })
}

// 获取公告列表
export function getAnnouncements(params) {
  return request.get('/front/announcement/list', { params })
}

// 获取公告详情
export function getAnnouncementDetail(id) {
  return request.get(`/front/announcement/${id}`)
}

