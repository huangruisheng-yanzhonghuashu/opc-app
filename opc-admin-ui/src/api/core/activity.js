import request from '@/utils/request'

export function listActivity(query) {
  return request({
    url: '/core/activity/list',
    method: 'get',
    params: query
  })
}

export function getActivity(id) {
  return request({
    url: '/core/activity/' + id,
    method: 'get'
  })
}

export function addActivity(data) {
  return request({
    url: '/core/activity',
    method: 'post',
    data: data
  })
}

export function updateActivity(data) {
  return request({
    url: '/core/activity',
    method: 'put',
    data: data
  })
}

export function delActivity(id) {
  return request({
    url: '/core/activity/' + id,
    method: 'delete'
  })
}

export function changeActivityStatus(id, status) {
  return request({
    url: '/core/activity/changeStatus',
    method: 'put',
    data: { id: id, status: status }
  })
}
