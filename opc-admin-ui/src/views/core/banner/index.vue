<template>
   <div class="app-container">
      <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
         <el-form-item label="标题" prop="title">
            <el-input
               v-model="queryParams.title"
               placeholder="请输入标题"
               clearable
               style="width: 200px"
               @keyup.enter="handleQuery"
            />
         </el-form-item>
         <el-form-item label="状态" prop="status">
            <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 200px">
               <el-option label="启用" value="0" />
               <el-option label="禁用" value="1" />
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
               v-hasPermi="['core:banner:add']"
            >新增</el-button>
         </el-col>
         <el-col :span="1.5">
            <el-button
               type="success"
               plain
               icon="Edit"
               :disabled="single"
               @click="handleUpdate"
               v-hasPermi="['core:banner:edit']"
            >修改</el-button>
         </el-col>
         <el-col :span="1.5">
            <el-button
               type="danger"
               plain
               icon="Delete"
               :disabled="multiple"
               @click="handleDelete"
               v-hasPermi="['core:banner:remove']"
            >删除</el-button>
         </el-col>
         <el-col :span="1.5">
            <el-button
               type="warning"
               plain
               icon="Download"
               @click="handleExport"
               v-hasPermi="['core:banner:export']"
            >导出</el-button>
         </el-col>
         <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="bannerList" @selection-change="handleSelectionChange">
         <el-table-column type="selection" width="55" align="center" />
         <el-table-column label="BannerID" align="center" prop="id" width="80" />
         <el-table-column label="标题" align="center" prop="title" />
         <el-table-column label="图片" align="center" prop="imageUrl" width="120">
            <template #default="scope">
               <el-image
                  v-if="scope.row.imageUrl"
                  :src="scope.row.imageUrl"
                  :preview-src-list="[scope.row.imageUrl]"
                  fit="cover"
                  style="width: 60px; height: 40px"
               />
               <span v-else>-</span>
            </template>
         </el-table-column>
         <el-table-column label="链接类型" align="center" prop="linkType" width="100">
            <template #default="scope">
               <el-tag :type="scope.row.linkType === '1' ? 'primary' : 'success'">
                  {{ scope.row.linkType === '1' ? '文章ID' : '外部链接' }}
               </el-tag>
            </template>
         </el-table-column>
         <el-table-column label="链接值" align="center" prop="linkValue" :show-overflow-tooltip="true" />
         <el-table-column label="排序" align="center" prop="sortOrder" width="80" />
         <el-table-column label="状态" align="center" prop="status" width="80">
            <template #default="scope">
               <el-switch
                  v-model="scope.row.status"
                  active-value="0"
                  inactive-value="1"
                  @change="handleStatusChange(scope.row)"
                  v-hasPermi="['core:banner:changeStatus']"
               ></el-switch>
            </template>
         </el-table-column>
         <el-table-column label="创建时间" align="center" prop="createTime" width="160">
            <template #default="scope">
               <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
         </el-table-column>
         <el-table-column label="操作" width="180" align="center" class-name="small-padding fixed-width">
            <template #default="scope">
               <el-button link type="primary" icon="View" @click="handleView(scope.row)" v-hasPermi="['core:banner:query']">详情</el-button>
               <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['core:banner:edit']">修改</el-button>
               <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['core:banner:remove']">删除</el-button>
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

      <!-- 添加或修改Banner对话框 -->
      <el-dialog :title="title" v-model="open" width="600px" append-to-body>
         <el-form ref="bannerRef" :model="form" :rules="rules" label-width="100px">
            <el-form-item label="标题" prop="title">
               <el-input v-model="form.title" placeholder="请输入标题" />
            </el-form-item>
            <el-form-item label="图片" prop="imageUrl">
               <el-upload
                  class="avatar-uploader"
                  :action="uploadAction"
                  :headers="uploadHeaders"
                  :show-file-list="false"
                  :on-success="handleUploadSuccess"
                  :on-error="handleUploadError"
                  :before-upload="beforeUpload"
               >
                  <img v-if="form.imageUrl" :src="form.imageUrl" class="avatar" />
                  <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
               </el-upload>
            </el-form-item>
            <el-form-item label="链接类型" prop="linkType">
               <el-radio-group v-model="form.linkType">
                  <el-radio label="1">文章ID</el-radio>
                  <el-radio label="2">外部链接</el-radio>
               </el-radio-group>
            </el-form-item>
            <el-form-item label="链接值" prop="linkValue">
               <el-input v-model="form.linkValue" placeholder="请输入链接值" />
            </el-form-item>
            <el-form-item label="排序" prop="sortOrder">
               <el-input-number v-model="form.sortOrder" :min="0" placeholder="请输入排序数字" />
            </el-form-item>
            <el-form-item label="状态" prop="status">
               <el-radio-group v-model="form.status">
                  <el-radio label="0">启用</el-radio>
                  <el-radio label="1">禁用</el-radio>
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

      <!-- Banner详情对话框 -->
      <el-dialog title="Banner详情" v-model="detailOpen" width="600px" append-to-body>
         <el-descriptions :column="2" border v-if="detailData">
            <el-descriptions-item label="BannerID" :span="1">{{ detailData.id }}</el-descriptions-item>
            <el-descriptions-item label="标题" :span="1">{{ detailData.title || '-' }}</el-descriptions-item>
            <el-descriptions-item label="图片" :span="2">
               <el-image
                  v-if="detailData.imageUrl"
                  :src="detailData.imageUrl"
                  :preview-src-list="[detailData.imageUrl]"
                  fit="cover"
                  style="width: 200px; height: 120px"
               />
               <span v-else>-</span>
            </el-descriptions-item>
            <el-descriptions-item label="链接类型" :span="1">
               <el-tag :type="detailData.linkType === '1' ? 'primary' : 'success'">
                  {{ detailData.linkType === '1' ? '文章ID' : '外部链接' }}
               </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="链接值" :span="1">{{ detailData.linkValue || '-' }}</el-descriptions-item>
            <el-descriptions-item label="排序" :span="1">{{ detailData.sortOrder }}</el-descriptions-item>
            <el-descriptions-item label="状态" :span="1">
               <el-tag :type="detailData.status === '0' ? 'success' : 'danger'">
                  {{ detailData.status === '0' ? '启用' : '禁用' }}
               </el-tag>
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

