<template>
  <div class="dashboard-container">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stat-cards">
      <el-col :span="6">
        <div class="stat-card" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%)">
          <div class="stat-icon">
            <el-icon size="32"><User /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.userCount || 0 }}</div>
            <div class="stat-label">用户总数</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%)">
          <div class="stat-icon">
            <el-icon size="32"><House /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.homestayCount || 0 }}</div>
            <div class="stat-label">民宿总数</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)">
          <div class="stat-icon">
            <el-icon size="32"><List /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.todayCount || 0 }}</div>
            <div class="stat-label">今日订单</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card" style="background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)">
          <div class="stat-icon">
            <el-icon size="32"><Wallet /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">¥{{ (stats.monthAmount || 0).toFixed(2) }}</div>
            <div class="stat-label">本月收入</div>
          </div>
        </div>
      </el-col>
    </el-row>
    
    <!-- 图表区域 -->
    <el-row :gutter="20">
      <el-col :span="16">
        <el-card class="chart-card">
          <template #header>
            <span>订单趋势</span>
          </template>
          <div ref="orderChartRef" class="chart"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="chart-card">
          <template #header>
            <span>订单状态分布</span>
          </template>
          <div ref="statusChartRef" class="chart"></div>
        </el-card>
      </el-col>
    </el-row>
    
    <!-- 快捷操作 -->
    <el-card class="quick-actions">
      <template #header>
        <span>快捷操作</span>
      </template>
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="action-item" @click="$router.push('/user')">
            <el-icon size="40" color="#667eea"><User /></el-icon>
            <span>用户管理</span>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="action-item" @click="$router.push('/homestay')">
            <el-icon size="40" color="#f5576c"><House /></el-icon>
            <span>民宿管理</span>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="action-item" @click="$router.push('/order')">
            <el-icon size="40" color="#4facfe"><List /></el-icon>
            <span>订单管理</span>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="action-item" @click="$router.push('/announcement')">
            <el-icon size="40" color="#43e97b"><Bell /></el-icon>
            <span>发布公告</span>
          </div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import * as echarts from 'echarts'
import { User, House, List, Bell, Wallet } from '@element-plus/icons-vue'
import { getOrderStatistics } from '@/api/order'
import { getUsers } from '@/api/user'
import { getHomestays } from '@/api/homestay'

const stats = ref({})
const orderChartRef = ref()
const statusChartRef = ref()

let orderChart = null
let statusChart = null

const fetchStats = async () => {
  try {
    // 获取订单统计
    const orderRes = await getOrderStatistics()
    stats.value.todayCount = orderRes.data.todayCount
    stats.value.monthAmount = orderRes.data.monthAmount
    stats.value.totalCount = orderRes.data.totalCount
    
    // 获取用户数量
    const userRes = await getUsers({ pageNum: 1, pageSize: 1 })
    stats.value.userCount = userRes.data.total
    
    // 获取民宿数量
    const homestayRes = await getHomestays({ pageNum: 1, pageSize: 1 })
    stats.value.homestayCount = homestayRes.data.total
    
    // 更新图表
    updateStatusChart(orderRes.data.statusCount || [])
    updateOrderChart(orderRes.data.weekTrend || [], orderRes.data.weekAmount || [])
  } catch (error) {
    console.error('获取统计数据失败:', error)
  }
}

const initCharts = () => {
  // 订单趋势图
  orderChart = echarts.init(orderChartRef.value)
  
  // 状态分布图
  statusChart = echarts.init(statusChartRef.value)
  updateStatusChart([])
}

const updateOrderChart = (trendData, amountData) => {
  // 生成近7天日期
  const dates = []
  const orderCounts = []
  const amounts = []
  
  for (let i = 6; i >= 0; i--) {
    const date = new Date()
    date.setDate(date.getDate() - i)
    const dateStr = date.toISOString().split('T')[0]
    const displayDate = `${date.getMonth() + 1}/${date.getDate()}`
    dates.push(displayDate)
    
    // 从数据中查找对应日期的值
    const trend = trendData.find(t => t.date === dateStr)
    const amount = amountData.find(a => a.date === dateStr)
    
    orderCounts.push(trend ? trend.count : 0)
    amounts.push(amount ? amount.amount : 0)
  }
  
  orderChart?.setOption({
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'cross' }
    },
    legend: {
      data: ['订单数', '收入(元)'],
      top: 0
    },
    xAxis: {
      type: 'category',
      data: dates
    },
    yAxis: [
      { type: 'value', name: '订单数', position: 'left' },
      { type: 'value', name: '收入(元)', position: 'right' }
    ],
    series: [
      {
        name: '订单数',
        data: orderCounts,
        type: 'bar',
        barWidth: '40%',
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#667eea' },
            { offset: 1, color: '#764ba2' }
          ])
        }
      },
      {
        name: '收入(元)',
        data: amounts,
        type: 'line',
        smooth: true,
        yAxisIndex: 1,
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(67, 233, 123, 0.5)' },
            { offset: 1, color: 'rgba(67, 233, 123, 0.05)' }
          ])
        },
        lineStyle: { color: '#43e97b' },
        itemStyle: { color: '#43e97b' }
      }
    ],
    grid: { left: 60, right: 60, top: 40, bottom: 30 }
  })
}

const updateStatusChart = (data) => {
  const statusMap = {
    0: '待审核',
    1: '已确认',
    2: '已入住',
    3: '已完成',
    4: '已取消'
  }
  const colorMap = {
    0: '#E6A23C',
    1: '#409EFF',
    2: '#67C23A',
    3: '#909399',
    4: '#F56C6C'
  }
  
  const pieData = data.map(item => ({
    name: statusMap[item.status] || '未知',
    value: item.count,
    itemStyle: { color: colorMap[item.status] }
  }))
  
  if (pieData.length === 0) {
    pieData.push({ name: '暂无数据', value: 1, itemStyle: { color: '#ddd' } })
  }
  
  statusChart?.setOption({
    tooltip: { trigger: 'item' },
    series: [{
      type: 'pie',
      radius: ['40%', '70%'],
      data: pieData,
      label: { show: true, formatter: '{b}: {c}' }
    }]
  })
}

const handleResize = () => {
  orderChart?.resize()
  statusChart?.resize()
}

onMounted(() => {
  fetchStats()
  initCharts()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  orderChart?.dispose()
  statusChart?.dispose()
})
</script>

<style lang="scss" scoped>
.dashboard-container {
  padding: 20px;
}

.stat-cards {
  margin-bottom: 20px;
}

.stat-card {
  padding: 24px;
  border-radius: 12px;
  color: #fff;
  display: flex;
  align-items: center;
  gap: 16px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  
  .stat-icon {
    width: 60px;
    height: 60px;
    background: rgba(255, 255, 255, 0.2);
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
  }
  
  .stat-info {
    .stat-value {
      font-size: 28px;
      font-weight: 600;
      line-height: 1.2;
    }
    
    .stat-label {
      font-size: 14px;
      opacity: 0.9;
      margin-top: 4px;
    }
  }
}

.chart-card {
  margin-bottom: 20px;
  
  .chart {
    height: 300px;
  }
}

.quick-actions {
  .action-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 12px;
    padding: 24px;
    background: #f5f7fa;
    border-radius: 8px;
    cursor: pointer;
    transition: all 0.3s;
    
    span {
      font-size: 14px;
      color: #606266;
    }
    
    &:hover {
      background: #ecf5ff;
      transform: translateY(-2px);
    }
  }
}
</style>

