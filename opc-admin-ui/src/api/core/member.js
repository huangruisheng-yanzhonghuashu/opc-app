import request from '@/utils/request'

export function listMember(query) {
  return request({
    url: '/core/member/list',
    method: 'get',
    params: query
  })
}

export function getMember(id) {
  return request({
    url: '/core/member/' + id,
    method: 'get'
  })
}

export function addMember(data) {
  return request({
    url: '/core/member',
    method: 'post',
    data: data
  })
}

export function updateMember(data) {
  return request({
    url: '/core/member',
    method: 'put',
    data: data
  })
}

export function blockMember(id) {
  return request({
    url: '/core/member/block/' + id,
    method: 'put'
  })
}

export function unblockMember(id) {
  return request({
    url: '/core/member/unblock/' + id,
    method: 'put'
  })
}
