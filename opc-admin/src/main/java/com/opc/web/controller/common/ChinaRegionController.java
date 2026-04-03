package com.opc.web.controller.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.opc.common.core.controller.BaseController;
import com.opc.common.core.domain.AjaxResult;
import com.opc.core.domain.CoreChinaRegion;
import com.opc.core.service.ICoreChinaRegionService;

/**
 * 中国省市区信息Controller
 * 
 * @author opc
 */
@RestController
@RequestMapping("/common/region")
public class ChinaRegionController extends BaseController
{
    @Autowired
    private ICoreChinaRegionService regionService;

    /**
     * 获取所有省份列表
     * 
     * @return 省份列表
     */
    @GetMapping("/provinces")
    public AjaxResult getProvinces()
    {
        List<CoreChinaRegion> provinces = regionService.selectAllProvinces();
        return success(provinces);
    }

    /**
     * 根据省份代码获取城市列表
     * 
     * @param provinceCode 省份行政区划代码
     * @return 城市列表
     */
    @GetMapping("/cities/{provinceCode}")
    public AjaxResult getCitiesByProvince(@PathVariable String provinceCode)
    {
        List<CoreChinaRegion> cities = regionService.selectCitiesByProvinceCode(provinceCode);
        return success(cities);
    }

    /**
     * 根据城市代码获取区县列表
     * 
     * @param cityCode 城市行政区划代码
     * @return 区县列表
     */
    @GetMapping("/districts/{cityCode}")
    public AjaxResult getDistrictsByCity(@PathVariable String cityCode)
    {
        List<CoreChinaRegion> districts = regionService.selectDistrictsByCityCode(cityCode);
        return success(districts);
    }

    /**
     * 根据父级代码获取子区域列表
     * 
     * @param parentCode 父级行政区划代码
     * @return 子区域列表
     */
    @GetMapping("/children/{parentCode}")
    public AjaxResult getChildrenByParentCode(@PathVariable String parentCode)
    {
        List<CoreChinaRegion> children = regionService.selectCoreChinaRegionByParentCode(parentCode);
        return success(children);
    }

    /**
     * 根据行政区划代码获取区域详情
     * 
     * @param code 行政区划代码
     * @return 区域详情
     */
    @GetMapping("/info/{code}")
    public AjaxResult getRegionInfo(@PathVariable String code)
    {
        CoreChinaRegion region = regionService.selectCoreChinaRegionByCode(code);
        return success(region);
    }

    /**
     * 获取完整的省市区树形结构
     * 
     * @return 省市区树形结构
     */
    @GetMapping("/tree")
    public AjaxResult getRegionTree()
    {
        List<CoreChinaRegion> tree = regionService.selectRegionTree();
        return success(tree);
    }

    /**
     * 根据层级获取区域列表
     * 
     * @param level 层级（1省/直辖市/自治区 2市 3区/县）
     * @return 区域列表
     */
    @GetMapping("/level/{level}")
    public AjaxResult getRegionsByLevel(@PathVariable Integer level)
    {
        List<CoreChinaRegion> regions = regionService.selectCoreChinaRegionByLevel(level);
        return success(regions);
    }

    /**
     * 根据省份代码获取省市级联数据（包含城市列表）
     * 
     * @param provinceCode 省份行政区划代码
     * @return 省市级联数据
     */
    @GetMapping("/provinceWithCities/{provinceCode}")
    public AjaxResult getProvinceWithCities(@PathVariable String provinceCode)
    {
        CoreChinaRegion province = regionService.selectProvinceWithCities(provinceCode);
        return success(province);
    }
}
