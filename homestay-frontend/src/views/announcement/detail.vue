<template>
  <div class="announcement-detail-page" v-loading="loading">
    <div class="container" v-if="detail">
      <div class="detail-card">
        <div class="detail-header">
          <div class="tags">
            <el-tag v-if="detail.isTop" type="danger">置顶</el-tag>
            <el-tag :type="detail.type === 0 ? 'warning' : 'primary'">
              {{ detail.type === 0 ? '公告' : '资讯' }}
            </el-tag>
          </div>
          <h1>{{ detail.title }}</h1>
          <div class="meta">
            <span><el-icon><Clock /></el-icon>{{ detail.createTime }}</span>
            <span><el-icon><View /></el-icon>{{ detail.viewCount }}次阅读</span>
          </div>
        </div>
        <div class="detail-content" v-html="formattedContent"></div>
        <div class="detail-footer">
          <el-button @click="$router.back()">
            <el-icon><ArrowLeft /></el-icon>返回列表
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getAnnouncementDetail } from '@/api/front'

const route = useRoute()
const loading = ref(true)
const detail = ref(null)

// 格式化内容 - 处理纯文本和HTML内容
const formattedContent = computed(() => {
  if (!detail.value?.content) return ''
  const content = detail.value.content
  // 如果内容已经包含HTML标签，直接返回
  if (content.includes('<p>') || content.includes('<div>') || content.includes('<br')) {
    return content
  }
  // 如果是纯文本，将换行转换为段落
  return content.split('\n').filter(p => p.trim()).map(p => `<p>${p}</p>`).join('')
})

const fetchDetail = async () => {
  loading.value = true
  try {
    const res = await getAnnouncementDetail(route.params.id)
    detail.value = res.data
  } finally {
    loading.value = false
  }
}

onMounted(() => fetchDetail())
</script>

<style lang="scss" scoped>
.announcement-detail-page {
  padding: 40px 0;
  min-height: calc(100vh - 200px);
}

.detail-card {
  max-width: 800px;
  margin: 0 auto;
  background: white;
  border-radius: 16px;
  padding: 40px;
}

.detail-header {
  text-align: center;
  margin-bottom: 40px;
  padding-bottom: 24px;
  border-bottom: 1px solid var(--border-color);
  
  .tags {
    margin-bottom: 16px;
  }
  
  h1 {
    font-size: 28px;
    font-weight: 600;
    margin-bottom: 16px;
  }
  
  .meta {
    display: flex;
    justify-content: center;
    gap: 24px;
    color: var(--text-secondary);
    font-size: 14px;
    
    span {
      display: flex;
      align-items: center;
      gap: 4px;
    }
  }
}

.detail-content {
  min-height: 200px;
  line-height: 2;
  font-size: 16px;
  color: var(--text-color);
  
  :deep(p) {
    margin-bottom: 16px;
    
    &:last-child {
      margin-bottom: 0;
    }
  }
  
  :deep(img) {
    max-width: 100%;
    border-radius: 8px;
  }
}

.detail-footer {
  margin-top: 40px;
  padding-top: 24px;
  border-top: 1px solid var(--border-color);
}
</style>

