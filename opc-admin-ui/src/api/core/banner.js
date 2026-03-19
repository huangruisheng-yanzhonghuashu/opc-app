import request from '@/utils/request'

export function listBanner(query) {
  return request({
    url: '/core/banner/list',
    method: 'get',
    params: query
  })
}

export function getBanner(id) {
  return request({
    url: '/core/banner/' + id,
    method: 'get'
  })
}

export function addBanner(data) {
  return request({
    url: '/core/banner',
    method: 'post',
    data: data
  })
}

export function updateBanner(data) {
  return request({
    url: '/core/banner',
    method: 'put',
    data: data
  })
}

export function delBanner(id) {
  return request({
    url: '/core/banner/' + id,
    method: 'delete'
  })
}

export function changeBannerStatus(id, status) {
  return request({
    url: '/core/banner/changeStatus',
    method: 'put',
    data: { id: id, status: status }
  })
}
