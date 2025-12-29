<template>
  <div class="home-page">
    <!-- 轮播图 -->
    <section class="hero-section">
      <el-carousel height="500px" :interval="5000">
        <el-carousel-item v-for="item in carousels" :key="item.id">
          <div 
            class="carousel-item" 
            :style="{ backgroundImage: `url(${item.imageUrl})` }"
            @click="handleCarouselClick(item)"
          >
            <div class="carousel-content">
              <h1>{{ item.title || '发现曲靖最美民宿' }}</h1>
              <p>体验不一样的住宿，感受家的温暖</p>
              <router-link to="/homestay" class="btn-explore">开始探索</router-link>
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>
    </section>
    
    <!-- 分类导航 -->
    <section class="category-section section">
      <div class="container">
        <h2 class="section-title">民宿分类</h2>
        <div class="category-grid">
          <div 
            v-for="cat in categories" 
            :key="cat.id" 
            class="category-item"
            @click="goToList(cat.id)"
          >
            <el-icon size="36"><House /></el-icon>
            <span>{{ cat.name }}</span>
          </div>
        </div>
      </div>
    </section>
    
    <!-- 推荐民宿 -->
    <section class="recommend-section section" v-if="recommendList.length">
      <div class="container">
        <h2 class="section-title">精选推荐</h2>
        <div class="homestay-grid">
          <div 
            v-for="item in recommendList" 
            :key="item.id" 
            class="card homestay-card"
            @click="goToDetail(item.id)"
          >
            <div class="card-image">
              <img :src="item.coverImage || '/placeholder.jpg'" :alt="item.name" />
              <span class="tag tag-hot">推荐</span>
            </div>
            <div class="card-body">
              <h3 class="card-title">{{ item.name }}</h3>
              <p class="card-location">
                <el-icon><Location /></el-icon>
                {{ item.address }}
              </p>
              <div class="card-footer">
                <span class="price">
                  <span class="currency">¥</span>{{ item.price }}
                  <span class="unit">/晚</span>
                </span>
                <span class="views">
                  <el-icon><View /></el-icon>{{ item.viewCount }}
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
    
    <!-- 最新民宿 -->
    <section class="latest-section section" v-if="latestList.length">
      <div class="container">
        <h2 class="section-title">最新上线</h2>
        <div class="homestay-grid">
          <div 
            v-for="item in latestList" 
            :key="item.id" 
            class="card homestay-card"
            @click="goToDetail(item.id)"
          >
            <div class="card-image">
              <img :src="item.coverImage || '/placeholder.jpg'" :alt="item.name" />
            </div>
            <div class="card-body">
              <h3 class="card-title">{{ item.name }}</h3>
              <p class="card-location">
                <el-icon><Location /></el-icon>
                {{ item.address }}
              </p>
              <div class="card-footer">
                <span class="price">
                  <span class="currency">¥</span>{{ item.price }}
                  <span class="unit">/晚</span>
                </span>
                <span class="views">
                  <el-icon><View /></el-icon>{{ item.viewCount }}
                </span>
              </div>
            </div>
          </div>
        </div>
        <div class="view-more">
          <router-link to="/homestay" class="btn-primary">查看更多民宿</router-link>
        </div>
      </div>
    </section>
    
    <!-- 公告区域 -->
    <section class="announcement-section section" v-if="announcements.length">
      <div class="container">
        <h2 class="section-title">最新公告</h2>
        <div class="announcement-list">
          <div 
            v-for="item in announcements" 
            :key="item.id" 
            class="announcement-item"
            @click="$router.push(`/announcement/${item.id}`)"
          >
            <div class="announcement-title">
              <el-tag v-if="item.isTop" size="small" type="danger">置顶</el-tag>
              <span>{{ item.title }}</span>
            </div>
            <span class="announcement-date">{{ item.createTime?.substring(0, 10) }}</span>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getHomeData } from '@/api/front'

const router = useRouter()

const carousels = ref([])
const categories = ref([])
const recommendList = ref([])
const latestList = ref([])
const announcements = ref([])

