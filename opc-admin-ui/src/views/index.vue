<template>
  <div class="app-container home-container">
    <el-row :gutter="20">
      <el-col :span="24">
        <h2 class="page-title">数据概览</h2>
      </el-col>
    </el-row>
    
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="data-cards">
      <el-col :xs="24" :sm="12" :md="8" :lg="4">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-value">{{ summaryData.newUserCount || 0 }}</div>
          <div class="stat-label">日新增用户</div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="8" :lg="4">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-value">{{ summaryData.activeUserCount || 0 }}</div>
          <div class="stat-label">日活跃用户</div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="8" :lg="4">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-value">{{ summaryData.normalUserCount || 0 }}</div>
          <div class="stat-label">普通用户</div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="8" :lg="4">
        <el-card class="stat-card vip-card" shadow="hover">
          <div class="stat-value">{{ summaryData.vipUserCount || 0 }}</div>
          <div class="stat-label">VIP用户</div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="8" :lg="4">
        <el-card class="stat-card svip-card" shadow="hover">
          <div class="stat-value">{{ summaryData.svipUserCount || 0 }}</div>
          <div class="stat-label">超级VIP用户</div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="8" :lg="4">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-value">{{ totalUserCount }}</div>
          <div class="stat-label">总用户数</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :span="24">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>用户数据趋势</span>
              <el-radio-group v-model="dateRange" size="small" @change="handleDateRangeChange">
                <el-radio-button label="week">近7天</el-radio-button>
                <el-radio-button label="month">近30天</el-radio-button>
              </el-radio-group>
            </div>
          </template>
          <div ref="chartRef" style="height: 350px; width: 100%"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup name="Index">
import { getMemberOverview } from "@/api/core/memberOverview.js"
import * as echarts from 'echarts'

const { proxy } = getCurrentInstance()

const chartRef = ref(null)
let chartInstance = null

const dateRange = ref('week')
const summaryData = ref({})
const totalUserCount = computed(() => {
  const normal = summaryData.value.normalUserCount || 0
  const vip = summaryData.value.vipUserCount || 0
  const svip = summaryData.value.svipUserCount || 0
  return normal + vip + svip
})

function formatDate(date) {
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  return `${year}-${month}-${day}`
}

function getDateRange(range) {
  const now = new Date()
  const end = formatDate(now)
  const start = new Date()
  
  if (range === 'week') {
    start.setDate(start.getDate() - 7)
  } else {
    start.setDate(start.getDate() - 30)
  }
  
  return {
    startDate: formatDate(start),
    endDate: end
  }
}

function loadData() {
  const params = getDateRange(dateRange.value)
  
  getMemberOverview(params).then(response => {
    const data = response.data || []
    
    // 提取汇总数据（日期为结束日期的那一行）
    const summary = data.find(item => item.date === params.endDate) || {}
    summaryData.value = summary
    
    // 提取每日数据用于图表
    const dailyData = data.filter(item => {
      return item.date && item.date !== params.endDate
    }).sort((a, b) => a.date.localeCompare(b.date))
    
    initChart(dailyData)
  })
}

function initChart(chartData) {
  if (!chartRef.value) return
  
  if (chartInstance) {
    chartInstance.dispose()
  }
  
  chartInstance = echarts.init(chartRef.value)
  
  const dates = chartData.map(item => item.date)
  const newUserCounts = chartData.map(item => item.newUserCount || 0)
  const activeUserCounts = chartData.map(item => item.activeUserCount || 0)
  
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'cross'
      }
    },
    legend: {
      data: ['日新增用户', '日活跃用户'],
      bottom: 0
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '15%',
      top: '10%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: dates,
      axisLabel: {
        rotate: 45,
        fontSize: 11
      }
    },
    yAxis: {
      type: 'value',
      name: '用户数'
    },
    series: [
      {
        name: '日新增用户',
        type: 'bar',
        data: newUserCounts,
        itemStyle: {
          color: '#409EFF'
        }
      },
      {
        name: '日活跃用户',
        type: 'line',
        data: activeUserCounts,
        smooth: true,
        itemStyle: {
          color: '#67C23A'
        },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(103, 194, 58, 0.3)' },
            { offset: 1, color: 'rgba(103, 194, 58, 0.05)' }
          ])
        }
      }
    ]
  }
  
  chartInstance.setOption(option)
}

function handleDateRangeChange() {
  loadData()
}

// 监听窗口大小变化
window.addEventListener('resize', () => {
  if (chartInstance) {
    chartInstance.resize()
  }
})

onUnmounted(() => {
  if (chartInstance) {
    chartInstance.dispose()
    chartInstance = null
  }
})

// 初始化加载
loadData()
</script>

<style scoped lang="scss">
.home-container {
  .page-title {
    margin: 0 0 20px 0;
    font-size: 24px;
    font-weight: 600;
    color: #303133;
  }
  
  .data-cards {
    margin-bottom: 20px;
    
    .el-col {
      margin-bottom: 15px;
    }
    
    .stat-card {
      text-align: center;
      
      :deep(.el-card__body) {
        padding: 20px;
      }
      
      .stat-value {
        font-size: 28px;
        font-weight: bold;
        color: #409EFF;
        margin-bottom: 8px;
      }
      
      .stat-label {
        font-size: 14px;
        color: #606266;
      }
    }
    
    .vip-card {
      .stat-value {
        color: #e6a23c;
      }
    }
    
    .svip-card {
      .stat-value {
        color: #f56c6c;
      }
    }
    
    .total-card {
      .stat-value {
        color: #67C23A;
      }
    }
  }
  
  .chart-row {
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
  }
}
</style>
