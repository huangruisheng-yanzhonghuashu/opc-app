<template>
   <div class="app-container">
      <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
         <el-form-item label="会员名" prop="username">
            <el-input
               v-model="queryParams.username"
               placeholder="请输入会员名"
               clearable
               style="width: 200px"
               @keyup.enter="handleQuery"
            />
         </el-form-item>
         <el-form-item label="会员昵称" prop="nickname">
            <el-input
               v-model="queryParams.nickname"
               placeholder="请输入会员昵称"
               clearable
               style="width: 200px"
               @keyup.enter="handleQuery"
            />
         </el-form-item>
         <el-form-item label="手机号" prop="phoneNumber">
            <el-input
               v-model="queryParams.phoneNumber"
               placeholder="请输入手机号"
               clearable
               style="width: 200px"
               @keyup.enter="handleQuery"
            />
         </el-form-item>
         <el-form-item label="邮箱" prop="email">
            <el-input
               v-model="queryParams.email"
               placeholder="请输入邮箱"
               clearable
               style="width: 200px"
               @keyup.enter="handleQuery"
            />
         </el-form-item>
         <el-form-item label="状态" prop="status">
            <el-select v-model="queryParams.status" placeholder="会员状态" clearable style="width: 200px">
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
               v-hasPermi="['core:member:add']"
            >新增</el-button>
         </el-col>
         <el-col :span="1.5">
            <el-button
               type="success"
               plain
               icon="Edit"
               :disabled="single"
               @click="handleUpdate"
               v-hasPermi="['core:member:edit']"
            >修改</el-button>
         </el-col>
         <el-col :span="1.5">
            <el-button
               type="danger"
               plain
               icon="Delete"
               :disabled="multiple"
               @click="handleDelete"
               v-hasPermi="['core:member:remove']"
            >删除</el-button>
         </el-col>
         <el-col :span="1.5">
            <el-button
               type="warning"
               plain
               icon="Download"
               @click="handleExport"
               v-hasPermi="['core:member:export']"
            >导出</el-button>
         </el-col>
         <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="memberList" @selection-change="handleSelectionChange">
         <el-table-column type="selection" width="55" align="center" />
         <el-table-column label="会员ID" align="center" prop="memberId" width="80" />
         <el-table-column label="会员名" align="center" prop="username" />
         <el-table-column label="会员昵称" align="center" prop="nickname" />
         <el-table-column label="手机号" align="center" prop="phoneNumber" width="120" />
         <el-table-column label="邮箱" align="center" prop="email" width="180" />
         <el-table-column label="当前套餐" align="center" prop="currentPackage" width="100" />
         <el-table-column label="一级用户" align="center" prop="level1Users" width="80" />
         <el-table-column label="二级用户" align="center" prop="level2Users" width="80" />
         <el-table-column label="三级用户" align="center" prop="level3Users" width="80" />
         <el-table-column label="来源" align="center" prop="source" width="100" />
         <el-table-column label="状态" align="center" prop="status" width="80">
            <template #default="scope">
               <dict-tag :options="sys_normal_disable" :value="scope.row.status" />
            </template>
         </el-table-column>
         <el-table-column label="注册时间" align="center" prop="registerTime" width="160">
            <template #default="scope">
               <span>{{ parseTime(scope.row.registerTime) }}</span>
            </template>
         </el-table-column>
         <el-table-column label="操作" width="180" align="center" class-name="small-padding fixed-width">
            <template #default="scope">
               <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['core:member:edit']">修改</el-button>
               <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['core:member:remove']">删除</el-button>
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

      <!-- 添加或修改会员对话框 -->
      <el-dialog :title="title" v-model="open" width="600px" append-to-body>
         <el-form ref="memberRef" :model="form" :rules="rules" label-width="100px">
            <el-row>
               <el-col :span="12">
                  <el-form-item label="会员名" prop="username">
                     <el-input v-model="form.username" placeholder="请输入会员名" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item label="会员昵称" prop="nickname">
                     <el-input v-model="form.nickname" placeholder="请输入会员昵称" />
                  </el-form-item>
               </el-col>
            </el-row>
            <el-row>
               <el-col :span="12">
                  <el-form-item label="手机号" prop="phoneNumber">
                     <el-input v-model="form.phoneNumber" placeholder="请输入手机号" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item label="邮箱" prop="email">
                     <el-input v-model="form.email" placeholder="请输入邮箱" />
                  </el-form-item>
               </el-col>
            </el-row>
            <el-row>
               <el-col :span="12">
                  <el-form-item label="当前套餐" prop="currentPackage">
                     <el-input v-model="form.currentPackage" placeholder="请输入当前套餐" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item label="来源" prop="source">
                     <el-input v-model="form.source" placeholder="请输入来源" />
                  </el-form-item>
               </el-col>
            </el-row>
            <el-row>
               <el-col :span="12">
                  <el-form-item label="来源ID" prop="sourceId">
                     <el-input v-model="form.sourceId" placeholder="请输入来源ID" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item label="会员状态" prop="status">
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
            <el-row>
               <el-col :span="8">
                  <el-form-item label="一级用户" prop="level1Users">
                     <el-input-number v-model="form.level1Users" controls-position="right" :min="0" />
                  </el-form-item>
               </el-col>
               <el-col :span="8">
                  <el-form-item label="二级用户" prop="level2Users">
                     <el-input-number v-model="form.level2Users" controls-position="right" :min="0" />
                  </el-form-item>
               </el-col>
               <el-col :span="8">
                  <el-form-item label="三级用户" prop="level3Users">
                     <el-input-number v-model="form.level3Users" controls-position="right" :min="0" />
                  </el-form-item>
               </el-col>
            </el-row>
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
   </div>
</template>

<script setup name="Member">
import { listMember, addMember, delMember, getMember, updateMember } from "@/api/core/member"

const { proxy } = getCurrentInstance()
const { sys_normal_disable } = proxy.useDict("sys_normal_disable")

const memberList = ref([])
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
    username: undefined,
    nickname: undefined,
    phoneNumber: undefined,
    email: undefined,
    status: undefined
  },
  rules: {
    username: [{ required: true, message: "会员名不能为空", trigger: "blur" }],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询会员列表 */
function getList() {
  loading.value = true
  listMember(queryParams.value).then(response => {
    memberList.value = response.rows
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
    memberId: undefined,
    username: undefined,
    nickname: undefined,
    phoneNumber: undefined,
    email: undefined,
    currentPackage: undefined,
    source: undefined,
    sourceId: undefined,
    level1Users: 0,
    level2Users: 0,
    level3Users: 0,
    status: "0",
    remark: undefined
  }
  proxy.resetForm("memberRef")
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
  ids.value = selection.map(item => item.memberId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加会员"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const memberId = row.memberId || ids.value
  getMember(memberId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改会员"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["memberRef"].validate(valid => {
    if (valid) {
      if (form.value.memberId != undefined) {
        updateMember(form.value).then(() => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addMember(form.value).then(() => {
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
  const memberIds = row.memberId || ids.value
  proxy.$modal.confirm('是否确认删除会员编号为"' + memberIds + '"的数据项？').then(function() {
    return delMember(memberIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download("core/member/export", {
    ...queryParams.value
  }, `member_${new Date().getTime()}.xlsx`)
}

getList()
</script>
