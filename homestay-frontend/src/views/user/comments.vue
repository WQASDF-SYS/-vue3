<template>
  <div class="comments-page">
    <h2>我的评论</h2>
    <div class="comments-list" v-loading="loading">
      <div v-for="item in list" :key="item.id" class="comment-item">
        <div class="comment-header">
          <span class="homestay">{{ item.homestayName }}</span>
          <el-rate v-model="item.rating" disabled size="small" />
        </div>
        <p class="comment-content">{{ item.content }}</p>
        <div class="comment-footer">
          <span class="time">{{ item.createTime }}</span>
          <el-button link type="danger" size="small" @click="handleDelete(item)">删除</el-button>
        </div>
      </div>
    </div>
    <el-empty v-if="!loading && list.length === 0" description="暂无评论" />
    <div class="pagination-container" v-if="total > 10">
      <el-pagination v-model:current-page="pageNum" :total="total" layout="prev, pager, next" @current-change="fetchData" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getMyComments, deleteComment } from '@/api/user'

const loading = ref(false)
const list = ref([])
const total = ref(0)
const pageNum = ref(1)

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getMyComments({ pageNum: pageNum.value, pageSize: 10 })
    list.value = res.data.records
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

const handleDelete = (item) => {
  ElMessageBox.confirm('确定删除该评论？', '提示', { type: 'warning' })
    .then(async () => {
      await deleteComment(item.id)
      ElMessage.success('删除成功')
      fetchData()
    }).catch(() => {})
}

onMounted(() => fetchData())
</script>

<style lang="scss" scoped>
h2 { margin-bottom: 24px; font-size: 20px; }

.comment-item {
  padding: 16px;
  border: 1px solid var(--border-color);
  border-radius: 8px;
  margin-bottom: 12px;
  
  .comment-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 12px;
    
    .homestay {
      font-weight: 500;
    }
  }
  
  .comment-content {
    color: var(--text-secondary);
    margin-bottom: 12px;
  }
  
  .comment-footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    .time {
      font-size: 12px;
      color: #9ca3af;
    }
  }
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 24px;
}
</style>

