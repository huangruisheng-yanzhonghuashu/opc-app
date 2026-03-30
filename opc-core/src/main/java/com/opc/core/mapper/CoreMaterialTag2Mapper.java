package com.opc.core.mapper;

import java.util.List;
import com.opc.core.domain.CoreMaterialTag2;
import com.opc.core.domain.CoreTag2;

public interface CoreMaterialTag2Mapper
{
    public List<CoreMaterialTag2> selectMaterialTag2List(CoreMaterialTag2 materialTag);

    public List<CoreTag2> selectTags2ByMaterialId(Long materialId);

    public int insertMaterialTag2(CoreMaterialTag2 materialTag);

    public int deleteMaterialTag2ById(Long id);

    public int deleteMaterialTag2ByMaterialId(Long materialId);

    public int deleteMaterialTag2ByTagId(Long tagId);

    public int deleteMaterialTag2s(Long[] ids);

    public int batchInsertMaterialTag2(List<CoreMaterialTag2> materialTagList);

    public boolean checkMaterialTag2Exists(Long materialId, Long tagId);
}
