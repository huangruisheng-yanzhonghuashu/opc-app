import request from '@/utils/request'

export function listActivityBanner(query) {
  return request({
    url: '/core/activityBanner/list',
    method: 'get',
    params: query
  })
}

export function getActivityBanner(id) {
  return request({
    url: '/core/activityBanner/' + id,
    method: 'get'
  })
}

export function addActivityBanner(data) {
  return request({
    url: '/core/activityBanner',
    method: 'post',
    data: data
  })
}

export function updateActivityBanner(data) {
  return request({
    url: '/core/activityBanner',
    method: 'put',
    data: data
  })
}

export function delActivityBanner(id) {
  return request({
    url: '/core/activityBanner/' + id,
    method: 'delete'
  })
}

export function changeActivityBannerStatus(id, status) {
  return request({
    url: '/core/activityBanner/changeStatus',
    method: 'put',
    data: { id: id, status: status }
  })
}
