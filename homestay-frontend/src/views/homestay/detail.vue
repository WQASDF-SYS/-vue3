<template>
  <div class="homestay-detail-page" v-loading="loading">
    <div class="container" v-if="detail">
      <!-- 头部信息 -->
      <div class="detail-header">
        <div class="header-left">
          <h1 class="title">{{ detail.name }}</h1>
          <div class="meta">
            <el-tag>{{ detail.categoryName }}</el-tag>
            <span><el-icon><Location /></el-icon>{{ detail.address }}</span>
            <span><el-icon><View /></el-icon>{{ detail.viewCount }}次浏览</span>
          </div>
        </div>
        <div class="header-right">
          <el-button 
            :type="isFavorite ? 'danger' : 'default'" 
            @click="handleFavorite"
          >
            <el-icon><Star /></el-icon>
            {{ isFavorite ? '已收藏' : '收藏' }}
          </el-button>
        </div>
      </div>
      
      <!-- 图片展示 -->
      <div class="detail-images">
        <el-image 
          :src="detail.coverImage" 
          fit="cover" 
          class="main-image"
          :preview-src-list="imageList"
        />
      </div>
      
      <div class="detail-content">
        <div class="content-left">
          <!-- 基本信息 -->
          <div class="info-card">
            <h3>房源信息</h3>
            <div class="info-grid">
              <div class="info-item">
                <el-icon size="24"><House /></el-icon>
                <div>
                  <p class="label">房间数量</p>
                  <p class="value">{{ detail.roomCount }}间</p>
                </div>
              </div>
              <div class="info-item">
                <el-icon size="24"><User /></el-icon>
                <div>
                  <p class="label">最大入住</p>
                  <p class="value">{{ detail.maxGuests }}人</p>
                </div>
              </div>
              <div class="info-item">
                <el-icon size="24"><Phone /></el-icon>
                <div>
                  <p class="label">联系电话</p>
                  <p class="value">{{ detail.contactPhone || '暂无' }}</p>
                </div>
              </div>
            </div>
          </div>
          
          <!-- 详细描述 -->
          <div class="info-card">
            <h3>详细描述</h3>
            <p class="description">{{ detail.description || '暂无详细描述' }}</p>
          </div>
          
          <!-- 评论区 -->
          <div class="info-card">
            <h3>用户评价 ({{ comments.length }})</h3>
            
            <!-- 发表评论表单 -->
            <div class="comment-form" v-if="userStore.token">
              <div class="form-header">
                <el-avatar :size="40">{{ userStore.userInfo?.nickname?.charAt(0) || '用' }}</el-avatar>
                <span class="form-title">发表评价</span>
              </div>
              <div class="form-content">
                <div class="rating-row">
                  <span class="rating-label">评分：</span>
                  <el-rate v-model="commentForm.rating" :colors="['#99A9BF', '#F7BA2A', '#FF9900']" />
                  <span class="rating-text">{{ ratingText }}</span>
                </div>
                <el-input 
                  v-model="commentForm.content" 
                  type="textarea" 
                  :rows="3" 
                  placeholder="分享您的入住体验吧~"
                  maxlength="500"
                  show-word-limit
                />
                <div class="form-footer">
                  <el-button type="primary" @click="submitComment" :loading="commentSubmitting">
                    发表评论
                  </el-button>
                </div>
              </div>
            </div>
            <div class="login-tip" v-else>
              <el-icon><InfoFilled /></el-icon>
              <span>请先 <router-link to="/login">登录</router-link> 后发表评论</span>
            </div>
            
            <el-divider v-if="comments.length" />
            
            <div class="comments-list" v-if="comments.length">
              <div v-for="comment in comments" :key="comment.id" class="comment-item">
                <el-avatar :size="40">{{ comment.userName?.charAt(0) }}</el-avatar>
                <div class="comment-content">
                  <div class="comment-header">
                    <span class="username">{{ comment.userName }}</span>
                    <el-rate v-model="comment.rating" disabled size="small" />
                  </div>
                  <p class="comment-text">{{ comment.content }}</p>
                  <span class="comment-time">{{ comment.createTime }}</span>
                </div>
              </div>
            </div>
            <el-empty v-else description="暂无评价，快来抢沙发吧~" :image-size="80" />
          </div>
        </div>
        
        <!-- 预订卡片 -->
        <div class="content-right">
          <div class="booking-card">
            <div class="price-section">
              <span class="price">
                <span class="currency">¥</span>{{ detail.price }}
              </span>
              <span class="unit">/晚</span>
            </div>
            
            <el-form :model="orderForm" label-position="top">
              <el-form-item label="入住日期">
                <el-date-picker 
                  v-model="orderForm.checkInDate" 
                  type="date"
                  placeholder="选择入住日期"
                  :disabled-date="disabledDate"
                  style="width: 100%"
                />
              </el-form-item>
              <el-form-item label="退房日期">
                <el-date-picker 
                  v-model="orderForm.checkOutDate" 
                  type="date"
                  placeholder="选择退房日期"
                  :disabled-date="disabledCheckOut"
                  style="width: 100%"
                />
              </el-form-item>
              <el-form-item label="入住人数">
                <el-input-number 
                  v-model="orderForm.guests" 
                  :min="1" 
                  :max="detail.maxGuests"
                  style="width: 100%"
                />
              </el-form-item>
              <el-form-item label="入住人姓名">
                <el-input v-model="orderForm.guestName" placeholder="请输入姓名" />
              </el-form-item>
              <el-form-item label="联系电话">
                <el-input v-model="orderForm.guestPhone" placeholder="请输入电话" />
              </el-form-item>
            </el-form>
            
            <div class="total-price" v-if="totalDays > 0">
              <span>共 {{ totalDays }} 晚</span>
              <span class="total">¥{{ totalPrice.toFixed(2) }}</span>
            </div>
            
            <el-button type="primary" size="large" class="btn-book" @click="handleBook">
              立即预订
            </el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { InfoFilled } from '@element-plus/icons-vue'
