<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="激活码" prop="code">
        <el-input
          v-model="queryParams.code"
          placeholder="请输入激活码"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="批次号" prop="batchNo">
        <el-input
          v-model="queryParams.batchNo"
          placeholder="请输入批次号"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="渠道标签" prop="channelTag">
        <el-input
          v-model="queryParams.channelTag"
          placeholder="请输入渠道标签"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option label="未使用" value="0" />
          <el-option label="已发送-未使用" value="1" />
          <el-option label="已发送-已使用" value="2" />
          <el-option label="已注销" value="3" />
          <el-option label="已过期" value="4" />
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
          @click="handleGenerate"
          v-hasPermi="['core:activationCode:add']"
        >生成激活码</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="multiple"
          @click="handleUpdate"
          v-hasPermi="['core:activationCode:edit']"
        >批量修改渠道</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="Position"
          :disabled="multiple"
          @click="handleBatchSend"
          v-hasPermi="['core:activationCode:send']"
        >批量发送</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['core:activationCode:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['core:activationCode:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="activationCodeList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" width="80" />
      <el-table-column label="激活码" align="center" prop="code" width="200" show-overflow-tooltip />
      <el-table-column label="渠道标签" align="center" prop="channelTag" />
      <el-table-column label="批次号" align="center" prop="batchNo" width="180" />
      <el-table-column label="有效天数" align="center" prop="validDays" width="80" />
      <el-table-column label="过期时间" align="center" prop="expireTime" width="120">
        <template #default="scope">
          <span>{{ parseTime(scope.row.expireTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" width="120">
        <template #default="scope">
          <el-tag :type="getStatusType(scope.row.status)">
            {{ getStatusLabel(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="发送时间" align="center" prop="sendTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.sendTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="使用时间" align="center" prop="useTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.useTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="生成时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200" fixed="right">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['core:activationCode:edit']">修改渠道</el-button>
          <el-button 
            link 
            type="success" 
            icon="Position" 
            @click="handleSend(scope.row)" 
            v-hasPermi="['core:activationCode:send']"
            :disabled="scope.row.status !== '0'"
          >发送</el-button>
          <el-button 
            link 
            type="danger" 
            icon="CircleClose" 
            @click="handleCancel(scope.row)" 
            v-hasPermi="['core:activationCode:cancel']"
            :disabled="scope.row.status === '2' || scope.row.status === '3'"
          >注销</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 生成激活码对话框 -->
    <el-dialog title="生成激活码" v-model="generateOpen" width="500px" append-to-body>
      <el-form ref="generateRef" :model="generateForm" :rules="generateRules" label-width="100px">
        <el-form-item label="生成个数" prop="count">
          <el-input-number v-model="generateForm.count" :min="1" :max="1000" />
        </el-form-item>
        <el-form-item label="有效天数" prop="validDays">
          <el-input-number v-model="generateForm.validDays" :min="1" :max="365" />
        </el-form-item>
        <el-form-item label="渠道标签" prop="channelTag">
          <el-input v-model="generateForm.channelTag" placeholder="请输入渠道标签，如：抖音、微信" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitGenerate">确 定</el-button>
          <el-button @click="cancelGenerate">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 批量修改渠道标签对话框 -->
    <el-dialog title="批量修改渠道标签" v-model="open" width="500px" append-to-body>
      <el-form ref="activationCodeRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="选中数量" prop="ids">
          <el-input :value="form.ids ? form.ids.length + ' 条' : '0 条'" disabled />
        </el-form-item>
        <el-form-item label="渠道标签" prop="channelTag">
          <el-input v-model="form.channelTag" placeholder="请输入渠道标签" />
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

<script setup name="ActivationCode">
import { listActivationCode, getActivationCode, generateActivationCode, updateActivationCode, delActivationCode, sendActivationCode, cancelActivationCode } from "@/api/core/activationCode";
import { parseTime } from '@/utils/ruoyi'

const { proxy } = getCurrentInstance();

const activationCodeList = ref([]);
const open = ref(false);
const generateOpen = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

const statusMap = {
  '0': { label: '未使用', type: 'info' },
  '1': { label: '已发送-未使用', type: 'warning' },
  '2': { label: '已发送-已使用', type: 'success' },
  '3': { label: '已注销', type: 'danger' },
  '4': { label: '已过期', type: 'info' }
}

const data = reactive({
  form: {},
  generateForm: {
    count: 10,
    validDays: 30,
    channelTag: ''
  },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    code: null,
    batchNo: null,
    channelTag: null,
    status: null
  },
  rules: {
    channelTag: [
      { required: true, message: "渠道标签不能为空", trigger: "blur" }
    ]
  },
  generateRules: {
    count: [
      { required: true, message: "生成个数不能为空", trigger: "blur" }
    ],
    validDays: [
      { required: true, message: "有效天数不能为空", trigger: "blur" }
    ]
  }
});

const { queryParams, form, generateForm, rules, generateRules } = toRefs(data);

/** 查询激活码列表 */
function getList() {
  loading.value = true;
  listActivationCode(queryParams.value).then(response => {
    activationCodeList.value = response.data;
    total.value = response.total;
    loading.value = false;
  });
}

/** 取消按钮 */
function cancel() {
  open.value = false;
  reset();
}

/** 取消生成 */
function cancelGenerate() {
  generateOpen.value = false;
  resetGenerate();
}

/** 表单重置 */
function reset() {
  form.value = {
    ids: [],
    channelTag: null
  };
  proxy.resetForm("activationCodeRef");
}

/** 重置生成表单 */
function resetGenerate() {
  generateForm.value = {
    count: 10,
    validDays: 30,
    channelTag: ''
  };
  proxy.resetForm("generateRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id);
  single.value = selection.length !== 1;
  multiple.value = !selection.length;
}

/** 生成激活码按钮操作 */
function handleGenerate() {
  resetGenerate();
  generateOpen.value = true;
}

/** 提交生成 */
function submitGenerate() {
  proxy.$refs["generateRef"].validate(valid => {
    if (valid) {
      generateActivationCode(generateForm.value).then(response => {
        proxy.$modal.msgSuccess("生成成功");
        generateOpen.value = false;
        getList();
      });
    }
  });
}

/** 批量修改按钮操作 */
function handleUpdate(row) {
  reset();
  const updateIds = row.id ? [row.id] : ids.value;
  if (!updateIds || updateIds.length === 0) {
    proxy.$modal.msgWarning("请选择要修改的数据");
    return;
  }
  form.value.ids = updateIds;
  // 单条修改时，回显当前渠道标签
  if (row.id) {
    form.value.channelTag = row.channelTag;
  }
  open.value = true;
}

/** 提交按钮（批量修改渠道） */
function submitForm() {
  proxy.$refs["activationCodeRef"].validate(valid => {
    if (valid) {
      // 批量修改，循环调用接口
      const promises = form.value.ids.map(id => {
        return updateActivationCode({ id: id, channelTag: form.value.channelTag });
      });
      Promise.all(promises).then(() => {
        proxy.$modal.msgSuccess("批量修改成功");
        open.value = false;
        getList();
      }).catch(() => {
        proxy.$modal.msgError("批量修改失败");
      });
    }
  });
}

/** 批量发送按钮操作 */
function handleBatchSend() {
  const sendIds = ids.value;
  if (!sendIds || sendIds.length === 0) {
    proxy.$modal.msgWarning("请选择要发送的激活码");
    return;
  }
  proxy.$modal.confirm('确认批量发送选中的激活码吗？').then(function() {
    return sendActivationCode(sendIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("批量发送成功");
  }).catch(() => {});
}

/** 发送按钮操作 */
function handleSend(row) {
  proxy.$modal.confirm('确认发送该激活码吗？').then(function() {
    return sendActivationCode(row.id);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("发送成功");
  }).catch(() => {});
}

/** 注销按钮操作 */
function handleCancel(row) {
  proxy.$modal.confirm('确认注销该激活码吗？注销后无法恢复').then(function() {
    return cancelActivationCode(row.id);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("注销成功");
  }).catch(() => {});
}

/** 删除按钮操作 */
function handleDelete(row) {
  const delIds = row.id || ids.value;
  proxy.$modal.confirm('是否确认删除选中的数据项？').then(function() {
    return delActivationCode(delIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('core/activationCode/export', {
    ...queryParams.value
  }, `activationCode_${new Date().getTime()}.xlsx`);
}

/** 获取状态标签 */
function getStatusLabel(status) {
  return statusMap[status]?.label || status;
}

/** 获取状态类型 */
function getStatusType(status) {
  return statusMap[status]?.type || 'info';
}

getList();
</script>
