package com.opc.core.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.opc.core.domain.CoreActivityBanner;
import com.opc.core.mapper.CoreActivityBannerMapper;
import com.opc.core.service.ICoreActivityBannerService;

@Service
public class CoreActivityBannerServiceImpl implements ICoreActivityBannerService
{
    @Autowired
    private CoreActivityBannerMapper bannerMapper;

    @Override
    public List<CoreActivityBanner> selectActivityBannerList(CoreActivityBanner banner)
    {
        return bannerMapper.selectActivityBannerList(banner);
    }

    @Override
    public CoreActivityBanner selectActivityBannerById(Long id)
    {
        return bannerMapper.selectActivityBannerById(id);
    }

    @Override
    public int insertActivityBanner(CoreActivityBanner banner)
    {
        return bannerMapper.insertActivityBanner(banner);
    }

    @Override
    public int updateActivityBanner(CoreActivityBanner banner)
    {
        return bannerMapper.updateActivityBanner(banner);
    }

    @Override
    public int deleteActivityBannerById(Long id)
    {
        return bannerMapper.deleteActivityBannerById(id);
    }

    @Override
    public int deleteActivityBannerByIds(Long[] ids)
    {
        return bannerMapper.deleteActivityBannerByIds(ids);
    }

    @Override
    public int changeStatus(Long id, String status)
    {
        CoreActivityBanner banner = new CoreActivityBanner();
        banner.setId(id);
        banner.setStatus(status);
        return bannerMapper.changeStatus(banner);
    }
}
