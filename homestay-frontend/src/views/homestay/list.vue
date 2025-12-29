<template>
  <div class="homestay-list-page">
    <div class="container">
      <!-- 筛选栏 -->
      <div class="filter-bar">
        <div class="filter-categories">
          <span 
            class="filter-item" 
            :class="{ active: !query.categoryId }"
            @click="selectCategory(null)"
          >全部</span>
          <span 
            v-for="cat in categories" 
            :key="cat.id"
            class="filter-item"
            :class="{ active: query.categoryId === cat.id }"
            @click="selectCategory(cat.id)"
          >{{ cat.name }}</span>
        </div>
        <div class="filter-search">
          <el-input 
            v-model="query.keyword" 
            placeholder="搜索民宿名称" 
            prefix-icon="Search"
            clearable
            @keyup.enter="fetchData"
            @clear="fetchData"
          />
          <el-button type="primary" @click="fetchData">搜索</el-button>
        </div>
      </div>
      
      <!-- 民宿列表 -->
      <div class="homestay-grid" v-loading="loading">
        <div 
          v-for="item in list" 
          :key="item.id" 
          class="card homestay-card"
          @click="$router.push(`/homestay/${item.id}`)"
        >
          <div class="card-image">
            <img :src="item.coverImage || '/placeholder.jpg'" :alt="item.name" />
            <span v-if="item.isRecommend" class="tag tag-hot">推荐</span>
          </div>
          <div class="card-body">
            <h3 class="card-title">{{ item.name }}</h3>
            <p class="card-category">
              <el-tag size="small">{{ item.categoryName }}</el-tag>
            </p>
            <p class="card-location">
              <el-icon><Location /></el-icon>
              {{ item.address }}
            </p>
            <div class="card-info">
              <span><el-icon><House /></el-icon>{{ item.roomCount }}间</span>
              <span><el-icon><User /></el-icon>{{ item.maxGuests }}人</span>
            </div>
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
      
      <!-- 空状态 -->
      <el-empty v-if="!loading && list.length === 0" description="暂无民宿数据" />
      
      <!-- 分页 -->
      <div class="pagination-container" v-if="total > 0">
        <el-pagination
          v-model:current-page="query.pageNum"
          v-model:page-size="query.pageSize"
          :total="total"
          :page-sizes="[12, 24, 48]"
          layout="total, sizes, prev, pager, next"
          @size-change="fetchData"
          @current-change="fetchData"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { getHomestays, getCategories } from '@/api/front'

const route = useRoute()

const loading = ref(false)
const categories = ref([])
const list = ref([])
const total = ref(0)

const query = reactive({
  pageNum: 1,
  pageSize: 12,
  categoryId: null,
  keyword: ''
})

const fetchCategories = async () => {
  const res = await getCategories()
  categories.value = res.data
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getHomestays(query)
    list.value = res.data.records
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

const selectCategory = (id) => {
  query.categoryId = id
  query.pageNum = 1
  fetchData()
}

watch(() => route.query.categoryId, (val) => {
  if (val) {
    query.categoryId = Number(val)
  }
}, { immediate: true })

onMounted(() => {
  fetchCategories()
  fetchData()
})
</script>

<style lang="scss" scoped>
.homestay-list-page {
  padding: 40px 0;
  min-height: calc(100vh - 200px);
}

.filter-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32px;
  padding: 20px;
  background: white;
  border-radius: 12px;
  
  .filter-categories {
    display: flex;
    gap: 12px;
    flex-wrap: wrap;
    
    .filter-item {
      padding: 8px 16px;
      border-radius: 20px;
      cursor: pointer;
      transition: all 0.3s;
      
      &:hover, &.active {
        background: var(--primary-color);
        color: white;
      }
    }
  }
  
  .filter-search {
    display: flex;
    gap: 12px;
    
    .el-input {
      width: 240px;
    }
  }
}

.homestay-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
  min-height: 300px;
}

.homestay-card {
  cursor: pointer;
  
  .card-image {
    position: relative;
    height: 180px;
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
  }
  
  .card-category {
    margin-bottom: 8px;
  }
  
  .card-location {
    display: flex;
    align-items: center;
    gap: 4px;
    font-size: 13px;
    color: var(--text-secondary);
    margin-bottom: 8px;
  }
  
  .card-info {
    display: flex;
    gap: 16px;
    font-size: 13px;
    color: var(--text-secondary);
    margin-bottom: 12px;
    
    span {
      display: flex;
      align-items: center;
      gap: 4px;
    }
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

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 40px;
}

@media (max-width: 1024px) {
  .homestay-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 768px) {
  .filter-bar {
    flex-direction: column;
    gap: 16px;
  }
  
  .homestay-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>