import dayjs from 'dayjs'
import { getHomestayDetail, getHomestayComments } from '@/api/front'
import { toggleFavorite, checkFavorite, createOrder, addComment } from '@/api/user'
import { useUserStore } from '@/stores/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const loading = ref(true)
const detail = ref(null)
const comments = ref([])
const isFavorite = ref(false)
const commentSubmitting = ref(false)

const commentForm = reactive({
  rating: 5,
  content: ''
})

const ratingText = computed(() => {
  const texts = ['', '非常差', '差', '一般', '好', '非常好']
  return texts[commentForm.rating] || ''
})

const orderForm = reactive({
  checkInDate: null,
  checkOutDate: null,
  guests: 1,
  guestName: '',
  guestPhone: ''
})

const imageList = computed(() => {
  if (!detail.value) return []
  const list = [detail.value.coverImage]
  if (detail.value.images) {
    try {
      const images = JSON.parse(detail.value.images)
      list.push(...images)
    } catch {}
  }
  return list.filter(Boolean)
})

const totalDays = computed(() => {
  if (!orderForm.checkInDate || !orderForm.checkOutDate) return 0
  return dayjs(orderForm.checkOutDate).diff(dayjs(orderForm.checkInDate), 'day')
})

const totalPrice = computed(() => {
  if (!detail.value) return 0
  return totalDays.value * detail.value.price
})

const disabledDate = (date) => {
  return date < new Date(new Date().setHours(0, 0, 0, 0))
}

const disabledCheckOut = (date) => {
  if (!orderForm.checkInDate) return disabledDate(date)
  return date <= orderForm.checkInDate
}

const fetchDetail = async () => {
  loading.value = true
  try {
    const res = await getHomestayDetail(route.params.id)
    detail.value = res.data
    
    // 获取评论
    const commentRes = await getHomestayComments(route.params.id, { pageNum: 1, pageSize: 10 })
    comments.value = commentRes.data.records
    
    // 检查是否收藏
    if (userStore.token) {
      const favRes = await checkFavorite(route.params.id)
      isFavorite.value = favRes.data.isFavorite
    }
  } finally {
    loading.value = false
  }
}

const handleFavorite = async () => {
  if (!userStore.token) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  
  const res = await toggleFavorite(route.params.id)
  isFavorite.value = res.data.isFavorite
  ElMessage.success(res.message)
}

const submitComment = async () => {
  if (!commentForm.content.trim()) {
    ElMessage.warning('请输入评论内容')
    return
  }
  if (commentForm.rating === 0) {
    ElMessage.warning('请选择评分')
    return
  }
  
  commentSubmitting.value = true
  try {
    await addComment({
      homestayId: Number(route.params.id),
      rating: commentForm.rating,
      content: commentForm.content.trim()
    })
    ElMessage.success('评论发表成功！')
    // 重置表单
    commentForm.rating = 5
    commentForm.content = ''
    // 重新加载评论列表
    const commentRes = await getHomestayComments(route.params.id, { pageNum: 1, pageSize: 10 })
    comments.value = commentRes.data.records
  } catch (error) {
    console.error('评论失败:', error)
  } finally {
    commentSubmitting.value = false
  }
}

