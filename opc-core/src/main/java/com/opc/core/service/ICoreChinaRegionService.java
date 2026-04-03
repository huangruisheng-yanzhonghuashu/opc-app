package com.opc.core.service;

import java.util.List;

import com.opc.core.domain.CoreChinaRegion;

/**
 * 中国省市区Service接口
 * 
 * @author opc
 */
public interface ICoreChinaRegionService 
{
    /**
     * 查询省市区列表
     * 
     * @param coreChinaRegion 省市区对象
     * @return 省市区集合
     */
    public List<CoreChinaRegion> selectCoreChinaRegionList(CoreChinaRegion coreChinaRegion);

    /**
     * 根据父级代码查询子区域列表
     * 
     * @param parentCode 父级行政区划代码
     * @return 子区域集合
     */
    public List<CoreChinaRegion> selectCoreChinaRegionByParentCode(String parentCode);

    /**
     * 根据层级查询区域列表
     * 
     * @param level 层级（1省/直辖市/自治区 2市 3区/县）
     * @return 区域集合
     */
    public List<CoreChinaRegion> selectCoreChinaRegionByLevel(Integer level);

    /**
     * 根据行政区划代码查询区域信息
     * 
     * @param code 行政区划代码
     * @return 区域信息
     */
    public CoreChinaRegion selectCoreChinaRegionByCode(String code);

    /**
     * 查询所有省份列表
     * 
     * @return 省份集合
     */
    public List<CoreChinaRegion> selectAllProvinces();

    /**
     * 根据省份代码查询城市列表
     * 
     * @param provinceCode 省份代码
     * @return 城市集合
     */
    public List<CoreChinaRegion> selectCitiesByProvinceCode(String provinceCode);

    /**
     * 根据城市代码查询区县列表
     * 
     * @param cityCode 城市代码
     * @return 区县集合
     */
    public List<CoreChinaRegion> selectDistrictsByCityCode(String cityCode);

    /**
     * 获取完整的省市区树形结构
     * 
     * @return 省市区树形结构
     */
    public List<CoreChinaRegion> selectRegionTree();

    /**
     * 根据省份代码获取省市级联数据（包含城市）
     * 
     * @param provinceCode 省份代码
     * @return 省市级联数据
     */
    public CoreChinaRegion selectProvinceWithCities(String provinceCode);
}
