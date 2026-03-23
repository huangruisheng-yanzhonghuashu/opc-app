import request from '@/utils/request'

export function listFeedback(query) {
  return request({
    url: '/core/feedback/list',
    method: 'get',
    params: query
  })
}

export function getFeedback(id) {
  return request({
    url: '/core/feedback/' + id,
    method: 'get'
  })
}

export function addFeedback(data) {
  return request({
    url: '/core/feedback',
    method: 'post',
    data: data
  })
}

export function updateFeedback(data) {
  return request({
    url: '/core/feedback',
    method: 'put',
    data: data
  })
}

export function replyFeedback(data) {
  return request({
    url: '/core/feedback/reply',
    method: 'put',
    data: data
  })
}

export function delFeedback(id) {
  return request({
    url: '/core/feedback/' + id,
    method: 'delete'
  })
}

export function batchDelFeedback(ids) {
  return request({
    url: '/core/feedback/batch/' + ids,
    method: 'delete'
  })
}
