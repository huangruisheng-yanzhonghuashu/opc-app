package com.opc.core.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.opc.core.domain.CoreMaterial;

public interface CoreMaterialMapper
{
    public List<CoreMaterial> selectMaterialList(CoreMaterial material);

    public CoreMaterial selectMaterialById(Long id);

    public int insertMaterial(CoreMaterial material);

    public int updateMaterial(CoreMaterial material);

    public int deleteMaterialById(Long id);

    public int deleteMaterialByIds(Long[] ids);

    public int changeStatus(CoreMaterial material);

    public int changeTop(CoreMaterial material);

    public int insertMaterialTag(@Param("materialId") Long materialId, @Param("tagId") Long tagId);

    /**
     * 根据标签ID查询素材列表
     *
     * @param tagId 标签ID
     * @param status 状态
     * @param maxPackageType 最大套餐类型（会员只能看到packageType小于等于此值的素材）
     * @return 素材列表
     */
    public List<CoreMaterial> selectMaterialListByTagId(@Param("tagId") Long tagId, @Param("status") String status, @Param("maxPackageType") Integer maxPackageType);

    /**
     * 原子增加查看数
     *
     * @param id 素材ID
     * @return 影响行数
     */
    int incrementViewCount(@Param("id") Long id);
}
