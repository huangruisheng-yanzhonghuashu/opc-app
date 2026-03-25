import request from '@/utils/request'

export function listCommunity(query) {
  return request({
    url: '/core/community/list',
    method: 'get',
    params: query
  })
}

export function getCommunity(id) {
  return request({
    url: '/core/community/' + id,
    method: 'get'
  })
}

export function addCommunity(data) {
  return request({
    url: '/core/community',
    method: 'post',
    data: data
  })
}

export function updateCommunity(data) {
  return request({
    url: '/core/community',
    method: 'put',
    data: data
  })
}

export function delCommunity(ids) {
  return request({
    url: '/core/community/' + ids,
    method: 'delete'
  })
}

export function exportCommunity(query) {
  return request({
    url: '/core/community/export',
    method: 'post',
    data: query,
    responseType: 'blob'
  })
}
