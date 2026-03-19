import request from '@/utils/request'

export function listPackageOrder(query) {
  return request({
    url: '/core/packageOrder/list',
    method: 'get',
    params: query
  })
}

export function getPackageOrder(id) {
  return request({
    url: '/core/packageOrder/' + id,
    method: 'get'
  })
}

export function addPackageOrder(data) {
  return request({
    url: '/core/packageOrder',
    method: 'post',
    data: data
  })
}

export function updatePackageOrder(data) {
  return request({
    url: '/core/packageOrder',
    method: 'put',
    data: data
  })
}

export function delPackageOrder(id) {
  return request({
    url: '/core/packageOrder/' + id,
    method: 'delete'
  })
}

export function batchRemovePackageOrder(ids) {
  return request({
    url: '/core/packageOrder/batch/' + ids,
    method: 'delete'
  })
}
