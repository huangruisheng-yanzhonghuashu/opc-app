package com.opc.core.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.opc.core.domain.CoreBanner;
import com.opc.core.mapper.CoreBannerMapper;
import com.opc.core.service.ICoreBannerService;

@Service
public class CoreBannerServiceImpl implements ICoreBannerService
{
    @Autowired
    private CoreBannerMapper bannerMapper;

    @Override
    public List<CoreBanner> selectBannerList(CoreBanner banner)
    {
        return bannerMapper.selectBannerList(banner);
    }

    @Override
    public CoreBanner selectBannerById(Long id)
    {
        return bannerMapper.selectBannerById(id);
    }

    @Override
    public int insertBanner(CoreBanner banner)
    {
        return bannerMapper.insertBanner(banner);
    }

    @Override
    public int updateBanner(CoreBanner banner)
    {
        return bannerMapper.updateBanner(banner);
    }

    @Override
    public int deleteBannerById(Long id)
    {
        return bannerMapper.deleteBannerById(id);
    }

    @Override
    public int deleteBannerByIds(Long[] ids)
    {
        return bannerMapper.deleteBannerByIds(ids);
    }

    @Override
    public int changeStatus(Long id, String status)
    {
        CoreBanner banner = new CoreBanner();
        banner.setId(id);
        banner.setStatus(status);
        return bannerMapper.changeStatus(banner);
    }
}
