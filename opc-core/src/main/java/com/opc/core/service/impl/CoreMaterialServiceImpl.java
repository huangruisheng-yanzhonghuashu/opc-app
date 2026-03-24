package com.opc.core.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.opc.core.domain.CoreMaterial;
import com.opc.core.domain.CoreTag;
import com.opc.core.mapper.CoreMaterialMapper;
import com.opc.core.mapper.CoreMaterialTagMapper;
import com.opc.core.service.ICoreMaterialService;

@Service
public class CoreMaterialServiceImpl implements ICoreMaterialService
{
    @Autowired
    private CoreMaterialMapper materialMapper;

    @Autowired
    private CoreMaterialTagMapper materialTagMapper;

    @Override
    public List<CoreMaterial> selectMaterialList(CoreMaterial material)
    {
        return materialMapper.selectMaterialList(material);
    }

    @Override
    public CoreMaterial selectMaterialById(Long id)
    {
        CoreMaterial material = materialMapper.selectMaterialById(id);
        if (material != null)
        {
            List<CoreTag> tags = materialTagMapper.selectTagsByMaterialId(id);
            material.setTags(tags);
        }
        return material;
    }

    @Override
    @Transactional
    public int insertMaterial(CoreMaterial material)
    {
        int result = materialMapper.insertMaterial(material);
        if (result > 0 && material.getTagIds() != null && !material.getTagIds().isEmpty())
        {
            materialTagMapper.deleteMaterialTagByMaterialId(material.getId());
            for (Long tagId : material.getTagIds())
            {
                materialMapper.insertMaterialTag(material.getId(), tagId);
            }
        }
        return result;
    }

    @Override
    @Transactional
    public int updateMaterial(CoreMaterial material)
    {
        int result = materialMapper.updateMaterial(material);
        if (result > 0 && material.getTagIds() != null)
        {
            materialTagMapper.deleteMaterialTagByMaterialId(material.getId());
            for (Long tagId : material.getTagIds())
            {
                materialMapper.insertMaterialTag(material.getId(), tagId);
            }
        }
        return result;
    }

    @Override
    @Transactional
    public int deleteMaterialById(Long id)
    {
        materialTagMapper.deleteMaterialTagByMaterialId(id);
        return materialMapper.deleteMaterialById(id);
    }

    @Override
    @Transactional
    public int deleteMaterialByIds(Long[] ids)
    {
        for (Long id : ids)
        {
            materialTagMapper.deleteMaterialTagByMaterialId(id);
        }
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

    @Override
    public List<CoreMaterial> selectMaterialListByTagId(Long tagId, String status, Integer maxPackageType)
    {
        return materialMapper.selectMaterialListByTagId(tagId, status, maxPackageType);
    }
}
