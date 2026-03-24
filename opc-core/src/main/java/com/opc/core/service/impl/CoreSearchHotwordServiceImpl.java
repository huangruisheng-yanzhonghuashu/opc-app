package com.opc.core.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.opc.core.domain.CoreSearchHotword;
import com.opc.core.mapper.CoreSearchHotwordMapper;
import com.opc.core.service.ICoreSearchHotwordService;

@Service
public class CoreSearchHotwordServiceImpl implements ICoreSearchHotwordService
{
    @Autowired
    private CoreSearchHotwordMapper searchHotwordMapper;

    @Override
    public List<CoreSearchHotword> selectSearchHotwordList(CoreSearchHotword searchHotword)
    {
        return searchHotwordMapper.selectSearchHotwordList(searchHotword);
    }

    @Override
    public CoreSearchHotword selectSearchHotwordById(Long id)
    {
        return searchHotwordMapper.selectSearchHotwordById(id);
    }

    @Override
    public int insertSearchHotword(CoreSearchHotword searchHotword)
    {
        return searchHotwordMapper.insertSearchHotword(searchHotword);
    }

    @Override
    public int updateSearchHotword(CoreSearchHotword searchHotword)
    {
        return searchHotwordMapper.updateSearchHotword(searchHotword);
    }

    @Override
    public int deleteSearchHotwordById(Long id)
    {
        return searchHotwordMapper.deleteSearchHotwordById(id);
    }

    @Override
    public int deleteSearchHotwordByIds(Long[] ids)
    {
        return searchHotwordMapper.deleteSearchHotwordByIds(ids);
    }

    @Override
    public int changeStatus(Long id, String status)
    {
        CoreSearchHotword searchHotword = new CoreSearchHotword();
        searchHotword.setId(id);
        searchHotword.setStatus(status);
        return searchHotwordMapper.changeStatus(searchHotword);
    }
}
