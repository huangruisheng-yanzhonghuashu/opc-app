package com.opc.core.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.opc.core.domain.CoreTag;
import com.opc.core.mapper.CoreTagMapper;
import com.opc.core.service.ICoreTagService;

@Service
public class CoreTagServiceImpl implements ICoreTagService
{
    @Autowired
    private CoreTagMapper tagMapper;

    @Override
    public List<CoreTag> selectTagList(CoreTag tag)
    {
        return tagMapper.selectTagList(tag);
    }

    @Override
    public CoreTag selectTagById(Long id)
    {
        return tagMapper.selectTagById(id);
    }

    @Override
    public CoreTag selectTagByName(String tagName)
    {
        return tagMapper.selectTagByName(tagName);
    }

    @Override
    public int insertTag(CoreTag tag)
    {
        return tagMapper.insertTag(tag);
    }

    @Override
    public int updateTag(CoreTag tag)
    {
        return tagMapper.updateTag(tag);
    }

    @Override
    public int deleteTagById(Long id)
    {
        return tagMapper.deleteTagById(id);
    }

    @Override
    public int deleteTagByIds(Long[] ids)
    {
        return tagMapper.deleteTagByIds(ids);
    }

    @Override
    public List<CoreTag> selectAllActiveTags()
    {
        return tagMapper.selectAllActiveTags();
    }
}