<script setup name="Banner">
import { listBanner, getBanner, addBanner, updateBanner, delBanner, changeBannerStatus } from "@/api/core/banner"
import { getToken } from "@/utils/auth"

const { proxy } = getCurrentInstance()

// 上传相关配置
const uploadAction = ref(import.meta.env.VITE_APP_BASE_API + "/common/upload")
const uploadHeaders = ref({ Authorization: "Bearer " + getToken() })

const bannerList = ref([])
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
    title: undefined,
    status: undefined
  },
  rules: {
    title: [{ required: true, message: "标题不能为空", trigger: "blur" }],
    imageUrl: [{ required: true, message: "图片URL不能为空", trigger: "blur" }],
    linkType: [{ required: true, message: "链接类型不能为空", trigger: "change" }],
    linkValue: [{ required: true, message: "链接值不能为空", trigger: "blur" }]
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询Banner列表 */
function getList() {
  loading.value = true
  listBanner(queryParams.value).then(response => {
    bannerList.value = response.rows
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
    title: undefined,
    imageUrl: undefined,
    linkType: '1',
    linkValue: undefined,
    sortOrder: 0,
    status: '0',
    remark: undefined
  }
  proxy.resetForm("bannerRef")
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
  title.value = "添加Banner"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const id = row.id || ids.value
  getBanner(id).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改Banner"
  })
}

/** 查看详情按钮操作 */
function handleView(row) {
  const id = row.id
  getBanner(id).then(response => {
    detailData.value = response.data
    detailOpen.value = true
  })
}

/** 删除按钮操作 */
function handleDelete(row) {
  const bannerIds = row.id || ids.value
  proxy.$modal.confirm('是否确认删除Banner编号为"' + bannerIds + '"的数据项？').then(() => {
    return delBanner(bannerIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 状态修改 */
function handleStatusChange(row) {
  const text = row.status === "0" ? "启用" : "禁用"
  proxy.$modal.confirm('确认要' + text + '"' + row.title + '"Banner吗？').then(() => {
    return changeBannerStatus(row.id, row.status)
  }).then(() => {
    proxy.$modal.msgSuccess(text + "成功")
  }).catch(() => {
    row.status = row.status === "0" ? "1" : "0"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["bannerRef"].validate(valid => {
    if (valid) {
      if (form.value.id != undefined) {
        updateBanner(form.value).then(() => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addBanner(form.value).then(() => {
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
  proxy.download("core/banner/export", {
    ...queryParams.value
  }, `banner_${new Date().getTime()}.xlsx`)
}

getList()

/** 上传成功回调 */
function handleUploadSuccess(response) {
  if (response.code === 200) {
    form.value.imageUrl = response.url
    proxy.$modal.msgSuccess("上传成功")
  } else {
    proxy.$modal.msgError(response.msg || "上传失败")
  }
}

/** 上传失败回调 */
function handleUploadError() {
  proxy.$modal.msgError("上传失败")
}

/** 上传前校验 */
function beforeUpload(file) {
  const isJPG = file.type === "image/jpeg"
  const isPNG = file.type === "image/png"
  const isGIF = file.type === "image/gif"
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isJPG && !isPNG && !isGIF) {
    proxy.$modal.msgError("请上传 JPG/PNG/GIF 格式的图片!")
    return false
  }
  if (!isLt2M) {
    proxy.$modal.msgError("图片大小不能超过 2MB!")
    return false
  }
  return true
}
</script>

<style scoped>
.avatar-uploader {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
  width: 200px;
  height: 120px;
}
.avatar-uploader:hover {
  border-color: var(--el-color-primary);
}
.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 200px;
  height: 120px;
  text-align: center;
  display: flex;
  align-items: center;
  justify-content: center;
}
.avatar {
  width: 200px;
  height: 120px;
  display: block;
  object-fit: cover;
}
</style>
