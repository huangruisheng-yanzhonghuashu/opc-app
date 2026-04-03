package com.opc.core.mapper;

import java.util.List;
import com.opc.core.domain.CoreActivity;

public interface CoreActivityMapper
{
    public List<CoreActivity> selectActivityList(CoreActivity activity);

    public CoreActivity selectActivityById(Long id);

    public int insertActivity(CoreActivity activity);

    public int updateActivity(CoreActivity activity);

    public int deleteActivityById(Long id);

    public int deleteActivityByIds(Long[] ids);

    public int changeStatus(CoreActivity activity);
}
