package com.opc.core.service;

import java.util.List;
import com.opc.core.domain.CoreMaterial;

public interface ICoreMaterialService
{
    public List<CoreMaterial> selectMaterialList(CoreMaterial material);

    public CoreMaterial selectMaterialById(Long id);

    public int insertMaterial(CoreMaterial material);

    public int updateMaterial(CoreMaterial material);

    public int deleteMaterialById(Long id);

    public int deleteMaterialByIds(Long[] ids);

    public int changeStatus(Long id, String status);

    public int changeTop(Long id, String isTop);
}
