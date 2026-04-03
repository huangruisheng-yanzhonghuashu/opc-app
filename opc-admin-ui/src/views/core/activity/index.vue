<template>
   <div class="app-container">
      <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
         <el-form-item label="活动名称" prop="activityName">
            <el-input
               v-model="queryParams.activityName"
               placeholder="请输入活动名称"
               clearable
               style="width: 200px"
               @keyup.enter="handleQuery"
            />
         </el-form-item>
         <el-form-item label="省份" prop="province">
            <el-input
               v-model="queryParams.province"
               placeholder="请输入省份"
               clearable
               style="width: 150px"
               @keyup.enter="handleQuery"
            />
         </el-form-item>
         <el-form-item label="城市" prop="city">
            <el-input
               v-model="queryParams.city"
               placeholder="请输入城市"
               clearable
               style="width: 150px"
               @keyup.enter="handleQuery"
            />
         </el-form-item>
         <el-form-item label="状态" prop="status">
            <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 150px">
               <el-option label="正常" value="0" />
               <el-option label="停用" value="1" />
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
               v-hasPermi="['core:activity:add']"
            >新增</el-button>
         </el-col>
         <el-col :span="1.5">
            <el-button
               type="success"
               plain
               icon="Edit"
               :disabled="single"
               @click="handleUpdate"
               v-hasPermi="['core:activity:edit']"
            >修改</el-button>
         </el-col>
         <el-col :span="1.5">
            <el-button
               type="danger"
               plain
               icon="Delete"
               :disabled="multiple"
               @click="handleDelete"
               v-hasPermi="['core:activity:remove']"
            >删除</el-button>
         </el-col>
         <el-col :span="1.5">
            <el-button
               type="warning"
               plain
               icon="Download"
               @click="handleExport"
               v-hasPermi="['core:activity:export']"
            >导出</el-button>
         </el-col>
         <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="activityList" @selection-change="handleSelectionChange">
         <el-table-column type="selection" width="55" align="center" />
         <el-table-column label="活动ID" align="center" prop="id" width="80" />
         <el-table-column label="活动海报" align="center" prop="posterUrl" width="120">
            <template #default="scope">
               <el-image
                  v-if="scope.row.posterUrl"
                  :src="scope.row.posterUrl"
                  :preview-src-list="[scope.row.posterUrl]"
                  fit="cover"
                  style="width: 80px; height: 60px"
               />
               <span v-else>-</span>
            </template>
         </el-table-column>
         <el-table-column label="活动名称" align="center" prop="activityName" :show-overflow-tooltip="true" />
         <el-table-column label="组织者" align="center" width="120">
            <template #default="scope">
               <div class="organizer-info">
                  <el-avatar v-if="scope.row.organizerAvatar" :size="30" :src="scope.row.organizerAvatar" />
                  <el-avatar v-else :size="30" icon="UserFilled" />
                  <span class="organizer-name">{{ scope.row.organizerName || '-' }}</span>
               </div>
            </template>
         </el-table-column>
         <el-table-column label="活动时间" align="center" prop="activityTime" width="160">
            <template #default="scope">
               <span>{{ parseTime(scope.row.activityTime) }}</span>
            </template>
         </el-table-column>
         <el-table-column label="活动地点" align="center" :show-overflow-tooltip="true">
            <template #default="scope">
               <span>{{ formatAddress(scope.row) }}</span>
            </template>
         </el-table-column>
         <el-table-column label="人数" align="center" width="100">
            <template #default="scope">
               <span>{{ scope.row.registeredCount || 0 }} / {{ scope.row.totalCapacity || 0 }}</span>
            </template>
         </el-table-column>
         <el-table-column label="报名费用" align="center" prop="registrationFee" width="100">
            <template #default="scope">
               <span>¥{{ scope.row.registrationFee || '0.00' }}</span>
            </template>
         </el-table-column>
         <el-table-column label="状态" align="center" prop="status" width="80">
            <template #default="scope">
               <el-switch
                  v-model="scope.row.status"
                  active-value="0"
                  inactive-value="1"
                  @change="handleStatusChange(scope.row)"
                  v-hasPermi="['core:activity:changeStatus']"
               ></el-switch>
            </template>
         </el-table-column>
         <el-table-column label="创建时间" align="center" prop="createTime" width="160">
            <template #default="scope">
               <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
         </el-table-column>
         <el-table-column label="操作" width="200" align="center" class-name="small-padding fixed-width">
            <template #default="scope">
               <el-button link type="primary" icon="View" @click="handleView(scope.row)" v-hasPermi="['core:activity:query']">查看</el-button>
               <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['core:activity:edit']">编辑</el-button>
               <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['core:activity:remove']">删除</el-button>
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

      <!-- 添加或修改活动对话框 -->
      <el-dialog :title="title" v-model="open" width="800px" append-to-body>
         <el-form ref="activityRef" :model="form" :rules="rules" label-width="100px">
            <el-row :gutter="20">
               <el-col :span="12">
                  <el-form-item label="活动名称" prop="activityName">
                     <el-input v-model="form.activityName" placeholder="请输入活动名称" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item label="活动时间" prop="activityTime">
                     <el-date-picker
                        v-model="form.activityTime"
                        type="datetime"
                        placeholder="选择活动时间"
                        value-format="YYYY-MM-DD HH:mm:ss"
                        style="width: 100%"
                     />
                  </el-form-item>
               </el-col>
            </el-row>
            <el-row :gutter="20">
               <el-col :span="12">
                  <el-form-item label="组织者名称" prop="organizerName">
                     <el-input v-model="form.organizerName" placeholder="请输入组织者名称" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item label="组织者头像" prop="organizerAvatar">
                     <el-upload
                        class="avatar-uploader-small"
                        :action="uploadAction"
                        :headers="uploadHeaders"
                        :show-file-list="false"
                        :on-success="handleAvatarUploadSuccess"
                        :on-error="handleUploadError"
                        :before-upload="beforeAvatarUpload"
                     >
                        <img v-if="form.organizerAvatar" :src="form.organizerAvatar" class="avatar-small" />
                        <el-icon v-else class="avatar-uploader-icon-small"><Plus /></el-icon>
                     </el-upload>
                  </el-form-item>
               </el-col>
            </el-row>
            <el-row :gutter="20">
               <el-col :span="12">
                  <el-form-item label="省份" prop="province">
                     <el-input v-model="form.province" placeholder="请输入省份" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item label="城市" prop="city">
                     <el-input v-model="form.city" placeholder="请输入城市" />
                  </el-form-item>
               </el-col>
            </el-row>
            <el-form-item label="详细地址" prop="address">
               <el-input v-model="form.address" placeholder="请输入详细地址" />
            </el-form-item>
            <el-row :gutter="20">
               <el-col :span="12">
                  <el-form-item label="总人数" prop="totalCapacity">
                     <el-input-number v-model="form.totalCapacity" :min="0" placeholder="请输入总人数" style="width: 100%" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item label="报名费用" prop="registrationFee">
                     <el-input-number v-model="form.registrationFee" :min="0" :precision="2" placeholder="请输入报名费用" style="width: 100%" />
                  </el-form-item>
               </el-col>
            </el-row>
            <el-form-item label="活动海报" prop="posterUrl">
               <el-upload
                  class="avatar-uploader"
                  :action="uploadAction"
                  :headers="uploadHeaders"
                  :show-file-list="false"
                  :on-success="handlePosterUploadSuccess"
                  :on-error="handleUploadError"
                  :before-upload="beforeUpload"
               >
                  <img v-if="form.posterUrl" :src="form.posterUrl" class="avatar" />
                  <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
               </el-upload>
            </el-form-item>
            <el-form-item label="活动详情" prop="activityDetail">
               <Editor v-model="form.activityDetail" :min-height="300" />
            </el-form-item>
            <el-form-item label="状态" prop="status">
               <el-radio-group v-model="form.status">
                  <el-radio label="0">正常</el-radio>
                  <el-radio label="1">停用</el-radio>
               </el-radio-group>
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

      <!-- 活动详情对话框 -->
      <el-dialog title="活动详情" v-model="detailOpen" width="800px" append-to-body>
         <el-descriptions :column="2" border v-if="detailData">
            <el-descriptions-item label="活动ID" :span="1">{{ detailData.id }}</el-descriptions-item>
            <el-descriptions-item label="活动名称" :span="1">{{ detailData.activityName || '-' }}</el-descriptions-item>
            <el-descriptions-item label="活动海报" :span="2">
               <el-image
                  v-if="detailData.posterUrl"
                  :src="detailData.posterUrl"
                  :preview-src-list="[detailData.posterUrl]"
                  fit="cover"
                  style="width: 200px; height: 150px"
               />
               <span v-else>-</span>
            </el-descriptions-item>
            <el-descriptions-item label="组织者" :span="1">
               <div class="organizer-info">
                  <el-avatar v-if="detailData.organizerAvatar" :size="40" :src="detailData.organizerAvatar" />
                  <el-avatar v-else :size="40" icon="UserFilled" />
                  <span class="organizer-name">{{ detailData.organizerName || '-' }}</span>
               </div>
            </el-descriptions-item>
            <el-descriptions-item label="活动时间" :span="1">{{ parseTime(detailData.activityTime) || '-' }}</el-descriptions-item>
            <el-descriptions-item label="活动地点" :span="2">{{ formatAddress(detailData) || '-' }}</el-descriptions-item>
            <el-descriptions-item label="总人数" :span="1">{{ detailData.totalCapacity || 0 }} 人</el-descriptions-item>
            <el-descriptions-item label="已报名" :span="1">{{ detailData.registeredCount || 0 }} 人</el-descriptions-item>
            <el-descriptions-item label="报名费用" :span="1">¥{{ detailData.registrationFee || '0.00' }}</el-descriptions-item>
            <el-descriptions-item label="状态" :span="1">
               <el-tag :type="detailData.status === '0' ? 'success' : 'danger'">
                  {{ detailData.status === '0' ? '正常' : '停用' }}
               </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="活动详情" :span="2">
               <div class="activity-detail" v-html="detailData.activityDetail || '-'"></div>
            </el-descriptions-item>
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

