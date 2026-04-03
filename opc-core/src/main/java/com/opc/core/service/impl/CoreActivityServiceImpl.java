package com.opc.core.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.opc.core.domain.CoreActivity;
import com.opc.core.mapper.CoreActivityMapper;
import com.opc.core.service.ICoreActivityService;

@Service
public class CoreActivityServiceImpl implements ICoreActivityService
{
    @Autowired
    private CoreActivityMapper activityMapper;

    @Override
    public List<CoreActivity> selectActivityList(CoreActivity activity)
    {
        return activityMapper.selectActivityList(activity);
    }

    @Override
    public CoreActivity selectActivityById(Long id)
    {
        return activityMapper.selectActivityById(id);
    }

    @Override
    public int insertActivity(CoreActivity activity)
    {
        return activityMapper.insertActivity(activity);
    }

    @Override
    public int updateActivity(CoreActivity activity)
    {
        return activityMapper.updateActivity(activity);
    }

    @Override
    public int deleteActivityById(Long id)
    {
        return activityMapper.deleteActivityById(id);
    }

    @Override
    public int deleteActivityByIds(Long[] ids)
    {
        return activityMapper.deleteActivityByIds(ids);
    }

    @Override
    public int changeStatus(Long id, String status)
    {
        CoreActivity activity = new CoreActivity();
        activity.setId(id);
        activity.setStatus(status);
        return activityMapper.changeStatus(activity);
    }
}
