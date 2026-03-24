import request from '@/utils/request'

export function listTag(query) {
  return request({
    url: '/core/tag/list',
    method: 'get',
    params: query
  })
}

export function getAllActiveTags() {
  return request({
    url: '/core/tag/all',
    method: 'get'
  })
}

export function getTag(id) {
  return request({
    url: '/core/tag/' + id,
    method: 'get'
  })
}

export function addTag(data) {
  return request({
    url: '/core/tag',
    method: 'post',
    data: data
  })
}

export function updateTag(data) {
  return request({
    url: '/core/tag',
    method: 'put',
    data: data
  })
}

export function delTag(id) {
  return request({
    url: '/core/tag/' + id,
    method: 'delete'
  })
}

export function exportTag(query) {
  return request({
    url: '/core/tag/export',
    method: 'post',
    data: query,
    responseType: 'blob'
  })
}
