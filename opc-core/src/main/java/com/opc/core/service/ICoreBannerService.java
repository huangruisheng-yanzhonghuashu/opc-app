package com.opc.core.service;

import java.util.List;
import com.opc.core.domain.CoreBanner;

public interface ICoreBannerService
{
    public List<CoreBanner> selectBannerList(CoreBanner banner);

    public CoreBanner selectBannerById(Long id);

    public int insertBanner(CoreBanner banner);

    public int updateBanner(CoreBanner banner);

    public int deleteBannerById(Long id);

    public int deleteBannerByIds(Long[] ids);

    public int changeStatus(Long id, String status);
}
