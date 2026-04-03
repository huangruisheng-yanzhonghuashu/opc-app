package com.opc.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.opc.core.domain.CoreChinaRegion;
import com.opc.core.mapper.CoreChinaRegionMapper;
import com.opc.core.service.ICoreChinaRegionService;

/**
 * 中国省市区Service业务层处理
 * 
 * @author opc
 */
@Service
public class CoreChinaRegionServiceImpl implements ICoreChinaRegionService
{
    @Autowired
    private CoreChinaRegionMapper coreChinaRegionMapper;

    /**
     * 查询省市区列表
     */
    @Override
    public List<CoreChinaRegion> selectCoreChinaRegionList(CoreChinaRegion coreChinaRegion)
    {
        return coreChinaRegionMapper.selectCoreChinaRegionList(coreChinaRegion);
    }

    /**
     * 根据父级代码查询子区域列表
     */
    @Override
    public List<CoreChinaRegion> selectCoreChinaRegionByParentCode(String parentCode)
    {
        return coreChinaRegionMapper.selectCoreChinaRegionByParentCode(parentCode);
    }

    /**
     * 根据层级查询区域列表
     */
    @Override
    public List<CoreChinaRegion> selectCoreChinaRegionByLevel(Integer level)
    {
        return coreChinaRegionMapper.selectCoreChinaRegionByLevel(level);
    }

    /**
     * 根据行政区划代码查询区域信息
     */
    @Override
    public CoreChinaRegion selectCoreChinaRegionByCode(String code)
    {
        return coreChinaRegionMapper.selectCoreChinaRegionByCode(code);
    }

    /**
     * 查询所有省份列表
     */
    @Override
    public List<CoreChinaRegion> selectAllProvinces()
    {
        return coreChinaRegionMapper.selectAllProvinces();
    }

    /**
     * 根据省份代码查询城市列表
     */
    @Override
    public List<CoreChinaRegion> selectCitiesByProvinceCode(String provinceCode)
    {
        return coreChinaRegionMapper.selectCitiesByProvinceCode(provinceCode);
    }

    /**
     * 根据城市代码查询区县列表
     */
    @Override
    public List<CoreChinaRegion> selectDistrictsByCityCode(String cityCode)
    {
        return coreChinaRegionMapper.selectDistrictsByCityCode(cityCode);
    }

    /**
     * 获取完整的省市区树形结构
     */
    @Override
    public List<CoreChinaRegion> selectRegionTree()
    {
        // 查询所有省份
        List<CoreChinaRegion> provinces = selectAllProvinces();
        
        // 为每个省份查询城市
        for (CoreChinaRegion province : provinces)
        {
            List<CoreChinaRegion> cities = selectCitiesByProvinceCode(province.getCode());
            
            // 为每个城市查询区县
            for (CoreChinaRegion city : cities)
            {
                List<CoreChinaRegion> districts = selectDistrictsByCityCode(city.getCode());
                city.setChildren(districts);
            }
            
            province.setChildren(cities);
        }
        
        return provinces;
    }

    /**
     * 根据省份代码获取省市级联数据（包含城市）
     */
    @Override
    public CoreChinaRegion selectProvinceWithCities(String provinceCode)
    {
        CoreChinaRegion province = selectCoreChinaRegionByCode(provinceCode);
        if (province != null)
        {
            List<CoreChinaRegion> cities = selectCitiesByProvinceCode(provinceCode);
            province.setChildren(cities);
        }
        return province;
    }
}
