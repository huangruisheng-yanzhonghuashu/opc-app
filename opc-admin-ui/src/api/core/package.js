import request from '@/utils/request'

export function listPackage(query) {
  return request({
    url: '/core/package/list',
    method: 'get',
    params: query
  })
}

export function getPackage(id) {
  return request({
    url: '/core/package/' + id,
    method: 'get'
  })
}

export function addPackage(data) {
  return request({
    url: '/core/package',
    method: 'post',
    data: data
  })
}

export function updatePackage(data) {
  return request({
    url: '/core/package',
    method: 'put',
    data: data
  })
}

export function delPackage(id) {
  return request({
    url: '/core/package/' + id,
    method: 'delete'
  })
}

export function batchRemovePackage(ids) {
  return request({
    url: '/core/package/batch/' + ids,
    method: 'delete'
  })
}
