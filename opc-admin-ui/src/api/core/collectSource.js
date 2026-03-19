import request from '@/utils/request'

export function listCollectSource(query) {
  return request({
    url: '/core/collect/list',
    method: 'get',
    params: query
  })
}

export function getCollectSource(id) {
  return request({
    url: '/core/collect/' + id,
    method: 'get'
  })
}

export function addCollectSource(data) {
  return request({
    url: '/core/collect',
    method: 'post',
    data: data
  })
}

export function updateCollectSource(data) {
  return request({
    url: '/core/collect',
    method: 'put',
    data: data
  })
}

export function delCollectSource(id) {
  return request({
    url: '/core/collect/' + id,
    method: 'delete'
  })
}

export function changeCollectSourceStatus(id, status) {
  return request({
    url: '/core/collect/changeStatus',
    method: 'put',
    data: { id: id, status: status }
  })
}
