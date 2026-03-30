package com.opc.core.mapper;

import java.util.List;
import com.opc.core.domain.CoreTag2;

public interface CoreTag2Mapper
{
    public List<CoreTag2> selectTag2List(CoreTag2 tag);

    public CoreTag2 selectTag2ById(Long id);

    public CoreTag2 selectTag2ByName(String tagName);

    public int insertTag2(CoreTag2 tag);

    public int updateTag2(CoreTag2 tag);

    public int deleteTag2ById(Long id);

    public int deleteTag2ByIds(Long[] ids);

    public List<CoreTag2> selectAllActiveTags();
}
