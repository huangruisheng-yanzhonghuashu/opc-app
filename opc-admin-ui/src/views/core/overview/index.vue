<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
      <el-form-item label="日期开始" prop="startDate">
        <el-date-picker
          v-model="queryParams.startDate"
          type="date"
          placeholder="选择开始日期"
          value-format="YYYY-MM-DD"
          style="width: 200px"
        />
      </el-form-item>
      <el-form-item label="日期结束" prop="endDate">
        <el-date-picker
          v-model="queryParams.endDate"
          type="date"
          placeholder="选择结束日期"
          value-format="YYYY-MM-DD"
          style="width: 200px"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
      <el-form-item>
        <el-button-group>
          <el-button type="info" plain @click="setDateRange('month')">一月</el-button>
          <el-button type="warning" plain @click="setDateRange('quarter')">一季</el-button>
          <el-button type="primary" plain @click="setDateRange('year')">一年</el-button>
        </el-button-group>
      </el-form-item>
      <el-form-item>
        <el-radio-group v-model="viewMode" @change="handleViewModeChange">
          <el-radio-button label="table">
            <el-icon><Grid /></el-icon> 表格
          </el-radio-button>
          <el-radio-button label="chart">
            <el-icon><TrendCharts /></el-icon> 图表
          </el-radio-button>
        </el-radio-group>
      </el-form-item>
    </el-form>

    <!-- 表格视图 -->
    <el-table v-if="viewMode === 'table'" v-loading="loading" :data="overviewList" border>
      <el-table-column label="日期" align="center" prop="date" width="180">
        <template #default="scope">
          <span>{{ parseDate(scope.row.date) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="日新增用户数" align="center" prop="newUserCount">
        <template #default="scope">
          <span class="count-number">{{ scope.row.newUserCount || 0 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="日活跃用户" align="center" prop="activeUserCount">
        <template #default="scope">
          <span class="count-number">{{ scope.row.activeUserCount || 0 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="普通用户数" align="center" prop="normalUserCount">
        <template #default="scope">
          <span class="count-number">{{ scope.row.normalUserCount || 0 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="VIP用户数" align="center" prop="vipUserCount">
        <template #default="scope">
          <span class="count-number vip-count">{{ scope.row.vipUserCount || 0 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="超级VIP用户数" align="center" prop="svipUserCount">
        <template #default="scope">
          <span class="count-number svip-count">{{ scope.row.svipUserCount || 0 }}</span>
        </template>
      </el-table-column>
    </el-table>

    <!-- 图表视图 -->
    <el-card v-else v-loading="loading" class="chart-card">
      <template #header>
        <span>用户数据趋势图</span>
      </template>
      <div ref="chartRef" style="height: 400px; width: 100%"></div>
    </el-card>
  </div>
</template>

<script setup name="MemberOverview">
import { getMemberOverview } from "@/api/core/memberOverview.js"
import * as echarts from 'echarts'
import { Grid, TrendCharts } from '@element-plus/icons-vue'

const { proxy } = getCurrentInstance()

const loading = ref(false)
const showSearch = ref(true)
const overviewList = ref([])
const viewMode = ref('table')
const chartRef = ref(null)
let chartInstance = null

const defaultStartDate = ref('')
const defaultEndDate = ref('')

function initDefaultDates() {
  const now = new Date()
  const oneMonthAgo = new Date()
  oneMonthAgo.setMonth(oneMonthAgo.getMonth() - 1)

  defaultEndDate.value = formatDate(now)
  defaultStartDate.value = formatDate(oneMonthAgo)
}

function formatDate(date) {
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  return `${year}-${month}-${day}`
}

const queryParams = reactive({
  startDate: '',
  endDate: ''
})

function parseDate(date) {
  if (!date) return '-'
  return date
}

function getList() {
  loading.value = true
  const params = {
    startDate: queryParams.startDate || defaultStartDate.value,
    endDate: queryParams.endDate || defaultEndDate.value
  }
  getMemberOverview(params).then(response => {
    const data = response.data || []
    overviewList.value = data.sort((a, b) => {
      if (!a.date) return 1
      if (!b.date) return -1
      return a.date.localeCompare(b.date)
    })
    loading.value = false
    // 如果当前是图表视图，更新图表
    if (viewMode.value === 'chart') {
      nextTick(() => {
        initChart()
      })
    }
  }).catch(() => {
    loading.value = false
  })
}

function handleQuery() {
  getList()
}

function resetQuery() {
  queryParams.startDate = defaultStartDate.value
  queryParams.endDate = defaultEndDate.value
  getList()
}

function setDateRange(range) {
  const now = new Date()
  const start = new Date()
  switch (range) {
    case 'month':
      start.setMonth(start.getMonth() - 1)
      break
    case 'quarter':
      start.setMonth(start.getMonth() - 3)
      break
    case 'year':
      start.setFullYear(start.getFullYear() - 1)
      break
  }
  queryParams.startDate = formatDate(start)
  queryParams.endDate = formatDate(now)
  getList()
}

function handleViewModeChange() {
  if (viewMode.value === 'chart') {
    nextTick(() => {
      initChart()
    })
  }
}

function initChart() {
  if (!chartRef.value) return
  
  if (chartInstance) {
    chartInstance.dispose()
  }
  
  chartInstance = echarts.init(chartRef.value)
  
  // 过滤掉汇总数据（没有日期或日期为结束日期的汇总行）
  const dailyData = overviewList.value.filter(item => {
    return item.date && item.date !== queryParams.endDate && item.date !== defaultEndDate.value
  })
  
  // 如果没有每日数据，使用所有数据
  const chartData = dailyData.length > 0 ? dailyData : overviewList.value
  
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
        rotate: 45
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
        }
      }
    ]
  }
  
  chartInstance.setOption(option)
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
  window.removeEventListener('resize', () => {
    if (chartInstance) {
      chartInstance.resize()
    }
  })
})

initDefaultDates()
queryParams.startDate = defaultStartDate.value
queryParams.endDate = defaultEndDate.value
getList()
</script>

<style scoped>
.count-number {
  font-weight: bold;
  font-size: 16px;
}

.vip-count {
  color: #e6a23c;
}

.svip-count {
  color: #f56c6c;
}

.chart-card {
  margin-top: 10px;
}
</style>
