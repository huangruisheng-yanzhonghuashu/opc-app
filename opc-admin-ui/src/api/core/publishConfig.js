import request from '@/utils/request'

// 查询发布配置列表
export function listPublishConfig(query) {
  return request({
    url: '/core/publishConfig/list',
    method: 'get',
    params: query
  })
}

// 查询发布配置详细
export function getPublishConfig(id) {
  return request({
    url: '/core/publishConfig/' + id,
    method: 'get'
  })
}

// 新增发布配置
export function addPublishConfig(data) {
  return request({
    url: '/core/publishConfig',
    method: 'post',
    data: data
  })
}

// 修改发布配置
export function updatePublishConfig(data) {
  return request({
    url: '/core/publishConfig',
    method: 'put',
    data: data
  })
}

// 删除发布配置
export function delPublishConfig(id) {
  return request({
    url: '/core/publishConfig/' + id,
    method: 'delete'
  })
}

// 导出发布配置
export function exportPublishConfig(query) {
  return request({
    url: '/core/publishConfig/export',
    method: 'post',
    data: query,
    responseType: 'blob'
  })
}
