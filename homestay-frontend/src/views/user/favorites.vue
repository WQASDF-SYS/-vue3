<template>
  <div class="favorites-page">
    <h2>我的收藏</h2>
    <div class="favorites-grid" v-loading="loading">
      <div v-for="item in list" :key="item.id" class="card favorite-card" @click="$router.push(`/homestay/${item.homestayId}`)">
        <div class="card-image">
          <img :src="item.homestay?.coverImage || '/placeholder.jpg'" />
        </div>
        <div class="card-body">
          <h4>{{ item.homestay?.name }}</h4>
          <p class="price">¥{{ item.homestay?.price }}/晚</p>
        </div>
      </div>
    </div>
    <el-empty v-if="!loading && list.length === 0" description="暂无收藏" />
    <div class="pagination-container" v-if="total > 12">
      <el-pagination v-model:current-page="pageNum" :total="total" layout="prev, pager, next" @current-change="fetchData" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMyFavorites } from '@/api/user'

const loading = ref(false)
const list = ref([])
const total = ref(0)
const pageNum = ref(1)

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getMyFavorites({ pageNum: pageNum.value, pageSize: 12 })
    list.value = res.data.records
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

onMounted(() => fetchData())
</script>

<style lang="scss" scoped>
h2 { margin-bottom: 24px; font-size: 20px; }

.favorites-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
}

.favorite-card {
  cursor: pointer;
  
  .card-image {
    height: 120px;
    overflow: hidden;
    
    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }
  }
  
  .card-body {
    padding: 12px;
    
    h4 {
      font-size: 14px;
      margin-bottom: 8px;
    }
    
    .price {
      color: #ef4444;
      font-weight: 600;
    }
  }
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 24px;
}
</style>

