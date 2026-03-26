<template>
   <div class="app-container">
      <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
         <el-form-item label="邀请码" prop="code">
            <el-input
               v-model="queryParams.code"
               placeholder="请输入邀请码"
               clearable
               style="width: 200px"
               @keyup.enter="handleQuery"
            />
         </el-form-item>
         <el-form-item label="渠道" prop="channel">
            <el-select v-model="queryParams.channel" placeholder="请选择渠道" clearable style="width: 200px">
               <el-option
                  v-for="dict in invite_channel"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
               />
            </el-select>
         </el-form-item>
         <el-form-item label="状态" prop="status">
            <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 200px">
               <el-option
                  v-for="dict in invite_status"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
               />
            </el-select>
         </el-form-item>
         <el-form-item label="使用时间" prop="useTimeRange">
            <el-date-picker
               v-model="queryParams.useTimeRange"
               type="datetimerange"
               range-separator="至"
               start-placeholder="开始时间"
               end-placeholder="结束时间"
               value-format="YYYY-MM-DD HH:mm:ss"
               style="width: 360px"
            />
         </el-form-item>
         <el-form-item>
            <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
            <el-button icon="Refresh" @click="resetQuery">重置</el-button>
         </el-form-item>
      </el-form>

      <el-row :gutter="10" class="mb8">
         <el-col :span="1.5">
            <el-button
               type="primary"
               plain
               icon="Plus"
               @click="handleAdd"
               v-hasPermi="['core:inviteCode:add']"
            >新增</el-button>
         </el-col>
         <el-col :span="1.5">
            <el-button
               type="success"
               plain
               icon="CirclePlus"
               @click="handleBatchGenerate"
               v-hasPermi="['core:inviteCode:batchGenerate']"
            >批量生成</el-button>
         </el-col>
         <el-col :span="1.5">
            <el-button
               type="warning"
               plain
               icon="Position"
               :disabled="multiple"
               @click="handleBatchDistribute"
               v-hasPermi="['core:inviteCode:distribute']"
            >批量下发</el-button>
         </el-col>
         <el-col :span="1.5">
            <el-button
               type="danger"
               plain
               icon="Delete"
               :disabled="multiple"
               @click="handleDelete"
               v-hasPermi="['core:inviteCode:remove']"
            >删除</el-button>
         </el-col>
         <el-col :span="1.5">
            <el-button
               type="info"
               plain
               icon="Download"
               @click="handleExport"
               v-hasPermi="['core:inviteCode:export']"
            >导出</el-button>
         </el-col>
         <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="inviteCodeList" @selection-change="handleSelectionChange">
         <el-table-column type="selection" width="55" align="center" />
         <el-table-column label="邀请码ID" align="center" prop="id" width="80" />
         <el-table-column label="邀请码" align="center" prop="code" width="180" show-overflow-tooltip />
         <el-table-column label="渠道" align="center" prop="channel" width="120">
            <template #default="scope">
               <dict-tag :options="invite_channel" :value="scope.row.channel" />
            </template>
         </el-table-column>
         <el-table-column label="状态" align="center" prop="status" width="100">
            <template #default="scope">
               <el-tag :type="getStatusType(scope.row.status)">
                  {{ getStatusLabel(scope.row.status) }}
               </el-tag>
            </template>
         </el-table-column>
         <el-table-column label="使用人" align="center" prop="usedByUsername" width="120" show-overflow-tooltip>
            <template #default="scope">
               <span>{{ scope.row.usedByUsername || '-' }}</span>
            </template>
         </el-table-column>
         <el-table-column label="使用时间" align="center" prop="usedTime" width="160">
            <template #default="scope">
               <span>{{ parseTime(scope.row.usedTime) || '-' }}</span>
            </template>
         </el-table-column>
         <el-table-column label="创建时间" align="center" prop="createTime" width="160">
            <template #default="scope">
               <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
         </el-table-column>
         <el-table-column label="备注" align="center" prop="remark" show-overflow-tooltip />
         <el-table-column label="操作" width="200" align="center" class-name="small-padding fixed-width">
            <template #default="scope">
               <el-button link type="primary" icon="View" @click="handleView(scope.row)" v-hasPermi="['core:inviteCode:query']">详情</el-button>
               <el-button 
                  link 
                  type="warning" 
                  icon="Position" 
                  @click="handleDistribute(scope.row)" 
                  v-hasPermi="['core:inviteCode:distribute']"
                  v-if="scope.row.status === '0'"
               >下发</el-button>
               <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['core:inviteCode:remove']">删除</el-button>
            </template>
         </el-table-column>
      </el-table>

      <pagination
         v-show="total > 0"
         :total="total"
         v-model:page="queryParams.pageNum"
         v-model:limit="queryParams.pageSize"
         @pagination="getList"
      />

      <!-- 新增邀请码对话框 -->
      <el-dialog :title="title" v-model="open" width="500px" append-to-body>
         <el-form ref="inviteCodeRef" :model="form" :rules="rules" label-width="80px">
            <el-form-item label="邀请码" prop="code">
               <el-input v-model="form.code" placeholder="请输入邀请码，留空自动生成" />
            </el-form-item>
            <el-form-item label="渠道" prop="channel">
               <el-select v-model="form.channel" placeholder="请选择渠道" style="width: 100%">
                  <el-option
                     v-for="dict in invite_channel"
                     :key="dict.value"
                     :label="dict.label"
                     :value="dict.value"
                  />
               </el-select>
            </el-form-item>
            <el-form-item label="备注" prop="remark">
               <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" :rows="3" />
            </el-form-item>
         </el-form>
         <template #footer>
            <div class="dialog-footer">
               <el-button type="primary" @click="submitForm">确 定</el-button>
               <el-button @click="cancel">取 消</el-button>
            </div>
         </template>
      </el-dialog>

      <!-- 批量生成邀请码对话框 -->
      <el-dialog title="批量生成邀请码" v-model="batchOpen" width="500px" append-to-body>
         <el-form ref="batchRef" :model="batchForm" :rules="batchRules" label-width="100px">
            <el-form-item label="生成数量" prop="count">
               <el-input-number v-model="batchForm.count" :min="1" :max="1000" placeholder="请输入生成数量" style="width: 100%" />
            </el-form-item>
            <el-form-item label="渠道" prop="channel">
               <el-select v-model="batchForm.channel" placeholder="请选择渠道" style="width: 100%">
                  <el-option
                     v-for="dict in invite_channel"
                     :key="dict.value"
                     :label="dict.label"
                     :value="dict.value"
                  />
               </el-select>
            </el-form-item>
            <el-form-item label="备注" prop="remark">
               <el-input v-model="batchForm.remark" type="textarea" placeholder="请输入备注（可选）" :rows="3" />
            </el-form-item>
         </el-form>
         <template #footer>
            <div class="dialog-footer">
               <el-button type="primary" @click="submitBatchForm">确 定</el-button>
               <el-button @click="cancelBatch">取 消</el-button>
            </div>
         </template>
      </el-dialog>

      <!-- 邀请码详情对话框 -->
      <el-dialog title="邀请码详情" v-model="detailOpen" width="600px" append-to-body>
         <el-descriptions :column="2" border v-if="detailData">
            <el-descriptions-item label="邀请码ID" :span="1">{{ detailData.id }}</el-descriptions-item>
            <el-descriptions-item label="邀请码" :span="1">{{ detailData.code }}</el-descriptions-item>
            <el-descriptions-item label="渠道" :span="1">
               <dict-tag :options="invite_channel" :value="detailData.channel" />
            </el-descriptions-item>
            <el-descriptions-item label="状态" :span="1">
               <el-tag :type="getStatusType(detailData.status)">
                  {{ getStatusLabel(detailData.status) }}
               </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="使用人ID" :span="1">{{ detailData.usedBy || '-' }}</el-descriptions-item>
            <el-descriptions-item label="使用人" :span="1">{{ detailData.usedByUsername || '-' }}</el-descriptions-item>
            <el-descriptions-item label="使用时间" :span="2">{{ parseTime(detailData.usedTime) || '-' }}</el-descriptions-item>
            <el-descriptions-item label="创建时间" :span="1">{{ parseTime(detailData.createTime) || '-' }}</el-descriptions-item>
            <el-descriptions-item label="更新时间" :span="1">{{ parseTime(detailData.updateTime) || '-' }}</el-descriptions-item>
            <el-descriptions-item label="备注" :span="2">{{ detailData.remark || '-' }}</el-descriptions-item>
         </el-descriptions>
         <template #footer>
            <div class="dialog-footer">
               <el-button @click="detailOpen = false">关 闭</el-button>
            </div>
         </template>
      </el-dialog>
   </div>
