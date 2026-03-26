<template>
   <div class="app-container">
      <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
         <el-form-item label="会员名称" prop="memberName">
            <el-input
               v-model="queryParams.memberName"
               placeholder="请输入会员名称"
               clearable
               style="width: 200px"
               @keyup.enter="handleQuery"
            />
         </el-form-item>
         <el-form-item label="反馈类型" prop="type">
            <el-select v-model="queryParams.type" placeholder="请选择反馈类型" clearable style="width: 200px">
               <el-option label="功能异常" value="bug" />
               <el-option label="功能建议" value="feature" />
               <el-option label="其他" value="other" />
            </el-select>
         </el-form-item>
         <el-form-item label="反馈标题" prop="title">
            <el-input
               v-model="queryParams.title"
               placeholder="请输入反馈标题"
               clearable
               style="width: 200px"
               @keyup.enter="handleQuery"
            />
         </el-form-item>
         <el-form-item label="处理状态" prop="status">
            <el-select v-model="queryParams.status" placeholder="请选择处理状态" clearable style="width: 200px">
               <el-option label="待处理" value="0" />
               <el-option label="处理中" value="1" />
               <el-option label="已处理" value="2" />
            </el-select>
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
               v-hasPermi="['core:feedback:add']"
            >新增</el-button>
         </el-col>
         <el-col :span="1.5">
            <el-button
               type="success"
               plain
               icon="Edit"
               :disabled="single"
               @click="handleUpdate"
               v-hasPermi="['core:feedback:edit']"
            >修改</el-button>
         </el-col>
         <el-col :span="1.5">
            <el-button
               type="danger"
               plain
               icon="Delete"
               :disabled="multiple"
               @click="handleDelete"
               v-hasPermi="['core:feedback:remove']"
            >删除</el-button>
         </el-col>
         <el-col :span="1.5">
            <el-button
               type="warning"
               plain
               icon="Download"
               @click="handleExport"
               v-hasPermi="['core:feedback:export']"
            >导出</el-button>
         </el-col>
         <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="feedbackList" @selection-change="handleSelectionChange">
         <el-table-column type="selection" width="55" align="center" />
         <el-table-column label="反馈ID" align="center" prop="id" width="80" />
         <el-table-column label="会员名称" align="center" prop="memberName" width="120" />
         <el-table-column label="反馈类型" align="center" prop="type" width="100">
            <template #default="scope">
               <el-tag v-if="scope.row.type === 'bug'" type="danger">功能异常</el-tag>
               <el-tag v-else-if="scope.row.type === 'feature'" type="primary">功能建议</el-tag>
               <el-tag v-else type="info">其他</el-tag>
            </template>
         </el-table-column>
         <el-table-column label="反馈标题" align="center" prop="title" show-overflow-tooltip />
         <el-table-column label="联系方式" align="center" prop="contact" width="150" />
         <el-table-column label="处理状态" align="center" prop="status" width="100">
            <template #default="scope">
               <el-tag v-if="scope.row.status === '0'" type="warning">待处理</el-tag>
               <el-tag v-else-if="scope.row.status === '1'" type="primary">处理中</el-tag>
               <el-tag v-else type="success">已处理</el-tag>
            </template>
         </el-table-column>
         <el-table-column label="回复人" align="center" prop="replyBy" width="100" />
         <el-table-column label="回复时间" align="center" prop="replyTime" width="160">
            <template #default="scope">
               <span>{{ parseTime(scope.row.replyTime) }}</span>
            </template>
         </el-table-column>
         <el-table-column label="创建时间" align="center" prop="createTime" width="160">
            <template #default="scope">
               <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
         </el-table-column>
         <el-table-column label="操作" width="280" align="center" class-name="small-padding fixed-width">
            <template #default="scope">
               <el-button link type="primary" icon="View" @click="handleView(scope.row)" v-hasPermi="['core:feedback:query']">详情</el-button>
               <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['core:feedback:edit']">修改</el-button>
               <el-button link type="success" icon="ChatDotRound" @click="handleReply(scope.row)" v-hasPermi="['core:feedback:reply']">回复</el-button>
               <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['core:feedback:remove']">删除</el-button>
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

      <!-- 添加或修改意见反馈对话框 -->
      <el-dialog :title="title" v-model="open" width="700px" append-to-body>
         <el-form ref="feedbackRef" :model="form" :rules="rules" label-width="100px">
            <el-row>
               <el-col :span="12">
                  <el-form-item label="会员ID" prop="memberId">
                     <el-input v-model="form.memberId" placeholder="请输入会员ID" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item label="会员名称" prop="memberName">
                     <el-input v-model="form.memberName" placeholder="请输入会员名称" />
                  </el-form-item>
               </el-col>
            </el-row>
            <el-row>
               <el-col :span="12">
                  <el-form-item label="反馈类型" prop="type">
                     <el-select v-model="form.type" placeholder="请选择反馈类型" style="width: 100%">
                        <el-option label="功能异常" value="bug" />
                        <el-option label="功能建议" value="feature" />
                        <el-option label="其他" value="other" />
                     </el-select>
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item label="处理状态" prop="status">
                     <el-select v-model="form.status" placeholder="请选择处理状态" style="width: 100%">
                        <el-option label="待处理" value="0" />
                        <el-option label="处理中" value="1" />
                        <el-option label="已处理" value="2" />
                     </el-select>
                  </el-form-item>
               </el-col>
            </el-row>
            <el-form-item label="反馈标题" prop="title">
               <el-input v-model="form.title" placeholder="请输入反馈标题" />
            </el-form-item>
            <el-form-item label="联系方式" prop="contact">
               <el-input v-model="form.contact" placeholder="请输入联系方式（邮箱/手机号）" />
            </el-form-item>
            <el-form-item label="反馈内容" prop="content">
               <el-input v-model="form.content" type="textarea" :rows="5" placeholder="请输入反馈内容" />
            </el-form-item>
            <el-form-item label="备注" prop="remark">
               <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
            </el-form-item>
         </el-form>
         <template #footer>
            <div class="dialog-footer">
               <el-button type="primary" @click="submitForm">确 定</el-button>
               <el-button @click="cancel">取 消</el-button>
            </div>
         </template>
      </el-dialog>

      <!-- 意见反馈详情对话框 -->
      <el-dialog title="意见反馈详情" v-model="detailOpen" width="700px" append-to-body>
         <el-descriptions :column="2" border v-if="detailData">
            <el-descriptions-item label="反馈ID" :span="1">{{ detailData.id }}</el-descriptions-item>
            <el-descriptions-item label="会员名称" :span="1">{{ detailData.memberName || '-' }}</el-descriptions-item>
            <el-descriptions-item label="反馈类型" :span="1">
               <el-tag v-if="detailData.type === 'bug'" type="danger">功能异常</el-tag>
               <el-tag v-else-if="detailData.type === 'feature'" type="primary">功能建议</el-tag>
               <el-tag v-else type="info">其他</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="处理状态" :span="1">
               <el-tag v-if="detailData.status === '0'" type="warning">待处理</el-tag>
               <el-tag v-else-if="detailData.status === '1'" type="primary">处理中</el-tag>
               <el-tag v-else type="success">已处理</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="反馈标题" :span="2">{{ detailData.title }}</el-descriptions-item>
            <el-descriptions-item label="联系方式" :span="2">{{ detailData.contact || '-' }}</el-descriptions-item>
            <el-descriptions-item label="反馈内容" :span="2">{{ detailData.content }}</el-descriptions-item>
            <el-descriptions-item label="回复内容" :span="2">
               <span v-if="detailData.reply">{{ detailData.reply }}</span>
               <span v-else class="text-gray">暂无回复</span>
            </el-descriptions-item>
            <el-descriptions-item label="回复人" :span="1">{{ detailData.replyBy || '-' }}</el-descriptions-item>
            <el-descriptions-item label="回复时间" :span="1">{{ parseTime(detailData.replyTime) || '-' }}</el-descriptions-item>
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

      <!-- 回复意见反馈对话框 -->
      <el-dialog title="回复意见反馈" v-model="replyOpen" width="600px" append-to-body>
         <el-form ref="replyRef" :model="replyForm" :rules="replyRules" label-width="80px">
            <el-form-item label="反馈标题">
               <el-input v-model="replyForm.title" disabled />
            </el-form-item>
            <el-form-item label="反馈内容">
               <el-input v-model="replyForm.content" type="textarea" :rows="3" disabled />
            </el-form-item>
            <el-form-item label="回复内容" prop="reply">
               <el-input v-model="replyForm.reply" type="textarea" :rows="5" placeholder="请输入回复内容" />
            </el-form-item>
         </el-form>
         <template #footer>
            <div class="dialog-footer">
               <el-button type="primary" @click="submitReply">确 定</el-button>
               <el-button @click="cancelReply">取 消</el-button>
            </div>
         </template>
      </el-dialog>
   </div>
