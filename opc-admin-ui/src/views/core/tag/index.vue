<template>
   <div class="app-container">
      <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
         <el-form-item label="标签名称" prop="tagName">
            <el-input
               v-model="queryParams.tagName"
               placeholder="请输入标签名称"
               clearable
               style="width: 200px"
               @keyup.enter="handleQuery"
            />
         </el-form-item>
         <el-form-item label="状态" prop="status">
            <el-select v-model="queryParams.status" placeholder="全部" clearable style="width: 200px">
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
               v-hasPermi="['core:tag:add']"
            >新增</el-button>
         </el-col>
         <el-col :span="1.5">
            <el-button
               type="danger"
               plain
               icon="Delete"
               :disabled="multiple"
               @click="handleDelete"
               v-hasPermi="['core:tag:remove']"
            >删除</el-button>
         </el-col>
         <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="tagList" @selection-change="handleSelectionChange">
         <el-table-column type="selection" width="55" align="center" />
         <el-table-column label="标签ID" align="center" prop="id" width="80" />
         <el-table-column label="标签名称" align="center" prop="tagName" />
         <el-table-column label="标签颜色" align="center" prop="tagColor" width="150">
            <template #default="scope">
               <div v-if="scope.row.tagColor" class="color-preview">
                  <span class="color-block" :style="{ backgroundColor: scope.row.tagColor }"></span>
                  <span class="color-text">{{ scope.row.tagColor }}</span>
               </div>
               <span v-else>-</span>
            </template>
         </el-table-column>
         <el-table-column label="排序" align="center" prop="sortOrder" width="80" />
         <el-table-column label="状态" align="center" prop="status" width="80">
            <template #default="scope">
               <el-tag :type="scope.row.status === '0' ? 'success' : 'danger'">
                  {{ scope.row.status === '0' ? '正常' : '停用' }}
               </el-tag>
            </template>
         </el-table-column>
         <el-table-column label="创建时间" align="center" prop="createTime" width="160">
            <template #default="scope">
               <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
         </el-table-column>
         <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" />
         <el-table-column label="操作" width="200" align="center" class-name="small-padding fixed-width">
            <template #default="scope">
               <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['core:tag:edit']">编辑</el-button>
               <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['core:tag:remove']">删除</el-button>
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

      <el-dialog :title="title" v-model="open" width="600px" append-to-body>
         <el-form ref="tagRef" :model="form" :rules="rules" label-width="100px">
            <el-form-item label="标签名称" prop="tagName">
               <el-input v-model="form.tagName" placeholder="请输入标签名称" />
            </el-form-item>
            <el-form-item label="标签颜色" prop="tagColor">
               <el-color-picker v-model="form.tagColor" show-alpha :predefine="predefineColors" />
               <span class="color-value" v-if="form.tagColor">{{ form.tagColor }}</span>
            </el-form-item>
            <el-form-item label="排序" prop="sortOrder">
               <el-input-number v-model="form.sortOrder" :min="0" :max="999" placeholder="请输入排序" style="width: 100%" />
            </el-form-item>
            <el-form-item label="状态" prop="status">
               <el-radio-group v-model="form.status">
                  <el-radio label="0">正常</el-radio>
                  <el-radio label="1">停用</el-radio>
               </el-radio-group>
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
   </div>
</template>

<script setup name="Tag">
import { listTag, addTag, getTag, updateTag, delTag } from "@/api/core/tag"

const { proxy } = getCurrentInstance()

const tagList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")

const predefineColors = ref([
  '#ff4500',
  '#ff8c00',
  '#ffd700',
  '#90ee90',
  '#00ced1',
  '#1e90ff',
  '#c71585',
  '#ff69b4',
  '#dda0dd',
  '#9370db',
  '#667eea',
  '#764ba2',
  '#f093fb',
  '#f5576c',
  '#4facfe',
  '#00f2fe'
])

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    tagName: undefined,
    status: undefined
  },
  rules: {
    tagName: [{ required: true, message: "标签名称不能为空", trigger: "blur" }],
    sortOrder: [{ required: true, message: "排序不能为空", trigger: "blur" }]
  }
})

const { queryParams, form, rules } = toRefs(data)

function getList() {
  loading.value = true
  listTag(queryParams.value).then(response => {
    tagList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

function cancel() {
  open.value = false
  reset()
}

function reset() {
  form.value = {
    id: undefined,
    tagName: undefined,
    tagColor: undefined,
    sortOrder: 0,
    status: '0',
    remark: undefined
  }
  proxy.resetForm("tagRef")
}

function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

function resetQuery() {
  proxy.resetForm("queryRef")
  queryParams.value.status = undefined
  handleQuery()
}

function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

function handleAdd() {
  reset()
  open.value = true
  title.value = "添加标签"
}

function handleUpdate(row) {
  reset()
  const id = row.id || ids.value
  getTag(id).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改标签"
  })
}

function handleDelete(row) {
  const tagIds = row.id ? [row.id] : ids.value
  proxy.$modal.confirm('是否确认删除标签编号为"' + tagIds + '"的数据项？').then(function() {
    return delTag(tagIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

function submitForm() {
  proxy.$refs["tagRef"].validate(valid => {
    if (valid) {
      if (form.value.id != undefined) {
        updateTag(form.value).then(() => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addTag(form.value).then(() => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}

getList()
</script>

<style scoped>
.color-preview {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}
.color-block {
  width: 24px;
  height: 24px;
  border-radius: 4px;
  border: 1px solid #dcdfe6;
  flex-shrink: 0;
}
.color-text {
  font-family: monospace;
  font-size: 12px;
  color: #606266;
}
.color-value {
  margin-left: 10px;
  color: #606266;
  font-size: 14px;
}
:deep(.el-color-picker__trigger) {
  width: 40px;
  height: 40px;
}
</style>
