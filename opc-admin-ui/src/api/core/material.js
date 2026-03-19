import request from '@/utils/request'

export function listMaterial(query) {
  return request({
    url: '/core/material/list',
    method: 'get',
    params: query
  })
}

export function getMaterial(id) {
  return request({
    url: '/core/material/' + id,
    method: 'get'
  })
}

export function addMaterial(data) {
  return request({
    url: '/core/material',
    method: 'post',
    data: data
  })
}

export function updateMaterial(data) {
  return request({
    url: '/core/material',
    method: 'put',
    data: data
  })
}

export function delMaterial(id) {
  return request({
    url: '/core/material/' + id,
    method: 'delete'
  })
}

export function changeMaterialStatus(id, status) {
  return request({
    url: '/core/material/changeStatus',
    method: 'put',
    data: { id: id, status: status }
  })
}

export function changeMaterialTop(id, isTop) {
  return request({
    url: '/core/material/changeTop',
    method: 'put',
    data: { id: id, isTop: isTop }
  })
}
