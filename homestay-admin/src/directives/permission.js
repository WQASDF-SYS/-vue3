/**
 * 按钮级权限控制指令
 */
import { useUserStore } from '@/stores/user'

/**
 * 权限配置
 * 根据角色定义可访问的按钮权限
 * role: 0-普通用户, 1-管理员, 2-超级管理员
 */
const permissionConfig = {
  // 用户管理权限
  'user:add': [1, 2],      // 添加用户
  'user:edit': [1, 2],     // 编辑用户
  'user:delete': [2],      // 删除用户（仅超级管理员）
  'user:export': [1, 2],   // 导出用户
  'user:import': [2],      // 导入用户（仅超级管理员）
  
  // 民宿管理权限
  'homestay:add': [1, 2],
  'homestay:edit': [1, 2],
  'homestay:delete': [2],
  'homestay:export': [1, 2],
  'homestay:import': [1, 2],
  
  // 订单管理权限
  'order:audit': [1, 2],   // 审核订单
  'order:delete': [2],     // 删除订单
  'order:export': [1, 2],  // 导出订单
  
  // 内容管理权限
  'carousel:add': [1, 2],
  'carousel:edit': [1, 2],
  'carousel:delete': [2],
  
  'announcement:add': [1, 2],
  'announcement:edit': [1, 2],
  'announcement:delete': [2],
  
  'category:add': [1, 2],
  'category:edit': [1, 2],
  'category:delete': [2],
  
  // 系统管理权限（仅超级管理员）
  'system:config': [2],
}

/**
 * 检查是否有权限
 */
export function hasPermission(permission) {
  const userStore = useUserStore()
  const userRole = userStore.userInfo?.role
  
  // 超级管理员拥有所有权限
  if (userRole === 2) return true
  
  // 检查具体权限
  const allowedRoles = permissionConfig[permission]
  if (!allowedRoles) return true // 未配置的权限默认允许
  
  return allowedRoles.includes(userRole)
}

/**
 * v-permission 指令
 * 用法: v-permission="'user:add'"
 */
export const permission = {
  mounted(el, binding) {
    const value = binding.value
    if (value && !hasPermission(value)) {
      el.parentNode?.removeChild(el)
    }
  }
}

/**
 * 注册指令
 */
export function setupPermissionDirective(app) {
  app.directive('permission', permission)
}

export default permission

