package com.opc.core.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.opc.core.domain.CoreMaterial;
import com.opc.core.mapper.CoreMaterialMapper;
import com.opc.core.service.ICoreMaterialService;

@Service
public class CoreMaterialServiceImpl implements ICoreMaterialService
{
    @Autowired
    private CoreMaterialMapper materialMapper;

    @Override
    public List<CoreMaterial> selectMaterialList(CoreMaterial material)
    {
        return materialMapper.selectMaterialList(material);
    }

    @Override
    public CoreMaterial selectMaterialById(Long id)
    {
        return materialMapper.selectMaterialById(id);
    }

    @Override
    public int insertMaterial(CoreMaterial material)
    {
        return materialMapper.insertMaterial(material);
    }

    @Override
    public int updateMaterial(CoreMaterial material)
    {
        return materialMapper.updateMaterial(material);
    }

    @Override
    public int deleteMaterialById(Long id)
    {
        return materialMapper.deleteMaterialById(id);
    }

    @Override
    public int deleteMaterialByIds(Long[] ids)
    {
        return materialMapper.deleteMaterialByIds(ids);
    }

    @Override
    public int changeStatus(Long id, String status)
    {
        CoreMaterial material = new CoreMaterial();
        material.setId(id);
        material.setStatus(status);
        return materialMapper.changeStatus(material);
    }

    @Override
    public int changeTop(Long id, String isTop)
    {
        CoreMaterial material = new CoreMaterial();
        material.setId(id);
        material.setIsTop(isTop);
        return materialMapper.changeTop(material);
    }
}
