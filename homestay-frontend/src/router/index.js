import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

const routes = [
  {
    path: '/',
    component: () => import('@/layout/index.vue'),
    children: [
      {
        path: '',
        name: 'Home',
        component: () => import('@/views/home/index.vue'),
        meta: { title: '首页' }
      },
      {
        path: 'homestay',
        name: 'HomestayList',
        component: () => import('@/views/homestay/list.vue'),
        meta: { title: '民宿列表' }
      },
      {
        path: 'homestay/:id',
        name: 'HomestayDetail',
        component: () => import('@/views/homestay/detail.vue'),
        meta: { title: '民宿详情' }
      },
      {
        path: 'announcement',
        name: 'AnnouncementList',
        component: () => import('@/views/announcement/list.vue'),
        meta: { title: '公告资讯' }
      },
      {
        path: 'announcement/:id',
        name: 'AnnouncementDetail',
        component: () => import('@/views/announcement/detail.vue'),
        meta: { title: '公告详情' }
      },
      {
        path: 'user',
        name: 'UserCenter',
        component: () => import('@/views/user/index.vue'),
        meta: { title: '个人中心', requireAuth: true },
        children: [
          {
            path: '',
            redirect: '/user/profile'
          },
          {
            path: 'profile',
            name: 'UserProfile',
            component: () => import('@/views/user/profile.vue'),
            meta: { title: '个人信息' }
          },
          {
            path: 'orders',
            name: 'UserOrders',
            component: () => import('@/views/user/orders.vue'),
            meta: { title: '我的订单' }
          },
          {
            path: 'favorites',
            name: 'UserFavorites',
            component: () => import('@/views/user/favorites.vue'),
            meta: { title: '我的收藏' }
          },
          {
            path: 'comments',
            name: 'UserComments',
            component: () => import('@/views/user/comments.vue'),
            meta: { title: '我的评论' }
          }
        ]
      }
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/login/register.vue'),
    meta: { title: '注册' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior() {
    return { top: 0 }
  }
})

router.beforeEach((to, from, next) => {
  document.title = to.meta.title ? `${to.meta.title} - 曲靖民宿` : '曲靖民宿'
  
  const userStore = useUserStore()
  if (to.meta.requireAuth && !userStore.token) {
    next({ path: '/login', query: { redirect: to.fullPath } })
    return
  }
  
  next()
})

export default router

