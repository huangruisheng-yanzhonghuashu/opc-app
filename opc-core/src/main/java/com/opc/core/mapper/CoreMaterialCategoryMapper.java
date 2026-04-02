package com.opc.core.mapper;

import java.util.List;
import com.opc.core.domain.CoreMaterialCategory;

/**
 * 素材二级分类Mapper接口
 *
 * @author opc
 */
public interface CoreMaterialCategoryMapper
{
    /**
     * 查询素材二级分类列表
     *
     * @param coreMaterialCategory 素材二级分类
     * @return 素材二级分类集合
     */
    public List<CoreMaterialCategory> selectCoreMaterialCategoryList(CoreMaterialCategory coreMaterialCategory);

    /**
     * 根据套餐分类查询素材二级分类列表
     *
     * @param packageType 套餐分类
     * @return 素材二级分类集合
     */
    public List<CoreMaterialCategory> selectCoreMaterialCategoryByPackageType(Integer packageType);

    /**
     * 查询所有启用的素材二级分类
     *
     * @return 素材二级分类集合
     */
    public List<CoreMaterialCategory> selectAllActiveCategory();

    /**
     * 查询素材二级分类
     *
     * @param id 素材二级分类ID
     * @return 素材二级分类
     */
    public CoreMaterialCategory selectCoreMaterialCategoryById(Long id);

    /**
     * 新增素材二级分类
     *
     * @param coreMaterialCategory 素材二级分类
     * @return 结果
     */
    public int insertCoreMaterialCategory(CoreMaterialCategory coreMaterialCategory);

    /**
     * 修改素材二级分类
     *
     * @param coreMaterialCategory 素材二级分类
     * @return 结果
     */
    public int updateCoreMaterialCategory(CoreMaterialCategory coreMaterialCategory);

    /**
     * 删除素材二级分类
     *
     * @param id 素材二级分类ID
     * @return 结果
     */
    public int deleteCoreMaterialCategoryById(Long id);

    /**
     * 批量删除素材二级分类
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteCoreMaterialCategoryByIds(Long[] ids);
}