</template>

<script setup name="InviteCode">
import { 
   listInviteCode, 
   addInviteCode, 
   getInviteCode, 
   delInviteCode, 
   batchGenerateInviteCode,
   distributeInviteCode,
   batchDistributeInviteCode,
   exportInviteCode
} from "@/api/core/inviteCode"

const { proxy } = getCurrentInstance()
const { invite_channel, invite_status } = proxy.useDict("invite_channel", "invite_status")

const inviteCodeList = ref([])
const open = ref(false)
const batchOpen = ref(false)
const detailOpen = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
const detailData = ref({})

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    code: undefined,
    channel: undefined,
    status: undefined,
    useTimeRange: undefined
  },
  rules: {
    channel: [{ required: true, message: "渠道不能为空", trigger: "change" }]
  },
  batchForm: {
    count: 10,
    channel: undefined,
    remark: undefined
  },
  batchRules: {
    count: [{ required: true, message: "生成数量不能为空", trigger: "blur" }],
    channel: [{ required: true, message: "渠道不能为空", trigger: "change" }]
  }
})

const { queryParams, form, rules, batchForm, batchRules } = toRefs(data)

// 获取状态标签类型
function getStatusType(status) {
  const typeMap = {
    '0': 'info',      // 未下发
    '1': 'warning',   // 未使用
    '2': 'success'    // 已使用
  }
  return typeMap[status] || 'info'
}

