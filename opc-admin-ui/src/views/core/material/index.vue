<template>
   <div class="app-container">
      <div class="material-tab">
         <el-radio-group v-model="activeTab" @change="handleTabChange" class="tab-group">
            <el-radio-button value="1">
               <el-icon><User /></el-icon>
               普通素材
            </el-radio-button>
            <el-radio-button value="2">
               <el-icon><Star /></el-icon>
               VIP素材
            </el-radio-button>
            <el-radio-button value="3">
               <el-icon><Medal /></el-icon>
               超级VIP
            </el-radio-button>
         </el-radio-group>
      </div>

      <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
         <el-form-item label="标题" prop="title">
            <el-input
               v-model="queryParams.title"
               placeholder="请输入标题"
               clearable
               style="width: 200px"
               @keyup.enter="handleQuery"
            />
         </el-form-item>
         <el-form-item label="作者" prop="author">
            <el-input
               v-model="queryParams.author"
               placeholder="请输入作者"
               clearable
               style="width: 200px"
               @keyup.enter="handleQuery"
            />
         </el-form-item>
         <el-form-item label="状态" prop="status">
            <el-select v-model="queryParams.status" placeholder="全部" clearable style="width: 200px">
               <el-option label="上线" value="0" />
               <el-option label="下线" value="1" />
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
               v-hasPermi="['core:material:add']"
            >新增</el-button>
         </el-col>
         <el-col :span="1.5">
            <el-button
               type="danger"
               plain
               icon="Delete"
               :disabled="multiple"
               @click="handleDelete"
               v-hasPermi="['core:material:remove']"
            >删除</el-button>
         </el-col>
         <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="materialList" @selection-change="handleSelectionChange">
         <el-table-column type="selection" width="55" align="center" />
         <el-table-column label="素材ID" align="center" prop="id" width="80" />
         <el-table-column label="标题" align="center" prop="title" :show-overflow-tooltip="true" />
         <el-table-column label="作者" align="center" prop="author" width="120" />
         <el-table-column label="分类" align="center" prop="category" width="100">
            <template #default="scope">
               <span>{{ getCategoryLabel(scope.row.category) }}</span>
            </template>
         </el-table-column>
         <el-table-column label="来源" align="center" prop="source" width="80">
            <template #default="scope">
               <span>{{ scope.row.source === 'crawler' ? '爬取' : '手动' }}</span>
            </template>
         </el-table-column>
         <el-table-column label="回复数" align="center" prop="replyCount" width="80" />
         <el-table-column label="点赞数" align="center" prop="likeCount" width="80" />
         <el-table-column label="查看数" align="center" prop="viewCount" width="80" />
         <el-table-column label="转发数" align="center" prop="shareCount" width="80" />
         <el-table-column label="评论数" align="center" prop="commentCount" width="80" />
         <el-table-column label="发布时间" align="center" prop="publishTime" width="160">
            <template #default="scope">
               <span>{{ parseTime(scope.row.publishTime) }}</span>
            </template>
         </el-table-column>
         <el-table-column label="状态" align="center" prop="status" width="80">
            <template #default="scope">
               <el-tag :type="scope.row.status === '0' ? 'success' : 'danger'">
                  {{ scope.row.status === '0' ? '上线' : '下线' }}
               </el-tag>
            </template>
         </el-table-column>
         <el-table-column label="置顶" align="center" prop="isTop" width="80">
            <template #default="scope">
               <el-tag :type="scope.row.isTop === '1' ? 'warning' : 'info'">
                  {{ scope.row.isTop === '1' ? '是' : '否' }}
               </el-tag>
            </template>
         </el-table-column>
         <el-table-column label="操作" width="280" align="center" class-name="small-padding fixed-width">
            <template #default="scope">
               <el-button link type="primary" icon="View" @click="handleView(scope.row)" v-hasPermi="['core:material:query']">详情</el-button>
               <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['core:material:edit']">编辑</el-button>
               <el-button
                  link
                  :type="scope.row.status === '0' ? 'danger' : 'success'"
                  :icon="scope.row.status === '0' ? 'CircleClose' : 'CircleCheck'"
                  @click="handleChangeStatus(scope.row)"
                  v-hasPermi="['core:material:changeStatus']"
               >{{ scope.row.status === '0' ? '下线' : '上线' }}</el-button>
               <el-button
                  link
                  :type="scope.row.isTop === '1' ? 'warning' : 'primary'"
                  :icon="scope.row.isTop === '1' ? 'Top' : 'Rank'"
                  @click="handleChangeTop(scope.row)"
                  v-hasPermi="['core:material:changeTop']"
               >{{ scope.row.isTop === '1' ? '取消置顶' : '置顶' }}</el-button>
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

      <el-dialog :title="title" v-model="open" width="1000px" append-to-body>
         <el-form ref="materialRef" :model="form" :rules="rules" label-width="100px">
            <el-row>
               <el-col :span="12">
                  <el-form-item label="标题" prop="title">
                     <el-input v-model="form.title" placeholder="请输入标题" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item label="作者" prop="author">
                     <el-input v-model="form.author" placeholder="请输入作者（注明出处）" />
                  </el-form-item>
               </el-col>
            </el-row>
            <el-row>
               <el-col :span="12">
                  <el-form-item label="来源" prop="source">
                     <el-select v-model="form.source" placeholder="请选择来源" style="width: 100%">
                        <el-option label="爬取" value="crawler" />
                        <el-option label="手动" value="manual" />
                     </el-select>
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item label="分类" prop="category">
                     <el-select v-model="form.category" placeholder="请选择分类" style="width: 100%">
                        <el-option label="普通素材" value="normal" />
                        <el-option label="大咖专栏" value="vip_column" />
                        <el-option label="VIP素材" value="vip" />
                        <el-option label="超级VIP" value="svip" />
                     </el-select>
                  </el-form-item>
               </el-col>
            </el-row>
            <el-row>
               <el-col :span="12">
                  <el-form-item label="发布时间" prop="publishTime">
                     <el-date-picker
                        v-model="form.publishTime"
                        type="datetime"
                        placeholder="选择发布时间"
                        style="width: 100%"
                     />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item label="查看权限" prop="viewPermission">
                     <el-select v-model="form.viewPermission" placeholder="请选择查看权限" style="width: 100%">
                        <el-option label="一级套餐" :value="1" />
                        <el-option label="二级套餐" :value="2" />
                        <el-option label="三级套餐" :value="3" />
                     </el-select>
                  </el-form-item>
               </el-col>
            </el-row>
            <el-row>
               <el-col :span="12">
                  <el-form-item label="内容类型" prop="contentType">
                     <el-select v-model="form.contentType" placeholder="请选择内容类型" style="width: 100%">
                        <el-option label="纯文本" value="text" />
                        <el-option label="图文" value="image" />
                        <el-option label="视频" value="video" />
                     </el-select>
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item label="状态" prop="status">
                     <el-radio-group v-model="form.status">
                        <el-radio label="0">上线</el-radio>
                        <el-radio label="1">下线</el-radio>
                     </el-radio-group>
                  </el-form-item>
               </el-col>
            </el-row>
            <el-form-item label="原链接" prop="originalUrl">
               <el-input v-model="form.originalUrl" placeholder="请输入原链接" />
            </el-form-item>
            <el-form-item label="原ID" prop="originalId">
               <el-input v-model="form.originalId" placeholder="请输入原ID" />
            </el-form-item>
            <el-row>
               <el-col :span="8">
                  <el-form-item label="回复数" prop="replyCount">
                     <el-input-number v-model="form.replyCount" :min="0" placeholder="请输入回复数" style="width: 100%" />
                  </el-form-item>
               </el-col>
               <el-col :span="8">
                  <el-form-item label="点赞数" prop="likeCount">
                     <el-input-number v-model="form.likeCount" :min="0" placeholder="请输入点赞数" style="width: 100%" />
                  </el-form-item>
               </el-col>
               <el-col :span="8">
                  <el-form-item label="查看数" prop="viewCount">
                     <el-input-number v-model="form.viewCount" :min="0" placeholder="请输入查看数" style="width: 100%" />
                  </el-form-item>
               </el-col>
            </el-row>
            <el-row>
               <el-col :span="12">
                  <el-form-item label="转发数" prop="shareCount">
                     <el-input-number v-model="form.shareCount" :min="0" placeholder="请输入转发数" style="width: 100%" />
                  </el-form-item>
               </el-col>
               <el-col :span="12">
                  <el-form-item label="评论数" prop="commentCount">
                     <el-input-number v-model="form.commentCount" :min="0" placeholder="请输入评论数" style="width: 100%" />
                  </el-form-item>
               </el-col>
            </el-row>
            <el-form-item label="正文" prop="content">
               <Editor v-model="form.content" :min-height="300" />
            </el-form-item>
            <el-form-item label="总结" prop="summary">
               <el-input v-model="form.summary" type="textarea" placeholder="请输入总结" :rows="2" />
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

      <el-dialog title="素材详情" v-model="detailOpen" width="800px" append-to-body>
         <el-descriptions :column="2" border v-if="detailData">
            <el-descriptions-item label="素材ID" :span="1">{{ detailData.id }}</el-descriptions-item>
            <el-descriptions-item label="标题" :span="1">{{ detailData.title }}</el-descriptions-item>
            <el-descriptions-item label="作者" :span="1">{{ detailData.author || '-' }}</el-descriptions-item>
            <el-descriptions-item label="来源" :span="1">
               <span>{{ detailData.source === 'crawler' ? '爬取' : '手动' }}</span>
            </el-descriptions-item>
            <el-descriptions-item label="原ID" :span="1">{{ detailData.originalId || '-' }}</el-descriptions-item>
            <el-descriptions-item label="回复数" :span="1">{{ detailData.replyCount || 0 }}</el-descriptions-item>
            <el-descriptions-item label="点赞数" :span="1">{{ detailData.likeCount || 0 }}</el-descriptions-item>
            <el-descriptions-item label="查看数" :span="1">{{ detailData.viewCount || 0 }}</el-descriptions-item>
            <el-descriptions-item label="转发数" :span="1">{{ detailData.shareCount || 0 }}</el-descriptions-item>
            <el-descriptions-item label="评论数" :span="1">{{ detailData.commentCount || 0 }}</el-descriptions-item>
            <el-descriptions-item label="分类" :span="1">{{ getCategoryLabel(detailData.category) }}</el-descriptions-item>
            <el-descriptions-item label="查看权限" :span="1">
               <el-tag type="success">{{ detailData.viewPermission }}级套餐</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="内容类型" :span="1">
               <span>{{ detailData.contentType === 'text' ? '纯文本' : detailData.contentType === 'image' ? '图文' : '视频' }}</span>
            </el-descriptions-item>
            <el-descriptions-item label="状态" :span="1">
               <el-tag :type="detailData.status === '0' ? 'success' : 'danger'">
                  {{ detailData.status === '0' ? '上线' : '下线' }}
               </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="发布时间" :span="1">{{ parseTime(detailData.publishTime) || '-' }}</el-descriptions-item>
            <el-descriptions-item label="置顶" :span="1">
               <el-tag :type="detailData.isTop === '1' ? 'warning' : 'info'">
                  {{ detailData.isTop === '1' ? '是' : '否' }}
               </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="原链接" :span="2">
               <a v-if="detailData.originalUrl" :href="detailData.originalUrl" target="_blank">{{ detailData.originalUrl }}</a>
               <span v-else>-</span>
            </el-descriptions-item>
            <el-descriptions-item label="总结" :span="2">{{ detailData.summary || '-' }}</el-descriptions-item>
            <el-descriptions-item label="正文" :span="2">
               <div v-if="detailData.content" style="max-height: 400px; overflow-y: auto; border: 1px solid #e4e7ed; padding: 10px; border-radius: 4px;" v-html="detailData.content"></div>
               <span v-else>-</span>
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

