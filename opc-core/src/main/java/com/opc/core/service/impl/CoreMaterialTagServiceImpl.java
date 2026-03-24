package com.opc.core.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.opc.core.domain.CoreMaterialTag;
import com.opc.core.domain.CoreTag;
import com.opc.core.mapper.CoreMaterialTagMapper;
import com.opc.core.service.ICoreMaterialTagService;

@Service
public class CoreMaterialTagServiceImpl implements ICoreMaterialTagService
{
    @Autowired
    private CoreMaterialTagMapper materialTagMapper;

    @Override
    public List<CoreMaterialTag> selectMaterialTagList(CoreMaterialTag materialTag)
    {
        return materialTagMapper.selectMaterialTagList(materialTag);
    }

    @Override
    public List<CoreTag> selectTagsByMaterialId(Long materialId)
    {
        return materialTagMapper.selectTagsByMaterialId(materialId);
    }

    @Override
    public int insertMaterialTag(CoreMaterialTag materialTag)
    {
        return materialTagMapper.insertMaterialTag(materialTag);
    }

    @Override
    public int deleteMaterialTagById(Long id)
    {
        return materialTagMapper.deleteMaterialTagById(id);
    }

    @Override
    public int deleteMaterialTagByMaterialId(Long materialId)
    {
        return materialTagMapper.deleteMaterialTagByMaterialId(materialId);
    }

    @Override
    public int deleteMaterialTagByTagId(Long tagId)
    {
        return materialTagMapper.deleteMaterialTagByTagId(tagId);
    }

    @Override
    public int deleteMaterialTags(Long[] ids)
    {
        return materialTagMapper.deleteMaterialTags(ids);
    }

    @Override
    public int batchInsertMaterialTag(List<CoreMaterialTag> materialTagList)
    {
        return materialTagMapper.batchInsertMaterialTag(materialTagList);
    }

    @Override
    @Transactional
    public void updateMaterialTags(Long materialId, List<Long> tagIds, String createBy)
    {
        // 先删除该素材的所有标签关联
        materialTagMapper.deleteMaterialTagByMaterialId(materialId);

        // 如果有新标签，批量插入
        if (tagIds != null && !tagIds.isEmpty())
        {
            List<CoreMaterialTag> materialTagList = new ArrayList<>();
            for (Long tagId : tagIds)
            {
                CoreMaterialTag materialTag = new CoreMaterialTag();
                materialTag.setMaterialId(materialId);
                materialTag.setTagId(tagId);
                materialTag.setCreateBy(createBy);
                materialTagList.add(materialTag);
            }
            materialTagMapper.batchInsertMaterialTag(materialTagList);
        }
    }
}
