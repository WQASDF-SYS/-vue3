<template>
  <div class="orders-page">
    <h2>我的订单</h2>
    <el-tabs v-model="activeTab" @tab-change="fetchData">
      <el-tab-pane label="全部" name="" />
      <el-tab-pane label="待审核" name="0" />
      <el-tab-pane label="已确认" name="1" />
      <el-tab-pane label="已入住" name="2" />
      <el-tab-pane label="已完成" name="3" />
      <el-tab-pane label="已取消" name="4" />
    </el-tabs>
    
    <div class="order-list" v-loading="loading">
      <div v-for="order in list" :key="order.id" class="order-item">
        <div class="order-header">
          <span class="order-no">订单号：{{ order.orderNo }}</span>
          <el-tag :type="statusType(order.status)">{{ statusText(order.status) }}</el-tag>
        </div>
        <div class="order-body">
          <img :src="order.homestayCover || '/placeholder.jpg'" class="order-image" />
          <div class="order-info">
            <h4>{{ order.homestayName }}</h4>
            <p>入住：{{ order.checkInDate }} ~ {{ order.checkOutDate }}</p>
            <p>入住人：{{ order.guestName }} | {{ order.guestPhone }}</p>
          </div>
          <div class="order-price">
            <p class="total">¥{{ order.totalPrice }}</p>
            <p class="days">共{{ order.days }}晚</p>
          </div>
        </div>
        <div class="order-footer">
          <span class="time">下单时间：{{ order.createTime }}</span>
          <div class="actions">
            <el-button v-if="order.status < 2" size="small" @click="handleCancel(order)">取消订单</el-button>
            <el-button v-if="order.status === 3" type="primary" size="small" @click="goComment(order)">去评价</el-button>
          </div>
        </div>
      </div>
      <el-empty v-if="!loading && list.length === 0" description="暂无订单" />
    </div>
    
    <div class="pagination-container" v-if="total > 10">
      <el-pagination
        v-model:current-page="pageNum"
        :total="total"
        layout="prev, pager, next"
        @current-change="fetchData"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getMyOrders, cancelOrder } from '@/api/user'

const router = useRouter()
const loading = ref(false)
const list = ref([])
const total = ref(0)
const pageNum = ref(1)
const activeTab = ref('')

const statusText = (s) => ({ 0: '待审核', 1: '已确认', 2: '已入住', 3: '已完成', 4: '已取消' }[s])
const statusType = (s) => ({ 0: 'warning', 1: 'primary', 2: 'success', 3: 'info', 4: 'danger' }[s])

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getMyOrders({ pageNum: pageNum.value, pageSize: 10, status: activeTab.value || undefined })
    list.value = res.data.records
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

const handleCancel = (order) => {
  ElMessageBox.confirm('确定要取消该订单吗？', '提示', { type: 'warning' })
    .then(async () => {
      await cancelOrder(order.id, '用户主动取消')
      ElMessage.success('订单已取消')
      fetchData()
    }).catch(() => {})
}

const goComment = (order) => {
  router.push(`/homestay/${order.homestayId}`)
}

onMounted(() => fetchData())
</script>

<style lang="scss" scoped>
h2 { margin-bottom: 24px; font-size: 20px; }

.order-list { margin-top: 20px; }

.order-item {
  border: 1px solid var(--border-color);
  border-radius: 8px;
  margin-bottom: 16px;
  overflow: hidden;
  
  .order-header {
    display: flex;
    justify-content: space-between;
    padding: 12px 16px;
    background: #f9fafb;
    border-bottom: 1px solid var(--border-color);
    
    .order-no {
      font-size: 13px;
      color: var(--text-secondary);
    }
  }
  
  .order-body {
    display: flex;
    padding: 16px;
    gap: 16px;
    
    .order-image {
      width: 120px;
      height: 80px;
      object-fit: cover;
      border-radius: 6px;
    }
    
    .order-info {
      flex: 1;
      
      h4 {
        font-size: 16px;
        margin-bottom: 8px;
      }
      
      p {
        font-size: 13px;
        color: var(--text-secondary);
        margin-bottom: 4px;
      }
    }
    
    .order-price {
      text-align: right;
      
      .total {
        font-size: 20px;
        font-weight: 600;
        color: #ef4444;
      }
      
      .days {
        font-size: 13px;
        color: var(--text-secondary);
      }
    }
  }
  
  .order-footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 12px 16px;
    background: #f9fafb;
    border-top: 1px solid var(--border-color);
    
    .time {
      font-size: 12px;
      color: var(--text-secondary);
    }
  }
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 24px;
}
</style>

