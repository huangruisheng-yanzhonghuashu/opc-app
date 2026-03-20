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
         <el-form-item label="来源" prop="source">
            <el-select v-model="queryParams.source" placeholder="请选择来源" clearable style="width: 200px">
               <el-option label="邮箱" value="email" />
               <el-option label="X" value="x" />
               <el-option label="Facebook" value="facebook" />
               <el-option label="Apple" value="apple" />
               <el-option label="Google" value="google" />
            </el-select>
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
         <el-table-column label="会员ID" align="center" prop="id" width="80" />
         <el-table-column label="头像" align="center" prop="avatar" width="80">
            <template #default="scope">
               <el-avatar :size="40" :src="getAvatarUrl(scope.row.avatar)" v-if="scope.row.avatar" />
               <el-avatar :size="40" v-else>{{ scope.row.username?.charAt(0) }}</el-avatar>
            </template>
         </el-table-column>
         <el-table-column label="会员名" align="center" prop="username" />
         <el-table-column label="会员昵称" align="center" prop="nickname" />
         <el-table-column label="手机号" align="center" prop="phoneNumber" width="120" />
         <el-table-column label="邮箱" align="center" prop="email" width="180" />
         <el-table-column label="当前套餐" align="center" prop="currentPackage" width="120">
            <template #default="scope">
               <span>{{ getPackageLabel(scope.row.currentPackage, scope.row.currentPackageLevel) }}</span>
            </template>
         </el-table-column>
         <el-table-column label="来源" align="center" prop="source" width="100">
            <template #default="scope">
               <el-tag v-if="scope.row.source === 'email'" type="info">邮箱</el-tag>
               <el-tag v-else-if="scope.row.source === 'x'" type="primary">X</el-tag>
               <el-tag v-else-if="scope.row.source === 'facebook'" type="primary">Facebook</el-tag>
               <el-tag v-else-if="scope.row.source === 'apple'" type="default">Apple</el-tag>
               <el-tag v-else-if="scope.row.source === 'google'" type="danger">Google</el-tag>
               <span v-else>{{ scope.row.source }}</span>
            </template>
         </el-table-column>
         <el-table-column label="最近活跃时间" align="center" prop="lastActiveTime" width="160">
            <template #default="scope">
               <span>{{ parseTime(scope.row.lastActiveTime) }}</span>
            </template>
         </el-table-column>
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
         <el-table-column label="操作" width="280" align="center" class-name="small-padding fixed-width">
            <template #default="scope">
               <el-button link type="primary" icon="View" @click="handleView(scope.row)" v-hasPermi="['core:member:query']">详情</el-button>
               <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['core:member:edit']">修改</el-button>
               <el-button
                  link
                  :type="scope.row.status === '0' ? 'danger' : 'success'"
                  :icon="scope.row.status === '0' ? 'CircleClose' : 'CircleCheck'"
                  @click="handleToggleBlock(scope.row)"
                  v-hasPermi="['core:member:edit']"
               >{{ scope.row.status === '0' ? '拉黑' : '解除' }}</el-button>
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
                  <el-form-item label="密码" prop="password">
                     <el-input v-model="form.password" type="password" placeholder="留空使用默认密码" />
                  </el-form-item>
               </el-col>
            </el-row>
            <el-row>
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
                  <el-form-item label="头像" prop="avatar">
                     <ImageUpload v-model="form.avatar" :limit="1" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item label="当前套餐" prop="currentPackage">
                     <el-select v-model="form.currentPackage" placeholder="请选择当前套餐" style="width: 100%">
                        <el-option label="一级" value="一级" />
                        <el-option label="二级" value="二级" />
                        <el-option label="三级" value="三级" />
                     </el-select>
                  </el-form-item>
               </el-col>
            </el-row>
            <el-row>
               <el-col :span="12">
                  <el-form-item label="来源" prop="source">
                     <el-select v-model="form.source" placeholder="请选择来源" style="width: 100%">
                        <el-option label="邮箱" value="email" />
                        <el-option label="X" value="x" />
                        <el-option label="Facebook" value="facebook" />
                        <el-option label="Apple" value="apple" />
                        <el-option label="Google" value="google" />
                     </el-select>
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item label="来源ID" prop="sourceId">
                     <el-input v-model="form.sourceId" placeholder="请输入来源ID" />
                  </el-form-item>
               </el-col>
            </el-row>
            <el-row>
               <el-col :span="12">
                  <el-form-item label="注册时间" prop="registerTime">
                     <el-date-picker
                        v-model="form.registerTime"
                        type="datetime"
                        placeholder="选择注册时间"
                        style="width: 100%"
                     />
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
               <el-col :span="12">
                  <el-form-item label="Token" prop="token">
                     <el-input v-model="form.token" placeholder="请输入Token" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item label="最近活跃" prop="lastActiveTime">
                     <el-date-picker
                        v-model="form.lastActiveTime"
                        type="datetime"
                        placeholder="选择最近活跃时间"
                        style="width: 100%"
                     />
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

      <!-- 会员详情对话框 -->
      <el-dialog title="会员详情" v-model="detailOpen" width="700px" append-to-body>
         <el-descriptions :column="2" border v-if="detailData">
            <el-descriptions-item label="会员ID" :span="1">{{ detailData.id }}</el-descriptions-item>
            <el-descriptions-item label="会员名" :span="1">{{ detailData.username }}</el-descriptions-item>
            <el-descriptions-item label="会员昵称" :span="1">{{ detailData.nickname || '-' }}</el-descriptions-item>
            <el-descriptions-item label="状态" :span="1">
               <el-tag :type="detailData.status === '0' ? 'success' : 'danger'">
                  {{ detailData.status === '0' ? '正常' : '已拉黑' }}
               </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="头像" :span="2">
               <el-avatar :size="60" :src="getAvatarUrl(detailData.avatar)" v-if="detailData.avatar" />
               <el-avatar :size="60" v-else>{{ detailData.username?.charAt(0) }}</el-avatar>
            </el-descriptions-item>
            <el-descriptions-item label="手机号" :span="1">{{ detailData.phoneNumber || '-' }}</el-descriptions-item>
            <el-descriptions-item label="邮箱" :span="1">{{ detailData.email || '-' }}</el-descriptions-item>
            <el-descriptions-item label="当前套餐" :span="1">{{ getPackageLabel(detailData.currentPackage, detailData.currentPackageLevel) }}</el-descriptions-item>
            <el-descriptions-item label="来源" :span="1">
               <el-tag v-if="detailData.source === 'email'" type="info">邮箱</el-tag>
               <el-tag v-else-if="detailData.source === 'x'" type="primary">X</el-tag>
               <el-tag v-else-if="detailData.source === 'facebook'" type="primary">Facebook</el-tag>
               <el-tag v-else-if="detailData.source === 'apple'" type="default">Apple</el-tag>
               <el-tag v-else-if="detailData.source === 'google'" type="danger">Google</el-tag>
               <span v-else>{{ detailData.source || '-' }}</span>
            </el-descriptions-item>
            <el-descriptions-item label="来源ID" :span="1">{{ detailData.sourceId || '-' }}</el-descriptions-item>
            <el-descriptions-item label="Token" :span="2">
               <el-tooltip :content="detailData.token" placement="top" v-if="detailData.token">
                  <span class="token-text">{{ detailData.token }}</span>
               </el-tooltip>
               <span v-else>-</span>
            </el-descriptions-item>
            <el-descriptions-item label="最近活跃时间" :span="1">{{ parseTime(detailData.lastActiveTime) || '-' }}</el-descriptions-item>
            <el-descriptions-item label="注册时间" :span="1">{{ parseTime(detailData.registerTime) || '-' }}</el-descriptions-item>
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

