<template>
   <div class="app-container">
      <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
         <el-form-item label="配置类型" prop="configType">
            <el-select v-model="queryParams.configType" placeholder="全部" clearable style="width: 200px">
               <el-option label="会员页Banner图" value="banner" />
               <el-option label="VIP引导图片" value="vip_guide" />
            </el-select>
         </el-form-item>
         <el-form-item>
            <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
            <el-button icon="Refresh" @click="resetQuery">重置</el-button>
         </el-form-item>
      </el-form>

      <el-row :gutter="10" class="mb8">
         <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="configList">
         <el-table-column label="配置ID" align="center" prop="id" width="80" />
         <el-table-column label="配置类型" align="center" prop="configType" width="150">
            <template #default="scope">
               <el-tag v-if="scope.row.configType === 'banner'" type="primary">会员页Banner图</el-tag>
               <el-tag v-else-if="scope.row.configType === 'vip_guide'" type="warning">VIP引导图片</el-tag>
            </template>
         </el-table-column>
         <el-table-column label="图片" align="center" prop="imageUrl" width="120">
            <template #default="scope">
               <el-image v-if="scope.row.imageUrl" :src="getImageUrl(scope.row.imageUrl)" style="width: 60px; height: 60px" fit="cover" />
               <span v-else>-</span>
            </template>
         </el-table-column>
         <el-table-column label="文章链接/id" align="center" prop="articleLink" :show-overflow-tooltip="true" />
         <el-table-column label="富文本内容" align="center" prop="richContent" :show-overflow-tooltip="true">
            <template #default="scope">
               <span v-if="scope.row.richContent" v-html="scope.row.richContent.substring(0, 50) + '...'"></span>
               <span v-else>-</span>
            </template>
         </el-table-column>
         <el-table-column label="状态" align="center" prop="status" width="80">
            <template #default="scope">
               <el-tag :type="scope.row.status === '0' ? 'success' : 'danger'">
                  {{ scope.row.status === '0' ? '启用' : '禁用' }}
               </el-tag>
            </template>
         </el-table-column>
         <el-table-column label="更新时间" align="center" prop="updateTime" width="160">
            <template #default="scope">
               <span>{{ parseTime(scope.row.updateTime) }}</span>
            </template>
         </el-table-column>
         <el-table-column label="操作" width="120" align="center" class-name="small-padding fixed-width">
            <template #default="scope">
               <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['core:memberConfig:save']">编辑</el-button>
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
         <el-form ref="configRef" :model="form" :rules="rules" label-width="140px">
            <el-form-item label="配置类型" prop="configType">
               <el-tag v-if="form.configType === 'banner'" type="primary">会员页Banner图</el-tag>
               <el-tag v-else-if="form.configType === 'vip_guide'" type="warning">VIP引导图片</el-tag>
            </el-form-item>
            <div v-if="form.configType === 'banner'">
               <el-form-item label="图片" prop="imageUrl">
                  <ImageUpload v-model="form.imageUrl" :limit="1" />
               </el-form-item>
               <el-form-item label="文章链接/id" prop="articleLink">
                  <el-input v-model="form.articleLink" placeholder="请输入文章链接或ID" />
               </el-form-item>
            </div>
            <div v-if="form.configType === 'vip_guide'">
               <el-form-item label="富文本内容" prop="richContent">
                  <Editor v-model="form.richContent" :min-height="300" />
               </el-form-item>
            </div>
            <el-form-item label="状态" prop="status">
               <el-radio-group v-model="form.status">
                  <el-radio label="0">启用</el-radio>
                  <el-radio label="1">禁用</el-radio>
               </el-radio-group>
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

<script setup name="MemberConfig">
import { listMemberConfig, saveMemberConfig } from "@/api/core/memberConfig"
import ImageUpload from "@/components/ImageUpload/index.vue"
import Editor from "@/components/Editor/index.vue"
import { isExternal } from "@/utils/validate"

const { proxy } = getCurrentInstance()

const baseUrl = import.meta.env.VITE_APP_BASE_API

const configList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const total = ref(0)
const title = ref("")

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    configType: undefined
  },
  rules: {}
})

const { queryParams, form, rules } = toRefs(data)

function getImageUrl(url) {
  if (!url) return ''
  if (isExternal(url)) return url
  return baseUrl + url
}

function getList() {
  loading.value = true
  listMemberConfig(queryParams.value).then(response => {
    configList.value = response.rows
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
    configType: undefined,
    imageUrl: undefined,
    articleLink: undefined,
    richContent: undefined,
    status: "0",
    remark: undefined
  }
  proxy.resetForm("configRef")
}

function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

function resetQuery() {
  proxy.resetForm("queryRef")
  handleQuery()
}

function handleUpdate(row) {
  reset()
  form.value = {
    id: row.id,
    configType: row.configType,
    imageUrl: row.imageUrl,
    articleLink: row.articleLink,
    richContent: row.richContent,
    status: row.status,
    remark: row.remark
  }
  open.value = true
  title.value = "编辑配置"
}

function submitForm() {
  proxy.$refs["configRef"].validate(valid => {
    if (valid) {
      saveMemberConfig(form.value).then(() => {
        proxy.$modal.msgSuccess("保存成功")
        open.value = false
        getList()
      })
    }
  })
}

getList()
</script>
