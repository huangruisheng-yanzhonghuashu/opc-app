<template>
   <div class="app-container">
      <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
         <el-form-item label="套餐名称" prop="packageName">
            <el-input
               v-model="queryParams.packageName"
               placeholder="请输入套餐名称"
               clearable
               style="width: 200px"
               @keyup.enter="handleQuery"
            />
         </el-form-item>
         <el-form-item label="套餐分类" prop="packageType">
            <el-select v-model="queryParams.packageType" placeholder="全部" clearable style="width: 200px">
               <el-option label="普通会员" value="normal" />
               <el-option label="VIP会员" value="vip" />
               <el-option label="超级VIP会员" value="svip" />
            </el-select>
         </el-form-item>
         <el-form-item label="状态" prop="status">
            <el-select v-model="queryParams.status" placeholder="全部" clearable style="width: 200px">
               <el-option label="上架" value="0" />
               <el-option label="下架" value="1" />
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
               v-hasPermi="['core:package:add']"
            >新增</el-button>
         </el-col>
         <el-col :span="1.5">
            <el-button
               type="danger"
               plain
               icon="Delete"
               :disabled="multiple"
               @click="handleDelete"
               v-hasPermi="['core:package:remove']"
            >删除</el-button>
         </el-col>
         <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="packageList" @selection-change="handleSelectionChange">
         <el-table-column type="selection" width="55" align="center" />
         <el-table-column label="套餐ID" align="center" prop="id" width="80" />
         <el-table-column label="套餐名称" align="center" prop="packageName" />
         <el-table-column label="套餐价格" align="center" prop="packagePrice" width="120">
            <template #default="scope">
               <span>￥{{ scope.row.packagePrice }}</span>
            </template>
         </el-table-column>
         <el-table-column label="套餐分类" align="center" prop="packageType" width="120">
            <template #default="scope">
               <el-tag v-if="scope.row.packageType === 'normal'" type="info">普通会员</el-tag>
               <el-tag v-else-if="scope.row.packageType === 'vip'" type="primary">VIP会员</el-tag>
               <el-tag v-else-if="scope.row.packageType === 'svip'" type="warning">超级VIP会员</el-tag>
            </template>
         </el-table-column>
         <el-table-column label="状态" align="center" prop="status" width="80">
            <template #default="scope">
               <el-tag :type="scope.row.status === '0' ? 'success' : 'danger'">
                  {{ scope.row.status === '0' ? '上架' : '下架' }}
               </el-tag>
            </template>
         </el-table-column>
         <el-table-column label="更新时间" align="center" prop="updateTime" width="160">
            <template #default="scope">
               <span>{{ parseTime(scope.row.updateTime) }}</span>
            </template>
         </el-table-column>
         <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" />
         <el-table-column label="操作" width="120" align="center" class-name="small-padding fixed-width">
            <template #default="scope">
               <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['core:package:edit']">编辑</el-button>
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
         <el-form ref="packageRef" :model="form" :rules="rules" label-width="100px">
            <el-row>
               <el-col :span="12">
                  <el-form-item label="套餐名称" prop="packageName">
                     <el-input v-model="form.packageName" placeholder="请输入套餐名称" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item label="套餐价格" prop="packagePrice">
                     <el-input-number v-model="form.packagePrice" :min="0" :precision="2" placeholder="请输入套餐价格" style="width: 100%" />
                  </el-form-item>
               </el-col>
            </el-row>
            <el-row>
               <el-col :span="12">
                  <el-form-item label="套餐分类" prop="packageType">
                     <el-select v-model="form.packageType" placeholder="请选择套餐分类" style="width: 100%">
                        <el-option label="普通会员" value="normal" />
                        <el-option label="VIP会员" value="vip" />
                        <el-option label="超级VIP会员" value="svip" />
                     </el-select>
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item label="状态" prop="status">
                     <el-radio-group v-model="form.status">
                        <el-radio label="0">上架</el-radio>
                        <el-radio label="1">下架</el-radio>
                     </el-radio-group>
                  </el-form-item>
               </el-col>
            </el-row>
            <el-form-item label="套餐描述" prop="description">
               <el-input v-model="form.description" type="textarea" placeholder="请输入套餐描述" :rows="3" />
            </el-form-item>
            <el-form-item label="备注" prop="remark">
               <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" :rows="2" />
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

<script setup name="Package">
import { listPackage, addPackage, updatePackage, delPackage } from "@/api/core/package"

const { proxy } = getCurrentInstance()

const packageList = ref([])
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
    packageName: undefined,
    packageType: undefined,
    status: undefined
  },
  rules: {
    packageName: [{ required: true, message: "套餐名称不能为空", trigger: "blur" }],
    packagePrice: [{ required: true, message: "套餐价格不能为空", trigger: "blur" }],
    packageType: [{ required: true, message: "套餐分类不能为空", trigger: "change" }]
  }
})

const { queryParams, form, rules } = toRefs(data)

function getList() {
  loading.value = true
  listPackage(queryParams.value).then(response => {
    packageList.value = response.rows
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
    packageName: undefined,
    packagePrice: undefined,
    packageType: undefined,
    description: undefined,
    status: "0",
    remark: undefined
  }
  proxy.resetForm("packageRef")
}

function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

function resetQuery() {
  proxy.resetForm("queryRef")
  handleQuery()
}

function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id)
  single.value = selection.length !== 1
  multiple.value = !selection.length
}

function handleAdd() {
  reset()
  open.value = true
  title.value = "添加套餐"
}

function handleUpdate(row) {
  reset()
  const id = row.id || ids.value
  form.value = { ...row }
  open.value = true
  title.value = "修改套餐"
}

function handleDelete(row) {
  const ids = row.id || this.ids
  proxy.$modal.confirm('是否确认删除套餐编号为"' + ids + '"的数据项？').then(function() {
    return delPackage(ids)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

function submitForm() {
  proxy.$refs["packageRef"].validate(valid => {
    if (valid) {
      if (form.value.id !== undefined) {
        updatePackage(form.value).then(() => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addPackage(form.value).then(() => {
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