</template>

<script setup name="Feedback">
import { listFeedback, getFeedback, addFeedback, updateFeedback, replyFeedback, delFeedback, batchDelFeedback } from "@/api/core/feedback"

const { proxy } = getCurrentInstance()

const feedbackList = ref([])
const open = ref(false)
const detailOpen = ref(false)
const replyOpen = ref(false)
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
    memberName: undefined,
    type: undefined,
    title: undefined,
    status: undefined
  },
  replyForm: {},
  rules: {
    type: [{ required: true, message: "反馈类型不能为空", trigger: "change" }],
    title: [{ required: true, message: "反馈标题不能为空", trigger: "blur" }],
    content: [{ required: true, message: "反馈内容不能为空", trigger: "blur" }]
  },
  replyRules: {
    reply: [{ required: true, message: "回复内容不能为空", trigger: "blur" }]
  }
})

const { queryParams, form, replyForm, rules, replyRules } = toRefs(data)

/** 查询意见反馈列表 */
function getList() {
  loading.value = true
  listFeedback(queryParams.value).then(response => {
    feedbackList.value = response.data
    total.value = response.total
    loading.value = false
  })
}

/** 取消按钮 */
function cancel() {
  open.value = false
  reset()
}

/** 表单重置 */
function reset() {
  form.value = {
    id: undefined,
    memberId: undefined,
    memberName: undefined,
    type: undefined,
    title: undefined,
    content: undefined,
    contact: undefined,
    status: "0",
    reply: undefined,
    replyTime: undefined,
    replyBy: undefined,
    remark: undefined
  }
  proxy.resetForm("feedbackRef")
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef")
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
  title.value = "添加意见反馈"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const id = row.id || ids.value
  getFeedback(id).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改意见反馈"
  })
}