<script setup name="Material">
import { listMaterial, addMaterial, getMaterial, updateMaterial, delMaterial, changeMaterialStatus, changeMaterialTop } from "@/api/core/material"
import { User, Star, Medal } from '@element-plus/icons-vue'
import Editor from "@/components/Editor/index.vue"

const { proxy } = getCurrentInstance()

const materialList = ref([])
const open = ref(false)
const detailOpen = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
const activeTab = ref("1")
const detailData = ref({})

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    title: undefined,
    author: undefined,
    status: undefined,
    viewPermission: 1
  },
  rules: {
    title: [{ required: true, message: "标题不能为空", trigger: "blur" }],
  }
})

const { queryParams, form, rules } = toRefs(data)

function getCategoryLabel(category) {
  const map = {
    'normal': '普通素材',
    'vip_column': '大咖专栏',
    'vip': 'VIP素材',
    'svip': '超级VIP'
  }
  return map[category] || category || '-'
}

function getList() {
  loading.value = true
  listMaterial(queryParams.value).then(response => {
    materialList.value = response.rows
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
    title: undefined,
    author: undefined,
    summary: undefined,
    content: undefined,
    originalUrl: undefined,
    originalId: undefined,
    replyCount: 0,
    likeCount: 0,
    viewCount: 0,
    shareCount: 0,
    commentCount: 0,
    publishTime: undefined,
    viewPermission: parseInt(activeTab.value),
    contentType: 'text',
    category: 'normal',
    status: '0',
    isTop: '0',
    source: 'manual',
    remark: undefined
  }
  proxy.resetForm("materialRef")
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

function handleTabChange(tabName) {
  queryParams.value.viewPermission = parseInt(tabName)
  queryParams.value.pageNum = 1
  getList()
}

function handleAdd() {
  reset()
  form.value.viewPermission = parseInt(activeTab.value)
  if (activeTab.value === '1') {
    form.value.category = 'normal'
  } else if (activeTab.value === '2') {
    form.value.category = 'vip_column'
  } else {
    form.value.category = 'svip'
  }
  open.value = true
  title.value = "添加素材"
}

function handleView(row) {
  getMaterial(row.id).then(response => {
    detailData.value = response.data
    detailOpen.value = true
  })
}

function handleUpdate(row) {
  reset()
  const id = row.id || ids.value
  getMaterial(id).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改素材"
  })
}

