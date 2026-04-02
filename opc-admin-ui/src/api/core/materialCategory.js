import request from '@/utils/request'

// 查询素材二级分类列表
export function listMaterialCategory(query) {
  return request({
    url: '/core/materialCategory/list',
    method: 'get',
    params: query
  })
}

// 根据套餐分类查询素材二级分类列表
export function listCategoryByPackageType(packageType) {
  return request({
    url: '/core/materialCategory/listByPackageType/' + packageType,
    method: 'get'
  })
}

// 查询所有启用的素材二级分类
export function listAllActiveCategory() {
  return request({
    url: '/core/materialCategory/listAllActive',
    method: 'get'
  })
}

// 查询素材二级分类详细
export function getMaterialCategory(id) {
  return request({
    url: '/core/materialCategory/' + id,
    method: 'get'
  })
}

// 新增素材二级分类
export function addMaterialCategory(data) {
  return request({
    url: '/core/materialCategory',
    method: 'post',
    data: data
  })
}

// 修改素材二级分类
export function updateMaterialCategory(data) {
  return request({
    url: '/core/materialCategory',
    method: 'put',
    data: data
  })
}

// 删除素材二级分类
export function delMaterialCategory(ids) {
  return request({
    url: '/core/materialCategory/' + ids.join(','),
    method: 'delete'
  })
}