<script setup name="Activity">
import { listActivity, getActivity, addActivity, updateActivity, delActivity, changeActivityStatus } from "@/api/core/activity"
import { getToken } from "@/utils/auth"

const { proxy } = getCurrentInstance()

// 上传相关配置
const uploadAction = ref(import.meta.env.VITE_APP_BASE_API + "/common/uploadToServer")
const uploadHeaders = ref({ Authorization: "Bearer " + getToken() })

const activityList = ref([])
const open = ref(false)
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
    activityName: undefined,
    province: undefined,
    city: undefined,
    status: undefined
  },
  rules: {
    activityName: [{ required: true, message: "活动名称不能为空", trigger: "blur" }],
    activityTime: [{ required: true, message: "活动时间不能为空", trigger: "change" }],
    totalCapacity: [{ required: true, message: "总人数不能为空", trigger: "blur" }],
    registrationFee: [{ required: true, message: "报名费用不能为空", trigger: "blur" }]
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询活动列表 */
function getList() {
  loading.value = true
  listActivity(queryParams.value).then(response => {
    activityList.value = response.data
    total.value = response.total
    loading.value = false
  })
}

/** 格式化地址 */
function formatAddress(row) {
  const parts = []
  if (row.province) parts.push(row.province)
  if (row.city) parts.push(row.city)
  if (row.address) parts.push(row.address)
  return parts.join(' ') || '-'
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
    activityName: undefined,
    posterUrl: undefined,
    organizerName: undefined,
    organizerAvatar: undefined,
    activityTime: undefined,
    province: undefined,
    city: undefined,
    address: undefined,
    totalCapacity: 0,
    registeredCount: 0,
    registrationFee: 0.00,
    activityDetail: undefined,
    status: '0',
    remark: undefined
  }
  proxy.resetForm("activityRef")
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
  single.value = selection.length !== 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加活动"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const id = row.id || ids.value
  getActivity(id).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改活动"
  })
}

