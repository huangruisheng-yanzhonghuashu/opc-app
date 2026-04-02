<template>
   <div class="app-container">
      <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
         <el-form-item label="分类名称" prop="categoryName">
            <el-input
               v-model="queryParams.categoryName"
               placeholder="请输入分类名称"
               clearable
               style="width: 200px"
               @keyup.enter="handleQuery"
            />
         </el-form-item>
         <el-form-item label="套餐分类" prop="packageType">
            <el-select v-model="queryParams.packageType" placeholder="全部" clearable style="width: 200px">
               <el-option label="VIP素材" :value="2" />
               <el-option label="超级VIP" :value="3" />
            </el-select>
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
               v-hasPermi="['core:materialCategory:add']"
            >新增</el-button>
         </el-col>
         <el-col :span="1.5">
            <el-button
               type="danger"
               plain
               icon="Delete"
               :disabled="multiple"
               @click="handleDelete"
               v-hasPermi="['core:materialCategory:remove']"
            >删除</el-button>
         </el-col>
         <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="categoryList" @selection-change="handleSelectionChange">
         <el-table-column type="selection" width="55" align="center" />
         <el-table-column label="分类ID" align="center" prop="id" width="80" />
         <el-table-column label="分类名称" align="center" prop="categoryName" />
         <el-table-column label="套餐分类" align="center" prop="packageType" width="100">
            <template #default="scope">
               <el-tag v-if="scope.row.packageType === 2" type="success">VIP素材</el-tag>
               <el-tag v-else-if="scope.row.packageType === 3" type="warning">超级VIP</el-tag>
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
               <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['core:materialCategory:edit']">编辑</el-button>
               <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['core:materialCategory:remove']">删除</el-button>
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
         <el-form ref="categoryRef" :model="form" :rules="rules" label-width="100px">
            <el-form-item label="分类名称" prop="categoryName">
               <el-input v-model="form.categoryName" placeholder="请输入分类名称" />
            </el-form-item>
            <el-form-item label="套餐分类" prop="packageType">
               <el-select v-model="form.packageType" placeholder="请选择套餐分类" style="width: 100%">
                  <el-option label="VIP素材" :value="2" />
                  <el-option label="超级VIP" :value="3" />
               </el-select>
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

<script setup name="MaterialCategory">
import { listMaterialCategory, addMaterialCategory, getMaterialCategory, updateMaterialCategory, delMaterialCategory } from "@/api/core/materialCategory"

const { proxy } = getCurrentInstance()

const categoryList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    categoryName: undefined,
    packageType: undefined,
    status: undefined
  },
  rules: {
    categoryName: [{ required: true, message: "分类名称不能为空", trigger: "blur" }],
    packageType: [{ required: true, message: "套餐分类不能为空", trigger: "change" }],
    sortOrder: [{ required: true, message: "排序不能为空", trigger: "blur" }]
  }
})

const { queryParams, form, rules } = toRefs(data)

function getList() {
  loading.value = true
  listMaterialCategory(queryParams.value).then(response => {
    categoryList.value = response.data
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
    categoryName: undefined,
    packageType: undefined,
    sortOrder: 0,
    status: '0',
    remark: undefined
  }
  proxy.resetForm("categoryRef")
}

function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

function resetQuery() {
  proxy.resetForm("queryRef")
  queryParams.value.packageType = undefined
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
  title.value = "添加二级分类"
}

function handleUpdate(row) {
  reset()
  const id = row.id || ids.value
  getMaterialCategory(id).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改二级分类"
  })
}

function handleDelete(row) {
  const categoryIds = row.id ? [row.id] : ids.value
  proxy.$modal.confirm('是否确认删除二级分类编号为"' + categoryIds + '"的数据项？').then(function() {
    return delMaterialCategory(categoryIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

function submitForm() {
  proxy.$refs["categoryRef"].validate(valid => {
    if (valid) {
      if (form.value.id != undefined) {
        updateMaterialCategory(form.value).then(() => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addMaterialCategory(form.value).then(() => {
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
