import request from '@/utils/request'

export function listMemberConfig(query) {
  return request({
    url: '/core/memberConfig/list',
    method: 'get',
    params: query
  })
}

export function getMemberConfig(id) {
  return request({
    url: '/core/memberConfig/' + id,
    method: 'get'
  })
}

export function getMemberConfigByType(type) {
  return request({
    url: '/core/memberConfig/type/' + type,
    method: 'get'
  })
}

export function saveMemberConfig(data) {
  return request({
    url: '/core/memberConfig',
    method: 'post',
    data: data
  })
}
