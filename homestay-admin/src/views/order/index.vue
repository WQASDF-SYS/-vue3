<template>
  <div class="page-container">
    <!-- 搜索栏 -->
    <div class="search-bar">
      <el-form inline>
        <el-form-item label="订单号">
          <el-input v-model="query.orderNo" placeholder="订单编号" clearable @clear="fetchData" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="query.status" placeholder="全部" clearable @change="fetchData">
            <el-option label="待审核" :value="0" />
            <el-option label="已确认" :value="1" />
            <el-option label="已入住" :value="2" />
            <el-option label="已完成" :value="3" />
            <el-option label="已取消" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="fetchData">搜索</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    
    <!-- 表格 -->
    <div class="table-card">
      <div class="card-header">
        <span class="title">订单列表</span>
        <el-button v-permission="'order:export'" type="success" @click="handleExport">
          <el-icon><Download /></el-icon>导出
        </el-button>
      </div>
      
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="orderNo" label="订单编号" width="200" />
        <el-table-column prop="homestayName" label="民宿名称" min-width="150" show-overflow-tooltip />
        <el-table-column prop="userName" label="下单用户" width="100" />
        <el-table-column label="入住日期" width="120">
          <template #default="{ row }">{{ row.checkInDate }}</template>
        </el-table-column>
        <el-table-column label="退房日期" width="120">
          <template #default="{ row }">{{ row.checkOutDate }}</template>
        </el-table-column>
        <el-table-column prop="days" label="天数" width="70" />
        <el-table-column label="总价" width="100">
          <template #default="{ row }">¥{{ row.totalPrice }}</template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="statusType(row.status)">{{ statusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="200">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleDetail(row)">详情</el-button>
            <el-button v-if="row.status === 0" link type="success" @click="handleAudit(row, 1)">通过</el-button>
            <el-button v-if="row.status === 0" link type="warning" @click="handleAudit(row, 4)">拒绝</el-button>
            <el-button v-if="row.status === 1" link type="primary" @click="handleAudit(row, 2)">入住</el-button>
            <el-button v-if="row.status === 2" link type="success" @click="handleAudit(row, 3)">完成</el-button>
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
    
    <!-- 详情对话框 -->
    <el-dialog v-model="detailVisible" title="订单详情" width="600px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="订单编号">{{ detail.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="订单状态">
          <el-tag :type="statusType(detail.status)">{{ statusText(detail.status) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="民宿名称">{{ detail.homestayName }}</el-descriptions-item>
        <el-descriptions-item label="下单用户">{{ detail.userName }}</el-descriptions-item>
        <el-descriptions-item label="入住日期">{{ detail.checkInDate }}</el-descriptions-item>
        <el-descriptions-item label="退房日期">{{ detail.checkOutDate }}</el-descriptions-item>
        <el-descriptions-item label="入住天数">{{ detail.days }}天</el-descriptions-item>
        <el-descriptions-item label="入住人数">{{ detail.guests }}人</el-descriptions-item>
        <el-descriptions-item label="单价">¥{{ detail.unitPrice }}/晚</el-descriptions-item>
        <el-descriptions-item label="总价">¥{{ detail.totalPrice }}</el-descriptions-item>
        <el-descriptions-item label="入住人">{{ detail.guestName }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ detail.guestPhone }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ detail.remark || '无' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间" :span="2">{{ detail.createTime }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Download } from '@element-plus/icons-vue'
import { getOrders, getOrder, updateOrderStatus } from '@/api/order'
import { exportOrders } from '@/api/excel'

const loading = ref(false)
const detailVisible = ref(false)
const tableData = ref([])
const total = ref(0)
const detail = ref({})

const query = reactive({
  pageNum: 1,
  pageSize: 10,
  orderNo: '',
  status: null
})

const statusText = (status) => {
  const map = { 0: '待审核', 1: '已确认', 2: '已入住', 3: '已完成', 4: '已取消' }
  return map[status] || '未知'
}

const statusType = (status) => {
  const map = { 0: 'warning', 1: 'primary', 2: 'success', 3: 'info', 4: 'danger' }
  return map[status] || 'info'
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getOrders(query)
    tableData.value = res.data.records
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

const resetQuery = () => {
  query.orderNo = ''
  query.status = null
  query.pageNum = 1
  fetchData()
}

const handleDetail = async (row) => {
  const res = await getOrder(row.id)
  detail.value = res.data
  detailVisible.value = true
}

const handleAudit = (row, status) => {
  const action = { 1: '确认', 2: '办理入住', 3: '完成', 4: '拒绝' }[status]
  ElMessageBox.confirm(`确定要${action}该订单吗？`, '提示', { type: 'warning' })
    .then(async () => {
      await updateOrderStatus(row.id, status)
      ElMessage.success('操作成功')
      fetchData()
    })
    .catch(() => {})
}

const handleExport = () => {
  const params = {}
  if (query.orderNo) params.keyword = query.orderNo
  if (query.status !== null) params.status = query.status
  window.open(exportOrders(params), '_blank')
}

fetchData()
</script>

