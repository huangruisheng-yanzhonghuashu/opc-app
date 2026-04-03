package com.opc.core.service;

import java.util.List;
import com.opc.core.domain.CoreActivity;

public interface ICoreActivityService
{
    public List<CoreActivity> selectActivityList(CoreActivity activity);

    public CoreActivity selectActivityById(Long id);

    public int insertActivity(CoreActivity activity);

    public int updateActivity(CoreActivity activity);

    public int deleteActivityById(Long id);

    public int deleteActivityByIds(Long[] ids);

    public int changeStatus(Long id, String status);
}