const fetchHomeData = async () => {
  try {
    const res = await getHomeData()
    carousels.value = res.data.carousels || []
    categories.value = res.data.categories || []
    recommendList.value = res.data.recommendList || []
    latestList.value = res.data.latestList || []
    announcements.value = res.data.announcements || []
    
    // 如果没有轮播图，添加默认
    if (carousels.value.length === 0) {
      carousels.value = [{ id: 0, imageUrl: '', title: '发现曲靖最美民宿' }]
    }
  } catch (error) {
    console.error('获取首页数据失败:', error)
  }
}

const handleCarouselClick = (item) => {
  if (item.linkType === 1 && item.targetId) {
    router.push(`/homestay/${item.targetId}`)
  } else if (item.linkType === 2 && item.linkUrl) {
    window.open(item.linkUrl)
  }
}

const goToList = (categoryId) => {
  router.push({ path: '/homestay', query: { categoryId } })
}

const goToDetail = (id) => {
  router.push(`/homestay/${id}`)
}

onMounted(() => {
  fetchHomeData()
})
</script>

<style lang="scss" scoped>
.hero-section {
  .carousel-item {
    height: 100%;
    background-size: cover;
    background-position: center;
    background-color: #374151;
    position: relative;
    cursor: pointer;
    
    &::before {
      content: '';
      position: absolute;
      inset: 0;
      background: linear-gradient(to bottom, rgba(0,0,0,0.2), rgba(0,0,0,0.5));
    }
  }
  
  .carousel-content {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    text-align: center;
    color: white;
    
    h1 {
      font-size: 42px;
      font-weight: 700;
      margin-bottom: 16px;
      text-shadow: 0 2px 4px rgba(0,0,0,0.3);
    }
    
    p {
      font-size: 18px;
      margin-bottom: 32px;
      opacity: 0.9;
    }
    
    .btn-explore {
      display: inline-block;
      padding: 14px 36px;
      background: var(--primary-color);
      color: white;
      text-decoration: none;
      border-radius: 30px;
      font-weight: 500;
      transition: all 0.3s;
      
      &:hover {
        background: var(--primary-dark);
        transform: translateY(-2px);
      }
    }
  }
}

.category-section {
  background: white;
  
  .category-grid {
    display: grid;
    grid-template-columns: repeat(5, 1fr);
    gap: 24px;
  }
  
  .category-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 12px;
    padding: 24px;
    background: var(--bg-color);
    border-radius: 12px;
    cursor: pointer;
    transition: all 0.3s;
    
    &:hover {
      background: rgba(16, 185, 129, 0.1);
      color: var(--primary-color);
      transform: translateY(-4px);
    }
    
    span {
      font-weight: 500;
    }
  }
}

.homestay-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
}

.homestay-card {
  cursor: pointer;
  
  .card-image {
    position: relative;
    height: 200px;
    overflow: hidden;
    
    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
      transition: transform 0.3s;
    }
    
    .tag {
      position: absolute;
      top: 12px;
      left: 12px;
    }
  }
  
  &:hover .card-image img {
    transform: scale(1.05);
  }
  
  .card-body {
    padding: 16px;
  }
  
  .card-title {
    font-size: 16px;
    font-weight: 600;
    margin-bottom: 8px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  
  .card-location {
    display: flex;
    align-items: center;
    gap: 4px;
    font-size: 13px;
    color: var(--text-secondary);
    margin-bottom: 12px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  
  .card-footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    .price {
      font-size: 20px;
    }
    
    .views {
      display: flex;
      align-items: center;
      gap: 4px;
      font-size: 13px;
      color: var(--text-secondary);
    }
  }
}

.view-more {
  text-align: center;
  margin-top: 40px;
}

.announcement-section {
  background: white;
  
  .announcement-list {
    max-width: 800px;
    margin: 0 auto;
  }
  
  .announcement-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16px 0;
    border-bottom: 1px solid var(--border-color);
    cursor: pointer;
    transition: color 0.3s;
    
    &:hover {
      color: var(--primary-color);
    }
    
    .announcement-title {
      display: flex;
      align-items: center;
      gap: 8px;
    }
    
    .announcement-date {
      color: var(--text-secondary);
      font-size: 14px;
    }
  }
}

@media (max-width: 1024px) {
  .homestay-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 768px) {
  .hero-section .carousel-content h1 {
    font-size: 28px;
  }
  
  .category-grid {
    grid-template-columns: repeat(3, 1fr) !important;
  }
  
  .homestay-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 480px) {
  .homestay-grid {
    grid-template-columns: 1fr;
  }
}
</style>

