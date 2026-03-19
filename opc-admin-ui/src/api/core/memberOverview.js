import request from '@/utils/request'

export function getMemberOverview(query) {
  return request({
    url: '/core/member/overview',
    method: 'get',
    params: query
  })
}
