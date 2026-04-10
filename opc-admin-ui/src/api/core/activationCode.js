import request from '@/utils/request'

// 查询激活码列表
export function listActivationCode(query) {
  return request({
    url: '/core/activationCode/list',
    method: 'get',
    params: query
  })
}

// 查询激活码详细
export function getActivationCode(id) {
  return request({
    url: '/core/activationCode/' + id,
    method: 'get'
  })
}

// 批量生成激活码
export function generateActivationCode(data) {
  return request({
    url: '/core/activationCode/generate',
    method: 'post',
    params: data
  })
}

// 修改激活码
export function updateActivationCode(data) {
  return request({
    url: '/core/activationCode',
    method: 'put',
    data: data
  })
}

// 删除激活码
export function delActivationCode(id) {
  return request({
    url: '/core/activationCode/' + id,
    method: 'delete'
  })
}

// 发送激活码
export function sendActivationCode(ids) {
  return request({
    url: '/core/activationCode/send/' + ids,
    method: 'put'
  })
}

// 注销激活码
export function cancelActivationCode(ids) {
  return request({
    url: '/core/activationCode/cancel/' + ids,
    method: 'put'
  })
}