/** 查看详情按钮操作 */
function handleView(row) {
  const id = row.id
  getFeedback(id).then(response => {
    detailData.value = response.data
    detailOpen.value = true
  })
}

/** 回复按钮操作 */
function handleReply(row) {
  replyForm.value = {
    id: row.id,
    title: row.title,
    content: row.content,
    reply: row.reply || ''
  }
  replyOpen.value = true
}

/** 取消回复 */
function cancelReply() {
  replyOpen.value = false
  replyForm.value = {}
}

/** 提交回复 */
function submitReply() {
  proxy.$refs["replyRef"].validate(valid => {
    if (valid) {
      replyFeedback(replyForm.value).then(() => {
        proxy.$modal.msgSuccess("回复成功")
        replyOpen.value = false
        getList()
      })
    }
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["feedbackRef"].validate(valid => {
    if (valid) {
      if (form.value.id != undefined) {
        updateFeedback(form.value).then(() => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addFeedback(form.value).then(() => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}

/** 删除按钮操作 */
function handleDelete(row) {
  const feedbackIds = row.id ? [row.id] : ids.value
  proxy.$modal.confirm('是否确认删除反馈编号为"' + feedbackIds + '"的数据项？').then(function() {
    if (row.id) {
      return delFeedback(row.id)
    } else {
      return batchDelFeedback(feedbackIds)
    }
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download("core/feedback/export", {
    ...queryParams.value
  }, `feedback_${new Date().getTime()}.xlsx`)
}

getList()
</script>

<style scoped>
.text-gray {
  color: #999;
}
</style>