function handleDelete() {
  const materialIds = ids.value
  proxy.$modal.confirm('是否确认删除素材编号为"' + materialIds + '"的数据项？').then(function() {
    return delMaterial(materialIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

function handleChangeStatus(row) {
  const text = row.status === '0' ? "下线" : "上线"
  const newStatus = row.status === '0' ? '1' : '0'
  proxy.$modal.confirm('确认要"' + text + '"素材"' + row.title + '"吗？').then(function() {
    return changeMaterialStatus(row.id, newStatus)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess(text + "成功")
  }).catch(() => {})
}

function handleChangeTop(row) {
  const text = row.isTop === '0' ? "置顶" : "取消置顶"
  const newIsTop = row.isTop === '0' ? '1' : '0'
  proxy.$modal.confirm('确认要"' + text + '"素材"' + row.title + '"吗？').then(function() {
    return changeMaterialTop(row.id, newIsTop)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess(text + "成功")
  }).catch(() => {})
}

function submitForm() {
  proxy.$refs["materialRef"].validate(valid => {
    if (valid) {
      if (form.value.id != undefined) {
        updateMaterial(form.value).then(() => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addMaterial(form.value).then(() => {
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
.material-tab {
  margin-bottom: 20px;
}
.tab-group :deep(.el-radio-button__inner) {
  padding: 10px 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
}
.tab-group :deep(.el-radio-button__original-radio:checked + .el-radio-button__inner) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-color: #667eea;
}
</style>
