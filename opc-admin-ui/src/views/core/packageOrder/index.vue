<template>
   <div class="app-container">
      <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
         <el-form-item label="邮箱" prop="email">
            <el-input
               v-model="queryParams.email"
               placeholder="请输入邮箱"
               clearable
               style="width: 200px"
               @keyup.enter="handleQuery"
            />
         </el-form-item>
         <el-form-item label="第三方账号" prop="thirdPartyAccount">
            <el-input
               v-model="queryParams.thirdPartyAccount"
               placeholder="请输入第三方账号"
               clearable
               style="width: 200px"
               @keyup.enter="handleQuery"
            />
         </el-form-item>
         <el-form-item label="昵称" prop="nickname">
            <el-input
               v-model="queryParams.nickname"
               placeholder="请输入昵称"
               clearable
               style="width: 200px"
               @keyup.enter="handleQuery"
            />
         </el-form-item>
         <el-form-item label="套餐名称" prop="packageName">
            <el-input
               v-model="queryParams.packageName"
               placeholder="请输入套餐名称"
               clearable
               style="width: 200px"
               @keyup.enter="handleQuery"
            />
         </el-form-item>
         <el-form-item label="支付状态" prop="payStatus">
            <el-select v-model="queryParams.payStatus" placeholder="全部" clearable style="width: 200px">
               <el-option label="待支付" value="0" />
               <el-option label="已支付" value="1" />
               <el-option label="已取消" value="2" />
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
               v-hasPermi="['core:packageOrder:add']"
            >新增</el-button>
         </el-col>
         <el-col :span="1.5">
            <el-button
               type="danger"
               plain
               icon="Delete"
               :disabled="multiple"
               @click="handleDelete"
               v-hasPermi="['core:packageOrder:remove']"
            >删除</el-button>
         </el-col>
         <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="orderList" @selection-change="handleSelectionChange">
         <el-table-column type="selection" width="55" align="center" />
         <el-table-column label="订单ID" align="center" prop="id" width="80" />
         <el-table-column label="订单号" align="center" prop="orderNo" width="180" :show-overflow-tooltip="true" />
         <el-table-column label="邮箱" align="center" prop="email" :show-overflow-tooltip="true" />
         <el-table-column label="第三方账号" align="center" prop="thirdPartyAccount" width="120" :show-overflow-tooltip="true" />
         <el-table-column label="昵称" align="center" prop="nickname" width="100" />
         <el-table-column label="套餐名称" align="center" prop="packageName" width="120" />
         <el-table-column label="套餐分类" align="center" prop="packageType" width="120">
            <template #default="scope">
               <el-tag v-if="scope.row.packageType === 'normal'" type="info">普通会员</el-tag>
               <el-tag v-else-if="scope.row.packageType === 'vip'" type="primary">VIP会员</el-tag>
               <el-tag v-else-if="scope.row.packageType === 'svip'" type="warning">超级VIP会员</el-tag>
            </template>
         </el-table-column>
         <el-table-column label="价格" align="center" prop="price" width="100">
            <template #default="scope">
               <span>￥{{ scope.row.price }}</span>
            </template>
         </el-table-column>
         <el-table-column label="支付状态" align="center" prop="payStatus" width="100">
            <template #default="scope">
               <el-tag v-if="scope.row.payStatus === '0'" type="warning">待支付</el-tag>
               <el-tag v-else-if="scope.row.payStatus === '1'" type="success">已支付</el-tag>
               <el-tag v-else-if="scope.row.payStatus === '2'" type="info">已取消</el-tag>
            </template>
         </el-table-column>
         <el-table-column label="支付时间" align="center" prop="payTime" width="160">
            <template #default="scope">
               <span>{{ parseTime(scope.row.payTime) }}</span>
            </template>
         </el-table-column>
         <el-table-column label="创建时间" align="center" prop="createTime" width="160">
            <template #default="scope">
               <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
         </el-table-column>
         <el-table-column label="操作" width="120" align="center" class-name="small-padding fixed-width">
            <template #default="scope">
               <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['core:packageOrder:edit']">编辑</el-button>
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

      <el-dialog :title="title" v-model="open" width="700px" append-to-body>
         <el-form ref="orderRef" :model="form" :rules="rules" label-width="120px">
            <el-row>
               <el-col :span="12">
                  <el-form-item label="订单号" prop="orderNo">
                     <el-input v-model="form.orderNo" placeholder="请输入订单号" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item label="套餐" prop="packageId">
                     <el-select v-model="form.packageId" placeholder="请选择套餐" style="width: 100%" @change="handlePackageChange">
                        <el-option label="普通会员 - ￥99" :value="1" />
                        <el-option label="VIP会员 - ￥199" :value="2" />
                        <el-option label="超级VIP会员 - ￥299" :value="3" />
                     </el-select>
                  </el-form-item>
               </el-col>
            </el-row>
            <el-row>
               <el-col :span="12">
                  <el-form-item label="邮箱" prop="email">
                     <el-input v-model="form.email" placeholder="请输入邮箱" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item label="第三方账号" prop="thirdPartyAccount">
                     <el-input v-model="form.thirdPartyAccount" placeholder="请输入第三方账号" />
                  </el-form-item>
               </el-col>
            </el-row>
            <el-row>
               <el-col :span="12">
                  <el-form-item label="昵称" prop="nickname">
                     <el-input v-model="form.nickname" placeholder="请输入昵称" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item label="价格" prop="price">
                     <el-input-number v-model="form.price" :min="0" :precision="2" placeholder="请输入价格" style="width: 100%" />
                  </el-form-item>
               </el-col>
            </el-row>
            <el-row>
               <el-col :span="12">
                  <el-form-item label="支付状态" prop="payStatus">
                     <el-select v-model="form.payStatus" placeholder="请选择支付状态" style="width: 100%">
                        <el-option label="待支付" value="0" />
                        <el-option label="已支付" value="1" />
                        <el-option label="已取消" value="2" />
                     </el-select>
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item label="支付时间" prop="payTime">
                     <el-date-picker
                        v-model="form.payTime"
                        type="datetime"
                        placeholder="选择支付时间"
                        style="width: 100%"
                     />
                  </el-form-item>
               </el-col>
            </el-row>
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