<script setup name="Member">
import { listMember, addMember, getMember, updateMember, blockMember, unblockMember } from "@/api/core/member"
import { isExternal } from "@/utils/validate"

const { proxy } = getCurrentInstance()
const { sys_normal_disable } = proxy.useDict("sys_normal_disable")

const baseUrl = import.meta.env.VITE_APP_BASE_API

const memberList = ref([])
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
    username: undefined,
    nickname: undefined,
    phoneNumber: undefined,
    email: undefined,
    source: undefined,
    status: undefined
  },
  rules: {
    username: [{ required: true, message: "会员名不能为空", trigger: "blur" }],
  }
})

const { queryParams, form, rules } = toRefs(data)

function getPackageLabel(currentPackage, currentPackageLevel) {
  if (currentPackage) {
    return currentPackage
  }
  if (currentPackageLevel === 1) return '一级'
  if (currentPackageLevel === 2) return '二级'
  if (currentPackageLevel === 3) return '三级'
  return '-'
}

/** 获取头像完整URL */
function getAvatarUrl(avatar) {
  if (!avatar) return ''
  if (isExternal(avatar)) return avatar
  return baseUrl + avatar
}

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
    id: undefined,
    username: undefined,
    password: undefined,
    nickname: undefined,
    phoneNumber: undefined,
    email: undefined,
    avatar: undefined,
    currentPackage: undefined,
    source: undefined,
    sourceId: undefined,
    token: undefined,
    registerTime: undefined,
    lastActiveTime: undefined,
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
  ids.value = selection.map(item => item.id)
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
  const id = row.id || ids.value
  getMember(id).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改会员"
  })
}

/** 查看详情按钮操作 */
function handleView(row) {
  const id = row.id
  getMember(id).then(response => {
    detailData.value = response.data
    detailOpen.value = true
  })
}

/** 拉黑/解除拉黑按钮操作 */
function handleToggleBlock(row) {
  const isBlock = row.status === '0'
  const text = isBlock ? "拉黑" : "解除拉黑"
  proxy.$modal.confirm('确认要' + text + '会员"' + row.username + '"吗？').then(function() {
    return isBlock ? blockMember(row.id) : unblockMember(row.id)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess(text + "成功")
  }).catch(() => {})
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["memberRef"].validate(valid => {
    if (valid) {
      if (form.value.id != undefined) {
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

/** 导出按钮操作 */
function handleExport() {
  proxy.download("core/member/export", {
    ...queryParams.value
  }, `member_${new Date().getTime()}.xlsx`)
}

getList()
</script>

<style scoped>
.token-text {
  max-width: 300px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  display: inline-block;
}
</style>
