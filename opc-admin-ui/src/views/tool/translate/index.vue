<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!-- 翻译输入区域 -->
      <el-col :span="24">
        <el-card class="translate-card">
          <template #header>
            <div class="card-header">
              <span><el-icon><Document /></el-icon> DeepLX 翻译</span>
              <el-button type="primary" link @click="getLanguages">
                <el-icon><Refresh /></el-icon> 刷新语言列表
              </el-button>
            </div>
          </template>
          
          <el-form :model="translateForm" label-width="100px">
            <el-row :gutter="20">
              <el-col :xs="24" :sm="12">
                <el-form-item label="源语言">
                  <el-select v-model="translateForm.sourceLang" placeholder="选择源语言" style="width: 100%">
                    <el-option label="自动检测" value="auto" />
                    <el-option 
                      v-for="(name, code) in languages" 
                      :key="code" 
                      :label="name" 
                      :value="code" 
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12">
                <el-form-item label="目标语言">
                  <el-select v-model="translateForm.targetLang" placeholder="选择目标语言" style="width: 100%">
                    <el-option 
                      v-for="(name, code) in languages" 
                      :key="code" 
                      :label="name" 
                      :value="code" 
                    />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20" class="translate-row">
              <el-col :xs="24" :sm="11">
                <el-form-item label="输入文本">
                  <el-input
                    v-model="translateForm.text"
                    type="textarea"
                    :rows="8"
                    placeholder="请输入要翻译的文本..."
                    resize="none"
                  />
                  <div class="text-actions">
                    <el-button link type="info" @click="clearText">
                      <el-icon><Delete /></el-icon> 清空
                    </el-button>
                    <el-button link type="info" @click="pasteText">
                      <el-icon><DocumentCopy /></el-icon> 粘贴
                    </el-button>
                  </div>
                </el-form-item>
              </el-col>
              
              <el-col :xs="24" :sm="2" class="swap-col">
                <el-button 
                  type="primary" 
                  circle 
                  @click="swapLanguages"
                  :disabled="translateForm.sourceLang === 'auto'"
                >
                  <el-icon><Switch /></el-icon>
                </el-button>
              </el-col>
              
              <el-col :xs="24" :sm="11">
                <el-form-item label="翻译结果">
                  <el-input
                    v-model="translatedText"
                    type="textarea"
                    :rows="8"
                    placeholder="翻译结果将显示在这里..."
                    resize="none"
                    readonly
                  />
                  <div class="text-actions">
                    <el-button link type="success" @click="copyResult" v-if="translatedText">
                      <el-icon><CopyDocument /></el-icon> 复制结果
                    </el-button>
                  </div>
                </el-form-item>
              </el-col>
            </el-row>

            <el-form-item>
              <el-button type="primary" @click="handleTranslate" :loading="loading" size="large">
                <el-icon><Promotion /></el-icon> 翻译
              </el-button>
              <el-button @click="quickTranslate('toChinese')" :loading="loading">
                <el-icon><ArrowRight /></el-icon> 转中文
              </el-button>
              <el-button @click="quickTranslate('toEnglish')" :loading="loading">
                <el-icon><ArrowRight /></el-icon> 转英文
              </el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>

    <!-- 批量翻译 -->
    <el-row :gutter="20" class="mt20">
      <el-col :span="24">
        <el-card>
          <template #header>
            <div class="card-header">
              <span><el-icon><List /></el-icon> 批量翻译</span>
            </div>
          </template>
          
          <el-alert
            title="每行输入一个文本，将批量翻译"
            type="info"
            :closable="false"
            class="mb15"
          />
          
          <el-row :gutter="20">
            <el-col :xs="24" :sm="11">
              <el-input
                v-model="batchForm.texts"
                type="textarea"
                :rows="6"
                placeholder="请输入多行文本，每行一个..."
                resize="none"
              />
            </el-col>
            <el-col :xs="24" :sm="2" class="swap-col">
              <el-button type="primary" @click="handleBatchTranslate" :loading="batchLoading">
                <el-icon><DArrowRight /></el-icon>
              </el-button>
            </el-col>
            <el-col :xs="24" :sm="11">
              <el-input
                v-model="batchResult"
                type="textarea"
                :rows="6"
                placeholder="批量翻译结果..."
                resize="none"
                readonly
              />
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>

    <!-- 语言列表 -->
    <el-row :gutter="20" class="mt20">
      <el-col :span="24">
        <el-card>
          <template #header>
            <div class="card-header">
              <span><el-icon><InfoFilled /></el-icon> 支持的语言</span>
            </div>
          </template>
          <el-descriptions :column="4" border>
            <el-descriptions-item 
              v-for="(name, code) in languages" 
              :key="code"
              :label="code"
            >
              {{ name }}
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup name="Translate">
import { translate, translateBatch, getLanguages as getLanguagesApi, translateToChinese, translateToEnglish } from "@/api/tool/translate"

