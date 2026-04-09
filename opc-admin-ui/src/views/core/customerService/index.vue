<template>
   <div class="app-container">
      <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
         <el-form-item label="客服名称" prop="serviceName">
            <el-input
               v-model="queryParams.serviceName"
               placeholder="请输入客服名称"
               clearable
               style="width: 200px"
               @keyup.enter="handleQuery"
            />
         </el-form-item>
         <el-form-item label="微信号" prop="wechatId">
            <el-input
               v-model="queryParams.wechatId"
               placeholder="请输入微信号"
               clearable
               style="width: 200px"
               @keyup.enter="handleQuery"
            />
         </el-form-item>
         <el-form-item label="是否默认" prop="isDefault">
            <el-select v-model="queryParams.isDefault" placeholder="全部" clearable style="width: 120px">
               <el-option label="是" value="0" />
               <el-option label="否" value="1" />
            </el-select>
         </el-form-item>
         <el-form-item label="状态" prop="status">
            <el-select v-model="queryParams.status" placeholder="全部" clearable style="width: 120px">
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
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['core:customerService:add']">新增</el-button>
         </el-col>
         <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['core:customerService:remove']">删除</el-button>
         </el-col>
         <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="customerServiceList" @selection-change="handleSelectionChange">
         <el-table-column type="selection" width="55" align="center" />
         <el-table-column label="客服ID" align="center" prop="id" width="80" />
         <el-table-column label="客服名称" align="center" prop="serviceName" :show-overflow-tooltip="true" />
         <el-table-column label="客服二维码" align="center" prop="qrCodeUrl" width="120">
            <template #default="scope">
               <el-image 
                  v-if="scope.row.qrCodeUrl" 
                  :src="getImageUrl(scope.row.qrCodeUrl)" 
                  style="width: 60px; height: 60px; cursor: pointer"
                  fit="cover"
                  :preview-src-list="[getImageUrl(scope.row.qrCodeUrl)]"
                  preview-teleported
               />
               <span v-else>-</span>
            </template>
         </el-table-column>
         <el-table-column label="微信号" align="center" prop="wechatId" :show-overflow-tooltip="true" />
         <el-table-column label="客服电话" align="center" prop="phone" :show-overflow-tooltip="true" />
         <el-table-column label="是否默认" align="center" prop="isDefault" width="100">
            <template #default="scope">
               <el-tag :type="scope.row.isDefault === '0' ? 'success' : 'info'">
                  {{ scope.row.isDefault === '0' ? '是' : '否' }}
               </el-tag>
            </template>
         </el-table-column>
         <el-table-column label="排序号" align="center" prop="sortOrder" width="80" />
         <el-table-column label="状态" align="center" prop="status" width="80">
            <template #default="scope">
               <el-switch
                  v-model="scope.row.status"
                  active-value="0"
                  inactive-value="1"
                  @change="handleStatusChange(scope.row)"
                  v-hasPermi="['core:customerService:edit']"
               />
            </template>
         </el-table-column>
         <el-table-column label="创建时间" align="center" prop="createTime" width="160">
            <template #default="scope">
               <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
         </el-table-column>
         <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
            <template #default="scope">
               <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['core:customerService:edit']">修改</el-button>
               <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['core:customerService:remove']">删除</el-button>
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

      <!-- 添加或修改客服配置对话框 -->
      <el-dialog :title="title" v-model="open" width="600px" append-to-body>
         <el-form ref="customerServiceRef" :model="form" :rules="rules" label-width="120px">
            <el-form-item label="客服名称" prop="serviceName">
               <el-input v-model="form.serviceName" placeholder="请输入客服名称" />
            </el-form-item>
            <el-form-item label="客服二维码" prop="qrCodeUrl">
               <ImageUpload v-model="form.qrCodeUrl" :limit="1" />
               <div class="form-tip">建议上传正方形图片，尺寸200x200像素以上</div>
            </el-form-item>
            <el-form-item label="微信号" prop="wechatId">
               <el-input v-model="form.wechatId" placeholder="请输入微信号" />
            </el-form-item>
            <el-form-item label="客服电话" prop="phone">
               <el-input v-model="form.phone" placeholder="请输入客服电话" />
            </el-form-item>
            <el-form-item label="是否默认" prop="isDefault">
               <el-radio-group v-model="form.isDefault">
                  <el-radio label="0">是</el-radio>
                  <el-radio label="1">否</el-radio>
               </el-radio-group>
               <div class="form-tip">设置为默认后，其他客服将自动取消默认</div>
            </el-form-item>
            <el-form-item label="排序号" prop="sortOrder">
               <el-input-number v-model="form.sortOrder" :min="0" :max="999" placeholder="请输入排序号" style="width: 200px" />
               <span class="form-tip">数字越小排序越靠前</span>
            </el-form-item>
            <el-form-item label="状态" prop="status">
               <el-radio-group v-model="form.status">
                  <el-radio label="0">启用</el-radio>
                  <el-radio label="1">禁用</el-radio>
               </el-radio-group>
            </el-form-item>
            <el-form-item label="备注" prop="remark">
               <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注" />
            </el-form-item>
         </el-form>
         <template #footer>
            <div class="dialog-footer">
               <el-button type="primary" @click="submitForm">确 定</el-button>
               <el-button @click="cancel">取 消</el-button>
            </div>
         </template>
      </el-dialog>
   </div>
