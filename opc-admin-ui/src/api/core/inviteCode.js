import request from '@/utils/request'

// 查询邀请码列表
export function listInviteCode(query) {
  return request({
    url: '/core/inviteCode/list',
    method: 'get',
    params: query
  })
}

// 查询邀请码详细
export function getInviteCode(id) {
  return request({
    url: '/core/inviteCode/' + id,
    method: 'get'
  })
}

// 新增邀请码
export function addInviteCode(data) {
  return request({
    url: '/core/inviteCode',
    method: 'post',
    data: data
  })
}

// 修改邀请码
export function updateInviteCode(data) {
  return request({
    url: '/core/inviteCode',
    method: 'put',
    data: data
  })
}

// 删除邀请码
export function delInviteCode(id) {
  return request({
    url: '/core/inviteCode/' + id,
    method: 'delete'
  })
}

// 批量生成邀请码
export function batchGenerateInviteCode(data) {
  return request({
    url: '/core/inviteCode/batchGenerate',
    method: 'post',
    data: data
  })
}

// 下发邀请码
export function distributeInviteCode(id) {
  return request({
    url: '/core/inviteCode/distribute/' + id,
    method: 'put'
  })
}

// 批量下发邀请码
export function batchDistributeInviteCode(ids) {
  return request({
    url: '/core/inviteCode/batchDistribute',
    method: 'put',
    data: { ids: ids }
  })
}

// 导出邀请码
export function exportInviteCode(query) {
  return request({
    url: '/core/inviteCode/export',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}
