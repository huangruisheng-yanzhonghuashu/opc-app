package com.opc.core.service;

import java.util.List;
import com.opc.core.domain.CoreSearchHotword;

public interface ICoreSearchHotwordService
{
    public List<CoreSearchHotword> selectSearchHotwordList(CoreSearchHotword searchHotword);

    public CoreSearchHotword selectSearchHotwordById(Long id);

    public int insertSearchHotword(CoreSearchHotword searchHotword);

    public int updateSearchHotword(CoreSearchHotword searchHotword);

    public int deleteSearchHotwordById(Long id);

    public int deleteSearchHotwordByIds(Long[] ids);

    public int changeStatus(Long id, String status);
}