</template>

<script setup name="CustomerService">
import { listCustomerService, getCustomerService, addCustomerService, updateCustomerService, delCustomerService } from "@/api/core/customerService"
import ImageUpload from "@/components/ImageUpload/index.vue"
import { isExternal } from "@/utils/validate"

const { proxy } = getCurrentInstance()
const baseUrl = import.meta.env.VITE_APP_BASE_API

const customerServiceList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")

const data = reactive({
  form: {
    id: undefined,
    serviceName: undefined,
    qrCodeUrl: undefined,
    wechatId: undefined,
    phone: undefined,
    isDefault: "1",
    sortOrder: 0,
    status: "0",
    remark: undefined
  },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    serviceName: undefined,
    wechatId: undefined,
    isDefault: undefined,
    status: undefined
  },
  rules: {
    serviceName: [{ required: true, message: "客服名称不能为空", trigger: "blur" }],
    qrCodeUrl: [{ required: true, message: "请上传客服二维码", trigger: "blur" }],
    isDefault: [{ required: true, message: "请选择是否默认", trigger: "change" }],
    status: [{ required: true, message: "请选择状态", trigger: "change" }]
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 获取图片完整URL */
function getImageUrl(url) {
  if (!url) return ''
  if (isExternal(url)) return url
  return baseUrl + url
}

/** 查询客服配置列表 */
function getList() {
  loading.value = true
  listCustomerService(queryParams.value).then(response => {
    customerServiceList.value = response.data
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
    serviceName: undefined,
    qrCodeUrl: undefined,
    wechatId: undefined,
    phone: undefined,
    isDefault: "1",
    sortOrder: 0,
    status: "0",
    remark: undefined
  }
  proxy.resetForm("customerServiceRef")
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
  title.value = "添加客服配置"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const id = row.id || ids.value
  getCustomerService(id).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改客服配置"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["customerServiceRef"].validate(valid => {
    if (valid) {
      if (form.value.id != undefined) {
        updateCustomerService(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addCustomerService(form.value).then(response => {
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
  const customerServiceIds = row.id || ids.value
  proxy.$modal.confirm('是否确认删除客服编号为"' + customerServiceIds + '"的数据项？').then(function() {
    return delCustomerService(customerServiceIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 状态修改 */
function handleStatusChange(row) {
  const text = row.status === "0" ? "启用" : "停用"
  proxy.$modal.confirm('确认要"' + text + '""' + row.serviceName + '"客服吗？').then(function() {
    return updateCustomerService({ id: row.id, status: row.status })
  }).then(() => {
    proxy.$modal.msgSuccess(text + "成功")
  }).catch(function() {
    row.status = row.status === "0" ? "1" : "0"
  })
}

getList()
</script>

<style scoped>
.form-tip {
  margin-left: 10px;
  color: #909399;
  font-size: 12px;
}
</style>
