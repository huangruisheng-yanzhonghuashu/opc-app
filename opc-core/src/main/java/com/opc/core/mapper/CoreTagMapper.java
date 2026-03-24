package com.opc.core.mapper;

import java.util.List;
import com.opc.core.domain.CoreTag;

public interface CoreTagMapper
{
    public List<CoreTag> selectTagList(CoreTag tag);

    public CoreTag selectTagById(Long id);

    public CoreTag selectTagByName(String tagName);

    public int insertTag(CoreTag tag);

    public int updateTag(CoreTag tag);

    public int deleteTagById(Long id);

    public int deleteTagByIds(Long[] ids);

    public List<CoreTag> selectAllActiveTags();
}
