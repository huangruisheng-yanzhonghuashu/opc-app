<template>
   <div class="app-container">
      <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
         <el-form-item label="社区名" prop="name">
            <el-input
               v-model="queryParams.name"
               placeholder="请输入社区名"
               clearable
               style="width: 200px"
               @keyup.enter="handleQuery"
            />
         </el-form-item>
         <el-form-item label="社区地址" prop="address">
            <el-input
               v-model="queryParams.address"
               placeholder="请输入社区地址"
               clearable
               style="width: 200px"
               @keyup.enter="handleQuery"
            />
         </el-form-item>
         <el-form-item label="状态" prop="status">
            <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 200px">
               <el-option
                  v-for="dict in sys_normal_disable"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
               />
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
               v-hasPermi="['core:community:add']"
            >新增</el-button>
         </el-col>
         <el-col :span="1.5">
            <el-button
               type="success"
               plain
               icon="Edit"
               :disabled="single"
               @click="handleUpdate"
               v-hasPermi="['core:community:edit']"
            >修改</el-button>
         </el-col>
         <el-col :span="1.5">
            <el-button
               type="danger"
               plain
               icon="Delete"
               :disabled="multiple"
               @click="handleDelete"
               v-hasPermi="['core:community:remove']"
            >删除</el-button>
         </el-col>
         <el-col :span="1.5">
            <el-button
               type="warning"
               plain
               icon="Download"
               @click="handleExport"
               v-hasPermi="['core:community:export']"
            >导出</el-button>
         </el-col>
         <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="communityList" @selection-change="handleSelectionChange">
         <el-table-column type="selection" width="55" align="center" />
         <el-table-column label="社区ID" align="center" prop="id" width="80" />
         <el-table-column label="社区图片" align="center" prop="image" width="100">
            <template #default="scope">
               <el-image
                  v-if="scope.row.image"
                  :src="getImageUrl(scope.row.image)"
                  :preview-src-list="[getImageUrl(scope.row.image)]"
                  fit="cover"
                  style="width: 60px; height: 60px; border-radius: 4px;"
               />
               <div v-else style="width: 60px; height: 60px; background: #f0f0f0; border-radius: 4px; display: flex; align-items: center; justify-content: center; margin: 0 auto;">
                  <el-icon><Picture /></el-icon>
               </div>
            </template>
         </el-table-column>
         <el-table-column label="社区名" align="center" prop="name" :show-overflow-tooltip="true" />
         <el-table-column label="社区地址" align="center" prop="address" :show-overflow-tooltip="true" />
         <el-table-column label="经纬度" align="center" width="180">
            <template #default="scope">
               <span v-if="scope.row.longitude && scope.row.latitude">
                  {{ scope.row.longitude }}, {{ scope.row.latitude }}
               </span>
               <span v-else>-</span>
            </template>
         </el-table-column>
         <el-table-column label="想去数" align="center" prop="wantToGoCount" width="80" />
         <el-table-column label="已去过数" align="center" prop="visitedCount" width="90" />
         <el-table-column label="评价数" align="center" prop="reviewCount" width="80" />
         <el-table-column label="评价星级" align="center" width="100">
            <template #default="scope">
               <el-rate
                  v-model="scope.row.rating"
                  disabled
                  show-score
                  text-color="#ff9900"
                  score-template="{value}"
               />
            </template>
         </el-table-column>
         <el-table-column label="状态" align="center" prop="status" width="80">
            <template #default="scope">
               <dict-tag :options="sys_normal_disable" :value="scope.row.status" />
            </template>
         </el-table-column>
         <el-table-column label="排序" align="center" prop="sortOrder" width="80" />
         <el-table-column label="操作" width="200" align="center" class-name="small-padding fixed-width">
            <template #default="scope">
               <el-button link type="primary" icon="View" @click="handleView(scope.row)" v-hasPermi="['core:community:query']">详情</el-button>
               <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['core:community:edit']">修改</el-button>
               <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['core:community:remove']">删除</el-button>
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

      <!-- 添加或修改社区对话框 -->
      <el-dialog :title="title" v-model="open" width="700px" append-to-body>
         <el-form ref="communityRef" :model="form" :rules="rules" label-width="100px">
            <el-row>
               <el-col :span="12">
                  <el-form-item label="社区名" prop="name">
                     <el-input v-model="form.name" placeholder="请输入社区名" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item label="排序" prop="sortOrder">
                     <el-input-number v-model="form.sortOrder" :min="0" :max="9999" style="width: 100%" />
                  </el-form-item>
               </el-col>
            </el-row>
            <el-row>
               <el-col :span="24">
                  <el-form-item label="社区图片" prop="image">
                     <ImageUpload v-model="form.image" :limit="1" />
                  </el-form-item>
               </el-col>
            </el-row>
            <el-row>
               <el-col :span="24">
                  <el-form-item label="社区地址" prop="address">
                     <el-input v-model="form.address" placeholder="请输入社区地址" />
                  </el-form-item>
               </el-col>
            </el-row>
            <el-row>
               <el-col :span="12">
                  <el-form-item label="经度" prop="longitude">
                     <el-input v-model="form.longitude" placeholder="请输入经度" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item label="纬度" prop="latitude">
                     <el-input v-model="form.latitude" placeholder="请输入纬度" />
                  </el-form-item>
               </el-col>
            </el-row>
            <el-row>
               <el-col :span="12">
                  <el-form-item label="想去数" prop="wantToGoCount">
                     <el-input-number v-model="form.wantToGoCount" :min="0" style="width: 100%" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item label="已去过数" prop="visitedCount">
                     <el-input-number v-model="form.visitedCount" :min="0" style="width: 100%" />
                  </el-form-item>
               </el-col>
            </el-row>
            <el-row>
               <el-col :span="12">
                  <el-form-item label="评价数" prop="reviewCount">
                     <el-input-number v-model="form.reviewCount" :min="0" style="width: 100%" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item label="评价星级" prop="rating">
                     <el-rate v-model="form.rating" show-score style="height: 32px; display: flex; align-items: center;" />
                  </el-form-item>
               </el-col>
            </el-row>
            <el-row>
               <el-col :span="12">
                  <el-form-item label="状态" prop="status">
                     <el-radio-group v-model="form.status">
                        <el-radio
                           v-for="dict in sys_normal_disable"
                           :key="dict.value"
                           :value="dict.value"
                        >{{ dict.label }}</el-radio>
                     </el-radio-group>
                  </el-form-item>
               </el-col>
            </el-row>
            <el-form-item label="相关详情" prop="details">
               <el-input v-model="form.details" type="textarea" :rows="4" placeholder="请输入相关详情" />
            </el-form-item>
            <el-form-item label="备注" prop="remark">
               <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
            </el-form-item>
         </el-form>
         <template #footer>
            <div class="dialog-footer">
               <el-button type="primary" @click="submitForm">确 定</el-button>
               <el-button @click="cancel">取 消</el-button>
            </div>
         </template>
      </el-dialog>

      <!-- 社区详情对话框 -->
      <el-dialog title="社区详情" v-model="detailOpen" width="700px" append-to-body>
         <el-descriptions :column="2" border v-if="detailData">
            <el-descriptions-item label="社区ID" :span="1">{{ detailData.id }}</el-descriptions-item>
            <el-descriptions-item label="社区名" :span="1">{{ detailData.name }}</el-descriptions-item>
            <el-descriptions-item label="社区图片" :span="2">
               <el-image
                  v-if="detailData.image"
                  :src="getImageUrl(detailData.image)"
                  :preview-src-list="[getImageUrl(detailData.image)]"
                  fit="cover"
                  style="width: 120px; height: 120px; border-radius: 4px;"
               />
               <div v-else style="width: 120px; height: 120px; background: #f0f0f0; border-radius: 4px; display: flex; align-items: center; justify-content: center;">
                  <el-icon size="32"><Picture /></el-icon>
               </div>
            </el-descriptions-item>
            <el-descriptions-item label="社区地址" :span="2">{{ detailData.address || '-' }}</el-descriptions-item>
            <el-descriptions-item label="经度" :span="1">{{ detailData.longitude || '-' }}</el-descriptions-item>
            <el-descriptions-item label="纬度" :span="1">{{ detailData.latitude || '-' }}</el-descriptions-item>
            <el-descriptions-item label="想去数" :span="1">{{ detailData.wantToGoCount || 0 }}</el-descriptions-item>
            <el-descriptions-item label="已去过数" :span="1">{{ detailData.visitedCount || 0 }}</el-descriptions-item>
            <el-descriptions-item label="评价数" :span="1">{{ detailData.reviewCount || 0 }}</el-descriptions-item>
            <el-descriptions-item label="评价星级" :span="1">
               <el-rate v-model="detailData.rating" disabled show-score />
            </el-descriptions-item>
            <el-descriptions-item label="状态" :span="1">
               <dict-tag :options="sys_normal_disable" :value="detailData.status" />
            </el-descriptions-item>
            <el-descriptions-item label="排序" :span="1">{{ detailData.sortOrder || 0 }}</el-descriptions-item>
            <el-descriptions-item label="相关详情" :span="2">{{ detailData.details || '-' }}</el-descriptions-item>
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

