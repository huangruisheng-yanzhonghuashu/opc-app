import request from '@/utils/request'

// 翻译文本
export function translate(data) {
  return request({
    url: '/translate/text',
    method: 'post',
    params: data
  })
}

// 翻译为中文
export function translateToChinese(data) {
  return request({
    url: '/translate/toChinese',
    method: 'post',
    params: data
  })
}

// 翻译为英文
export function translateToEnglish(data) {
  return request({
    url: '/translate/toEnglish',
    method: 'post',
    params: data
  })
}

// 批量翻译
export function translateBatch(data) {
  return request({
    url: '/translate/batch',
    method: 'post',
    params: data
  })
}

// 获取支持的语言列表
export function getLanguages() {
  return request({
    url: '/translate/languages',
    method: 'get'
  })
}
