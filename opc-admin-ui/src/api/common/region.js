import request from '@/utils/request'

// 获取所有省份列表
export function getProvinces() {
  return request({
    url: '/common/region/provinces',
    method: 'get'
  })
}

// 根据省份代码获取城市列表
export function getCitiesByProvince(provinceCode) {
  return request({
    url: '/common/region/cities/' + provinceCode,
    method: 'get'
  })
}

// 根据城市代码获取区县列表
export function getDistrictsByCity(cityCode) {
  return request({
    url: '/common/region/districts/' + cityCode,
    method: 'get'
  })
}

// 根据父级代码获取子区域列表
export function getChildrenByParentCode(parentCode) {
  return request({
    url: '/common/region/children/' + parentCode,
    method: 'get'
  })
}

// 根据行政区划代码获取区域详情
export function getRegionInfo(code) {
  return request({
    url: '/common/region/info/' + code,
    method: 'get'
  })
}

// 获取完整的省市区树形结构
export function getRegionTree() {
  return request({
    url: '/common/region/tree',
    method: 'get'
  })
}

// 根据层级获取区域列表
export function getRegionsByLevel(level) {
  return request({
    url: '/common/region/level/' + level,
    method: 'get'
  })
}

// 根据省份代码获取省市级联数据（包含城市列表）
export function getProvinceWithCities(provinceCode) {
  return request({
    url: '/common/region/provinceWithCities/' + provinceCode,
    method: 'get'
  })
}
