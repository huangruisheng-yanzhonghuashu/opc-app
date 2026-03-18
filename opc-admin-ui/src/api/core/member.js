import request from '@/utils/request'

export function listMember(query) {
  return request({
    url: '/core/member/list',
    method: 'get',
    params: query
  })
}

export function getMember(memberId) {
  return request({
    url: '/core/member/' + memberId,
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

export function delMember(memberId) {
  return request({
    url: '/core/member/' + memberId,
    method: 'delete'
  })
}
