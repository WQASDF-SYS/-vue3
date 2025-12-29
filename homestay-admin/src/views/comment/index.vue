<template>
  <div class="page-container">
    <div class="search-bar">
      <el-form inline>
        <el-form-item label="状态">
          <el-select v-model="query.status" placeholder="全部" clearable @change="fetchData">
            <el-option label="待审核" :value="0" />
            <el-option label="已通过" :value="1" />
            <el-option label="已拒绝" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="fetchData">搜索</el-button>
        </el-form-item>
      </el-form>
    </div>
    
    <div class="table-card">
      <div class="card-header">
        <span class="title">评论列表</span>
      </div>
      
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="userName" label="用户" width="120" />
        <el-table-column prop="homestayName" label="民宿" width="150" show-overflow-tooltip />
        <el-table-column prop="content" label="评论内容" min-width="200" show-overflow-tooltip />
        <el-table-column label="评分" width="150">
          <template #default="{ row }">
            <el-rate v-model="row.rating" disabled />
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="statusType(row.status)">{{ statusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" fixed="right" width="180">
          <template #default="{ row }">
            <el-button v-if="row.status === 0" link type="success" @click="handleAudit(row, 1)">通过</el-button>
            <el-button v-if="row.status === 0" link type="warning" @click="handleAudit(row, 2)">拒绝</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="query.pageNum"
          v-model:page-size="query.pageSize"
          :total="total"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next"
          @size-change="fetchData"
          @current-change="fetchData"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getComments, auditComment, deleteComment } from '@/api/comment'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)

const query = reactive({ pageNum: 1, pageSize: 10, status: null })

const statusText = (status) => ({ 0: '待审核', 1: '已通过', 2: '已拒绝' }[status] || '未知')
const statusType = (status) => ({ 0: 'warning', 1: 'success', 2: 'danger' }[status] || 'info')

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getComments(query)
    tableData.value = res.data.records
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

const handleAudit = async (row, status) => {
  await auditComment(row.id, status)
  ElMessage.success('操作成功')
  fetchData()
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该评论吗？', '提示', { type: 'warning' })
    .then(async () => {
      await deleteComment(row.id)
      ElMessage.success('删除成功')
      fetchData()
    }).catch(() => {})
}

fetchData()
</script>

