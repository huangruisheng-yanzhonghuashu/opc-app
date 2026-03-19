<template>
   <div class="app-container">
      <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
         <el-form-item label="关键词" prop="keyword">
            <el-input
               v-model="queryParams.keyword"
               placeholder="请输入关键词"
               clearable
               style="width: 200px"
               @keyup.enter="handleQuery"
            />
         </el-form-item>
         <el-form-item label="来源类型" prop="sourceType">
            <el-select v-model="queryParams.sourceType" placeholder="请选择来源类型" clearable style="width: 200px">
               <el-option label="Twitter" value="twitter" />
               <el-option label="Telegram" value="telegram" />
               <el-option label="YouTube" value="youtube" />
            </el-select>
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
               v-hasPermi="['core:collect:add']"
            >新增</el-button>
         </el-col>
         <el-col :span="1.5">
            <el-button
               type="success"
               plain
               icon="Edit"
               :disabled="single"
               @click="handleUpdate"
               v-hasPermi="['core:collect:edit']"
            >修改</el-button>
         </el-col>
         <el-col :span="1.5">
            <el-button
               type="danger"
               plain
               icon="Delete"
               :disabled="multiple"
               @click="handleDelete"
               v-hasPermi="['core:collect:remove']"
            >删除</el-button>
         </el-col>
         <el-col :span="1.5">
            <el-button
               type="warning"
               plain
               icon="Download"
               @click="handleExport"
               v-hasPermi="['core:collect:export']"
            >导出</el-button>
         </el-col>
         <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="collectSourceList" @selection-change="handleSelectionChange">
         <el-table-column type="selection" width="55" align="center" />
         <el-table-column label="配置ID" align="center" prop="id" width="80" />
         <el-table-column label="关键词" align="center" prop="keyword" />
         <el-table-column label="信息源链接" align="center" prop="sourceUrl" :show-overflow-tooltip="true" />
         <el-table-column label="来源类型" align="center" prop="sourceType" width="120">
            <template #default="scope">
               <el-tag v-if="scope.row.sourceType === 'twitter'" type="primary">Twitter</el-tag>
               <el-tag v-else-if="scope.row.sourceType === 'telegram'" type="success">Telegram</el-tag>
               <el-tag v-else-if="scope.row.sourceType === 'youtube'" type="danger">YouTube</el-tag>
               <span v-else>{{ scope.row.sourceType }}</span>
            </template>
         </el-table-column>
         <el-table-column label="状态" align="center" prop="status" width="80">
            <template #default="scope">
               <el-switch
                  v-model="scope.row.status"
                  active-value="0"
                  inactive-value="1"
                  @change="handleStatusChange(scope.row)"
                  v-hasPermi="['core:collect:changeStatus']"
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
               <el-button link type="primary" icon="View" @click="handleView(scope.row)" v-hasPermi="['core:collect:query']">详情</el-button>
               <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['core:collect:edit']">修改</el-button>
               <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['core:collect:remove']">删除</el-button>
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

      <!-- 添加或修改采集信息源对话框 -->
      <el-dialog :title="title" v-model="open" width="600px" append-to-body>
         <el-form ref="collectSourceRef" :model="form" :rules="rules" label-width="100px">
            <el-form-item label="关键词" prop="keyword">
               <el-input v-model="form.keyword" placeholder="请输入关键词" />
            </el-form-item>
            <el-form-item label="信息源链接" prop="sourceUrl">
               <el-input v-model="form.sourceUrl" placeholder="请输入信息源链接" />
            </el-form-item>
            <el-form-item label="来源类型" prop="sourceType">
               <el-select v-model="form.sourceType" placeholder="请选择来源类型" style="width: 100%">
                  <el-option label="Twitter" value="twitter" />
                  <el-option label="Telegram" value="telegram" />
                  <el-option label="YouTube" value="youtube" />
               </el-select>
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

      <!-- 采集信息源详情对话框 -->
      <el-dialog title="采集信息源详情" v-model="detailOpen" width="600px" append-to-body>
         <el-descriptions :column="2" border v-if="detailData">
            <el-descriptions-item label="配置ID" :span="1">{{ detailData.id }}</el-descriptions-item>
            <el-descriptions-item label="关键词" :span="1">{{ detailData.keyword || '-' }}</el-descriptions-item>
            <el-descriptions-item label="信息源链接" :span="2">{{ detailData.sourceUrl || '-' }}</el-descriptions-item>
            <el-descriptions-item label="来源类型" :span="1">
               <el-tag v-if="detailData.sourceType === 'twitter'" type="primary">Twitter</el-tag>
               <el-tag v-else-if="detailData.sourceType === 'telegram'" type="success">Telegram</el-tag>
               <el-tag v-else-if="detailData.sourceType === 'youtube'" type="danger">YouTube</el-tag>
               <span v-else>{{ detailData.sourceType || '-' }}</span>
            </el-descriptions-item>
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

<script setup name="CollectSource">
import { listCollectSource, getCollectSource, addCollectSource, updateCollectSource, delCollectSource, changeCollectSourceStatus } from "@/api/core/collectSource"

const { proxy } = getCurrentInstance()

const collectSourceList = ref([])
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
    keyword: undefined,
    sourceType: undefined,
    status: undefined
  },
  rules: {
    keyword: [{ required: true, message: "关键词不能为空", trigger: "blur" }],
    sourceUrl: [{ required: true, message: "信息源链接不能为空", trigger: "blur" }],
    sourceType: [{ required: true, message: "来源类型不能为空", trigger: "change" }]
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询采集信息源列表 */
function getList() {
  loading.value = true
  listCollectSource(queryParams.value).then(response => {
    collectSourceList.value = response.rows
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
    keyword: undefined,
    sourceUrl: undefined,
    sourceType: 'twitter',
    status: '0',
    remark: undefined
  }
  proxy.resetForm("collectSourceRef")
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
  title.value = "添加采集信息源"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const id = row.id || ids.value
  getCollectSource(id).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改采集信息源"
  })
}

/** 查看详情按钮操作 */
function handleView(row) {
  const id = row.id
  getCollectSource(id).then(response => {
    detailData.value = response.data
    detailOpen.value = true
  })
}

/** 删除按钮操作 */
function handleDelete(row) {
  const sourceIds = row.id || ids.value
  proxy.$modal.confirm('是否确认删除采集信息源编号为"' + sourceIds + '"的数据项？').then(() => {
    return delCollectSource(sourceIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 状态修改 */
function handleStatusChange(row) {
  const text = row.status === "0" ? "启用" : "禁用"
  proxy.$modal.confirm('确认要' + text + '"' + row.keyword + '"采集信息源吗？').then(() => {
    return changeCollectSourceStatus(row.id, row.status)
  }).then(() => {
    proxy.$modal.msgSuccess(text + "成功")
  }).catch(() => {
    row.status = row.status === "0" ? "1" : "0"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["collectSourceRef"].validate(valid => {
    if (valid) {
      if (form.value.id != undefined) {
        updateCollectSource(form.value).then(() => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addCollectSource(form.value).then(() => {
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
  proxy.download("core/collect/export", {
    ...queryParams.value
  }, `collectSource_${new Date().getTime()}.xlsx`)
}

getList()
</script>
