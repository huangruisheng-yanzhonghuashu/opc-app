package com.opc.core.service;

import java.util.List;
import com.opc.core.domain.CoreMaterialTag;
import com.opc.core.domain.CoreTag;

public interface ICoreMaterialTagService
{
    public List<CoreMaterialTag> selectMaterialTagList(CoreMaterialTag materialTag);

    public List<CoreTag> selectTagsByMaterialId(Long materialId);

    public int insertMaterialTag(CoreMaterialTag materialTag);

    public int deleteMaterialTagById(Long id);

    public int deleteMaterialTagByMaterialId(Long materialId);

    public int deleteMaterialTagByTagId(Long tagId);

    public int deleteMaterialTags(Long[] ids);

    public int batchInsertMaterialTag(List<CoreMaterialTag> materialTagList);

    public void updateMaterialTags(Long materialId, List<Long> tagIds, String createBy);
}