/** 查看详情按钮操作 */
function handleView(row) {
  const id = row.id
  getActivity(id).then(response => {
    detailData.value = response.data
    detailOpen.value = true
  })
}

/** 删除按钮操作 */
function handleDelete(row) {
  const activityIds = row.id || ids.value
  proxy.$modal.confirm('是否确认删除活动编号为"' + activityIds + '"的数据项？').then(() => {
    return delActivity(activityIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 状态修改 */
function handleStatusChange(row) {
  const text = row.status === "0" ? "启用" : "停用"
  proxy.$modal.confirm('确认要' + text + '"' + row.activityName + '"活动吗？').then(() => {
    return changeActivityStatus(row.id, row.status)
  }).then(() => {
    proxy.$modal.msgSuccess(text + "成功")
  }).catch(() => {
    row.status = row.status === "0" ? "1" : "0"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["activityRef"].validate(valid => {
    if (valid) {
      if (form.value.id != undefined) {
        updateActivity(form.value).then(() => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addActivity(form.value).then(() => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download("core/activity/export", {
    ...queryParams.value
  }, `activity_${new Date().getTime()}.xlsx`)
}

/** 海报上传成功回调 */
function handlePosterUploadSuccess(response) {
  if (response.code === 200) {
    form.value.posterUrl = response.url
    proxy.$modal.msgSuccess("上传成功")
  } else {
    proxy.$modal.msgError(response.msg || "上传失败")
  }
}

/** 头像上传成功回调 */
function handleAvatarUploadSuccess(response) {
  if (response.code === 200) {
    form.value.organizerAvatar = response.url
    proxy.$modal.msgSuccess("上传成功")
  } else {
    proxy.$modal.msgError(response.msg || "上传失败")
  }
}

/** 上传失败回调 */
function handleUploadError() {
  proxy.$modal.msgError("上传失败")
}

/** 海报上传前校验 */
function beforeUpload(file) {
  const isJPG = file.type === "image/jpeg"
  const isPNG = file.type === "image/png"
  const isGIF = file.type === "image/gif"
  const isLt5M = file.size / 1024 / 1024 < 5

  if (!isJPG && !isPNG && !isGIF) {
    proxy.$modal.msgError("请上传 JPG/PNG/GIF 格式的图片!")
    return false
  }
  if (!isLt5M) {
    proxy.$modal.msgError("图片大小不能超过 5MB!")
    return false
  }
  return true
}

/** 头像上传前校验 */
function beforeAvatarUpload(file) {
  const isJPG = file.type === "image/jpeg"
  const isPNG = file.type === "image/png"
  const isGIF = file.type === "image/gif"
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isJPG && !isPNG && !isGIF) {
    proxy.$modal.msgError("请上传 JPG/PNG/GIF 格式的图片!")
    return false
  }
  if (!isLt2M) {
    proxy.$modal.msgError("头像大小不能超过 2MB!")
    return false
  }
  return true
}

getList()
</script>

<style scoped>
.organizer-info {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}
.organizer-name {
  font-size: 14px;
}
.avatar-uploader {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
  width: 300px;
  height: 180px;
}
.avatar-uploader:hover {
  border-color: var(--el-color-primary);
}
.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 300px;
  height: 180px;
  text-align: center;
  display: flex;
  align-items: center;
  justify-content: center;
}
.avatar {
  width: 300px;
  height: 180px;
  display: block;
  object-fit: cover;
}
.activity-detail {
  max-height: 300px;
  overflow-y: auto;
  padding: 10px;
  background-color: #f5f7fa;
  border-radius: 4px;
}
.avatar-uploader-small {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
  width: 80px;
  height: 80px;
}
.avatar-uploader-small:hover {
  border-color: var(--el-color-primary);
}
.el-icon.avatar-uploader-icon-small {
  font-size: 20px;
  color: #8c939d;
  width: 80px;
  height: 80px;
  text-align: center;
  display: flex;
  align-items: center;
  justify-content: center;
}
.avatar-small {
  width: 80px;
  height: 80px;
  display: block;
  object-fit: cover;
}
</style>