const { proxy } = getCurrentInstance()

const loading = ref(false)
const batchLoading = ref(false)
const translatedText = ref('')
const batchResult = ref('')
const languages = ref({})

const translateForm = reactive({
  text: '',
  sourceLang: 'auto',
  targetLang: 'ZH'
})

const batchForm = reactive({
  texts: '',
  sourceLang: 'auto',
  targetLang: 'ZH'
})

// 获取语言列表
function getLanguages() {
  getLanguagesApi().then(response => {
    languages.value = response.data
    proxy.$modal.msgSuccess("语言列表已更新")
  })
}

// 翻译
function handleTranslate() {
  if (!translateForm.text.trim()) {
    proxy.$modal.msgError("请输入要翻译的文本")
    return
  }
  
  loading.value = true
  translate({
    text: translateForm.text,
    sourceLang: translateForm.sourceLang,
    targetLang: translateForm.targetLang
  }).then(response => {
    translatedText.value = response.data.translatedText
    loading.value = false
  }).catch(() => {
    loading.value = false
  })
}

// 快速翻译
function quickTranslate(type) {
  if (!translateForm.text.trim()) {
    proxy.$modal.msgError("请输入要翻译的文本")
    return
  }
  
  loading.value = true
  const api = type === 'toChinese' ? translateToChinese : translateToEnglish
  api({ text: translateForm.text }).then(response => {
    translatedText.value = response.data.translatedText
    // 更新目标语言
    translateForm.targetLang = type === 'toChinese' ? 'ZH' : 'EN'
    loading.value = false
  }).catch(() => {
    loading.value = false
  })
}

// 批量翻译
function handleBatchTranslate() {
  if (!batchForm.texts.trim()) {
    proxy.$modal.msgError("请输入要翻译的文本")
    return
  }
  
  const texts = batchForm.texts.split('\n').filter(t => t.trim())
  if (texts.length === 0) {
    proxy.$modal.msgError("请输入有效的文本")
    return
  }
  
  batchLoading.value = true
  translateBatch({
    texts: texts,
    sourceLang: translateForm.sourceLang,
    targetLang: translateForm.targetLang
  }).then(response => {
    batchResult.value = response.data.translatedTexts.join('\n')
    batchLoading.value = false
  }).catch(() => {
    batchLoading.value = false
  })
}

// 交换语言
function swapLanguages() {
  const temp = translateForm.sourceLang
  translateForm.sourceLang = translateForm.targetLang
  translateForm.targetLang = temp
}

// 清空文本
function clearText() {
  translateForm.text = ''
  translatedText.value = ''
}

// 粘贴文本
async function pasteText() {
  try {
    const text = await navigator.clipboard.readText()
    translateForm.text = text
    proxy.$modal.msgSuccess("粘贴成功")
  } catch (err) {
    proxy.$modal.msgError("无法访问剪贴板，请手动粘贴")
  }
}

// 复制结果
function copyResult() {
  navigator.clipboard.writeText(translatedText.value).then(() => {
    proxy.$modal.msgSuccess("复制成功")
  }).catch(() => {
    proxy.$modal.msgError("复制失败")
  })
}

// 初始化
getLanguages()
</script>

<style scoped lang="scss">
.translate-card {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
}

.translate-row {
  display: flex;
  align-items: center;
}

.swap-col {
  display: flex;
  justify-content: center;
  align-items: center;
}

.text-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 8px;
}

.mt20 {
  margin-top: 20px;
}

.mb15 {
  margin-bottom: 15px;
}
</style>
