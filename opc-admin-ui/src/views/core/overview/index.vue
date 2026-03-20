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
          <el-button @click="setDateRange('month')">一月</el-button>
          <el-button @click="setDateRange('quarter')">一季</el-button>
          <el-button @click="setDateRange('year')">一年</el-button>
        </el-button-group>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="overviewList" border>
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
    </el-table>
  </div>
</template>

<script setup name="MemberOverview">
import { getMemberOverview } from "@/api/core/memberOverview.js"

const { proxy } = getCurrentInstance()

const loading = ref(false)
const showSearch = ref(true)
const overviewList = ref([])

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
</style>