<script setup name="PackageOrder">
import { listPackageOrder, addPackageOrder, updatePackageOrder, delPackageOrder } from "@/api/core/packageOrder"

const { proxy } = getCurrentInstance()

const orderList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")

const packageMap = {
   1: { name: '普通会员', type: 'normal', price: 99.00 },
   2: { name: 'VIP会员', type: 'vip', price: 199.00 },
   3: { name: '超级VIP会员', type: 'svip', price: 299.00 }
}

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    email: undefined,
    thirdPartyAccount: undefined,
    nickname: undefined,
    packageName: undefined,
    payStatus: undefined
  },
  rules: {
    orderNo: [{ required: true, message: "订单号不能为空", trigger: "blur" }],
    packageId: [{ required: true, message: "请选择套餐", trigger: "change" }],
    payStatus: [{ required: true, message: "请选择支付状态", trigger: "change" }]
  }
})

const { queryParams, form, rules } = toRefs(data)

function handlePackageChange(packageId) {
   const pkg = packageMap[packageId]
   if (pkg) {
      form.value.packageName = pkg.name
      form.value.packageType = pkg.type
      form.value.price = pkg.price
   }
}

function getList() {
   loading.value = true
   listPackageOrder(queryParams.value).then(response => {
      orderList.value = response.rows
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
      orderNo: undefined,
      memberId: undefined,
      email: undefined,
      thirdPartyAccount: undefined,
      nickname: undefined,
      packageId: undefined,
      packageName: undefined,
      packageType: undefined,
      price: undefined,
      payTime: undefined,
      payStatus: "0",
      remark: undefined
   }
   proxy.resetForm("orderRef")
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
   title.value = "添加订单"
}

function handleUpdate(row) {
   reset()
   form.value = { ...row }
   open.value = true
   title.value = "修改订单"
}

function handleDelete(row) {
   const deleteIds = row.id || ids.value
   proxy.$modal.confirm('是否确认删除订单编号为"' + deleteIds + '"的数据项？').then(function() {
      return delPackageOrder(deleteIds)
   }).then(() => {
      getList()
      proxy.$modal.msgSuccess("删除成功")
   }).catch(() => {})
}

function submitForm() {
   proxy.$refs["orderRef"].validate(valid => {
      if (valid) {
         if (form.value.id !== undefined) {
            updatePackageOrder(form.value).then(() => {
               proxy.$modal.msgSuccess("修改成功")
               open.value = false
               getList()
            })
         } else {
            addPackageOrder(form.value).then(() => {
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
