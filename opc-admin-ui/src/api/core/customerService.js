import request from '@/utils/request'

// 查询客服配置列表
export function listCustomerService(query) {
  return request({
    url: '/core/customerService/list',
    method: 'get',
    params: query
  })
}

// 查询客服配置详细
export function getCustomerService(id) {
  return request({
    url: '/core/customerService/' + id,
    method: 'get'
  })
}

// 获取默认客服配置
export function getDefaultCustomerService() {
  return request({
    url: '/core/customerService/default',
    method: 'get'
  })
}

// 新增客服配置
export function addCustomerService(data) {
  return request({
    url: '/core/customerService',
    method: 'post',
    data: data
  })
}

// 修改客服配置
export function updateCustomerService(data) {
  return request({
    url: '/core/customerService',
    method: 'put',
    data: data
  })
}

// 删除客服配置
export function delCustomerService(id) {
  return request({
    url: '/core/customerService/' + id,
    method: 'delete'
  })
}

// 批量删除客服配置
export function batchDelCustomerService(ids) {
  return request({
    url: '/core/customerService/batch/' + ids,
    method: 'delete'
  })
}
