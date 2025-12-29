<template>
  <div class="announcement-list-page">
    <div class="container">
      <h1 class="page-title">公告资讯</h1>
      <el-tabs v-model="activeType" @tab-change="fetchData">
        <el-tab-pane label="全部" name="" />
        <el-tab-pane label="公告" name="0" />
        <el-tab-pane label="资讯" name="1" />
      </el-tabs>
      
      <div class="announcement-list" v-loading="loading">
        <div 
          v-for="item in list" 
          :key="item.id" 
          class="announcement-item"
          @click="$router.push(`/announcement/${item.id}`)"
        >
          <div class="announcement-content">
            <div class="announcement-title">
              <el-tag v-if="item.isTop" size="small" type="danger">置顶</el-tag>
              <el-tag size="small" :type="item.type === 0 ? 'warning' : 'primary'">
                {{ item.type === 0 ? '公告' : '资讯' }}
              </el-tag>
              <h3>{{ item.title }}</h3>
            </div>
            <p class="announcement-desc">{{ stripHtml(item.content)?.substring(0, 100) }}...</p>
            <div class="announcement-meta">
              <span><el-icon><Clock /></el-icon>{{ item.createTime?.substring(0, 10) }}</span>
              <span><el-icon><View /></el-icon>{{ item.viewCount }}次阅读</span>
            </div>
          </div>
          <el-icon class="arrow"><ArrowRight /></el-icon>
        </div>
      </div>
      
      <el-empty v-if="!loading && list.length === 0" description="暂无公告" />
      
      <div class="pagination-container" v-if="total > 10">
        <el-pagination
          v-model:current-page="pageNum"
          :total="total"
          layout="prev, pager, next"
          @current-change="fetchData"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getAnnouncements } from '@/api/front'

const loading = ref(false)
const list = ref([])
const total = ref(0)
const pageNum = ref(1)
const activeType = ref('')

// 去除HTML标签，用于预览显示
const stripHtml = (html) => {
  if (!html) return ''
  return html.replace(/<[^>]*>/g, '').replace(/\s+/g, ' ').trim()
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getAnnouncements({ 
      pageNum: pageNum.value, 
      pageSize: 10, 
      type: activeType.value || undefined 
    })
    list.value = res.data.records
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

onMounted(() => fetchData())
</script>

<style lang="scss" scoped>
.announcement-list-page {
  padding: 40px 0;
  min-height: calc(100vh - 200px);
}

.page-title {
  font-size: 28px;
  margin-bottom: 24px;
}

.announcement-list {
  margin-top: 20px;
}

.announcement-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24px;
  background: white;
  border-radius: 12px;
  margin-bottom: 16px;
  cursor: pointer;
  transition: all 0.3s;
  
  &:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
    transform: translateX(4px);
    
    .arrow {
      color: var(--primary-color);
    }
  }
  
  .announcement-content {
    flex: 1;
  }
  
  .announcement-title {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-bottom: 12px;
    
    h3 {
      font-size: 18px;
      font-weight: 500;
    }
  }
  
  .announcement-desc {
    color: var(--text-secondary);
    font-size: 14px;
    line-height: 1.6;
    margin-bottom: 12px;
  }
  
  .announcement-meta {
    display: flex;
    gap: 20px;
    font-size: 13px;
    color: #9ca3af;
    
    span {
      display: flex;
      align-items: center;
      gap: 4px;
    }
  }
  
  .arrow {
    font-size: 20px;
    color: #d1d5db;
    transition: color 0.3s;
  }
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 32px;
}
</style>

