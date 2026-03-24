import request from '@/utils/request'

export function listSearchHotword(query) {
  return request({
    url: '/core/searchHotword/list',
    method: 'get',
    params: query
  })
}

export function getSearchHotword(id) {
  return request({
    url: '/core/searchHotword/' + id,
    method: 'get'
  })
}

export function addSearchHotword(data) {
  return request({
    url: '/core/searchHotword',
    method: 'post',
    data: data
  })
}

export function updateSearchHotword(data) {
  return request({
    url: '/core/searchHotword',
    method: 'put',
    data: data
  })
}

export function delSearchHotword(id) {
  return request({
    url: '/core/searchHotword/' + id,
    method: 'delete'
  })
}

export function changeSearchHotwordStatus(id, status) {
  return request({
    url: '/core/searchHotword/changeStatus',
    method: 'put',
    data: { id: id, status: status }
  })
}