<script setup name="Community">
import { listCommunity, addCommunity, getCommunity, updateCommunity, delCommunity } from "@/api/core/community"
import { isExternal } from "@/utils/validate"

const { proxy } = getCurrentInstance()
const { sys_normal_disable } = proxy.useDict("sys_normal_disable")

const baseUrl = import.meta.env.VITE_APP_BASE_API

const communityList = ref([])
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
    name: undefined,
    address: undefined,
    status: undefined
  },
  rules: {
    name: [{ required: true, message: "社区名不能为空", trigger: "blur" }],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 获取图片完整URL */
function getImageUrl(image) {
  if (!image) return ''
  if (isExternal(image)) return image
  return baseUrl + image
}

/** 查询社区列表 */
function getList() {
  loading.value = true
  listCommunity(queryParams.value).then(response => {
    communityList.value = response.data
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
    name: undefined,
    image: undefined,
    address: undefined,
    longitude: undefined,
    latitude: undefined,
    details: undefined,
    wantToGoCount: 0,
    visitedCount: 0,
    reviewCount: 0,
    rating: 5,
    status: "0",
    sortOrder: 0,
    remark: undefined
  }
  proxy.resetForm("communityRef")
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
  title.value = "添加社区"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const id = row.id || ids.value
  getCommunity(id).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改社区"
  })
}

/** 查看详情按钮操作 */
function handleView(row) {
  const id = row.id
  getCommunity(id).then(response => {
    detailData.value = response.data
    detailOpen.value = true
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["communityRef"].validate(valid => {
    if (valid) {
      if (form.value.id != undefined) {
        updateCommunity(form.value).then(() => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addCommunity(form.value).then(() => {
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
  const communityIds = row.id || ids.value
  proxy.$modal.confirm('是否确认删除社区编号为"' + communityIds + '"的数据项？').then(function() {
    return delCommunity(communityIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download("core/community/export", {
    ...queryParams.value
  }, `community_${new Date().getTime()}.xlsx`)
}

getList()
</script>