// 获取状态标签文字
function getStatusLabel(status) {
  const labelMap = {
    '0': '未下发',
    '1': '未使用',
    '2': '已使用'
  }
  return labelMap[status] || status
}

/** 查询邀请码列表 */
function getList() {
  loading.value = true
  // 处理时间范围
  const params = { ...queryParams.value }
  if (params.useTimeRange && params.useTimeRange.length === 2) {
    params.usedTimeStart = params.useTimeRange[0]
    params.usedTimeEnd = params.useTimeRange[1]
  }
  delete params.useTimeRange
  
  listInviteCode(params).then(response => {
    inviteCodeList.value = response.data
    total.value = response.total
    loading.value = false
  })
}

/** 取消按钮 */
function cancel() {
  open.value = false
  reset()
}

/** 取消批量生成 */
function cancelBatch() {
  batchOpen.value = false
  resetBatch()
}

/** 表单重置 */
function reset() {
  form.value = {
    id: undefined,
    code: undefined,
    channel: undefined,
    remark: undefined
  }
  proxy.resetForm("inviteCodeRef")
}

/** 批量生成表单重置 */
function resetBatch() {
  batchForm.value = {
    count: 10,
    channel: undefined,
    remark: undefined
  }
  proxy.resetForm("batchRef")
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef")
  queryParams.value.useTimeRange = undefined
  handleQuery()
}

/** 多选框选中数据 */
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "新增邀请码"
}

/** 批量生成按钮操作 */
function handleBatchGenerate() {
  resetBatch()
  batchOpen.value = true
}

/** 查看详情按钮操作 */
function handleView(row) {
  const id = row.id
  getInviteCode(id).then(response => {
    detailData.value = response.data
    detailOpen.value = true
  })
}

/** 下发按钮操作 */
function handleDistribute(row) {
  proxy.$modal.confirm('确认要下发邀请码"' + row.code + '"吗？下发后状态将变为"未使用"').then(function() {
    return distributeInviteCode(row.id)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("下发成功")
  }).catch(() => {})
}

/** 批量下发按钮操作 */
function handleBatchDistribute() {
  const selectedIds = ids.value
  proxy.$modal.confirm('确认要下发选中的' + selectedIds.length + '个邀请码吗？').then(function() {
    return batchDistributeInviteCode(selectedIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("批量下发成功")
  }).catch(() => {})
}

/** 删除按钮操作 */
function handleDelete(row) {
  const inviteCodeIds = row.id ? [row.id] : ids.value
  proxy.$modal.confirm('是否确认删除邀请码编号为"' + inviteCodeIds + '"的数据项？').then(function() {
    // 逐个删除
    const deletePromises = inviteCodeIds.map(id => delInviteCode(id))
    return Promise.all(deletePromises)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["inviteCodeRef"].validate(valid => {
    if (valid) {
      addInviteCode(form.value).then(() => {
        proxy.$modal.msgSuccess("新增成功")
        open.value = false
        getList()
      })
    }
  })
}

/** 提交批量生成表单 */
function submitBatchForm() {
  proxy.$refs["batchRef"].validate(valid => {
    if (valid) {
      batchGenerateInviteCode(batchForm.value).then(response => {
        proxy.$modal.msgSuccess("成功生成 " + response.data + " 个邀请码")
        batchOpen.value = false
        getList()
      })
    }
  })
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download("core/inviteCode/export", {
    ...queryParams.value
  }, `inviteCode_${new Date().getTime()}.xlsx`)
}

getList()
</script>

<style scoped>
/* 可以添加自定义样式 */
</style>