const handleBook = async () => {
  if (!userStore.token) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  
  if (!orderForm.checkInDate || !orderForm.checkOutDate) {
    ElMessage.warning('请选择入住和退房日期')
    return
  }
  if (!orderForm.guestName || !orderForm.guestPhone) {
    ElMessage.warning('请填写入住人信息')
    return
  }
  
  try {
    await createOrder({
      homestayId: Number(route.params.id),
      checkInDate: dayjs(orderForm.checkInDate).format('YYYY-MM-DD'),
      checkOutDate: dayjs(orderForm.checkOutDate).format('YYYY-MM-DD'),
      guests: orderForm.guests,
      guestName: orderForm.guestName,
      guestPhone: orderForm.guestPhone
    })
    ElMessage.success('预订成功！')
    router.push('/user/orders')
  } catch (error) {
    console.error('预订失败:', error)
  }
}

onMounted(() => {
  fetchDetail()
})
</script>

<style lang="scss" scoped>
.homestay-detail-page {
  padding: 40px 0;
  min-height: calc(100vh - 200px);
}

.detail-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 24px;
  
  .title {
    font-size: 28px;
    font-weight: 600;
    margin-bottom: 12px;
  }
  
  .meta {
    display: flex;
    align-items: center;
    gap: 16px;
    color: var(--text-secondary);
    
    span {
      display: flex;
      align-items: center;
      gap: 4px;
    }
  }
}

.detail-images {
  margin-bottom: 32px;
  
  .main-image {
    width: 100%;
    height: 400px;
    border-radius: 12px;
    overflow: hidden;
  }
}

.detail-content {
  display: grid;
  grid-template-columns: 1fr 360px;
  gap: 32px;
}

.info-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
  
  h3 {
    font-size: 18px;
    margin-bottom: 20px;
    padding-bottom: 12px;
    border-bottom: 1px solid var(--border-color);
  }
  
  .info-grid {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 24px;
  }
  
  .info-item {
    display: flex;
    align-items: center;
    gap: 12px;
    
    .label {
      font-size: 13px;
      color: var(--text-secondary);
    }
    
    .value {
      font-weight: 600;
    }
  }
  
  .description {
    line-height: 1.8;
    color: var(--text-secondary);
  }
}

.comment-form {
  margin-bottom: 20px;
  padding: 16px;
  background: #f9fafb;
  border-radius: 8px;
  
  .form-header {
    display: flex;
    align-items: center;
    gap: 12px;
    margin-bottom: 16px;
    
    .form-title {
      font-weight: 500;
      color: var(--text-color);
    }
  }
  
  .form-content {
    .rating-row {
      display: flex;
      align-items: center;
      gap: 12px;
      margin-bottom: 12px;
      
      .rating-label {
        color: var(--text-secondary);
      }
      
      .rating-text {
        color: #f7ba2a;
        font-size: 14px;
      }
    }
    
    .form-footer {
      display: flex;
      justify-content: flex-end;
      margin-top: 12px;
    }
  }
}

.login-tip {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 16px;
  background: #fef3cd;
  border-radius: 8px;
  color: #856404;
  margin-bottom: 16px;
  
  a {
    color: var(--primary-color);
    font-weight: 500;
    
    &:hover {
      text-decoration: underline;
    }
  }
}

.comments-list {
  .comment-item {
    display: flex;
    gap: 16px;
    padding: 16px 0;
    border-bottom: 1px solid var(--border-color);
    
    &:last-child {
      border-bottom: none;
    }
    
    .comment-content {
      flex: 1;
    }
    
    .comment-header {
      display: flex;
      align-items: center;
      gap: 12px;
      margin-bottom: 8px;
      
      .username {
        font-weight: 500;
      }
    }
    
    .comment-text {
      color: var(--text-secondary);
      margin-bottom: 8px;
    }
    
    .comment-time {
      font-size: 12px;
      color: #9ca3af;
    }
  }
}

.booking-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  position: sticky;
  top: 90px;
  
  .price-section {
    margin-bottom: 24px;
    
    .price {
      font-size: 32px;
      font-weight: 600;
      color: #ef4444;
      
      .currency {
        font-size: 18px;
      }
    }
    
    .unit {
      color: var(--text-secondary);
    }
  }
  
  .total-price {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16px 0;
    border-top: 1px solid var(--border-color);
    margin-top: 16px;
    
    .total {
      font-size: 24px;
      font-weight: 600;
      color: #ef4444;
    }
  }
  
  .btn-book {
    width: 100%;
    margin-top: 16px;
    height: 48px;
    font-size: 16px;
  }
}

@media (max-width: 1024px) {
  .detail-content {
    grid-template-columns: 1fr;
  }
  
  .booking-card {
    position: static;
  }
}
</style>

