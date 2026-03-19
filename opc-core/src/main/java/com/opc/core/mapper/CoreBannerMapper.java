package com.opc.core.mapper;

import java.util.List;
import com.opc.core.domain.CoreBanner;

public interface CoreBannerMapper
{
    public List<CoreBanner> selectBannerList(CoreBanner banner);

    public CoreBanner selectBannerById(Long id);

    public int insertBanner(CoreBanner banner);

    public int updateBanner(CoreBanner banner);

    public int deleteBannerById(Long id);

    public int deleteBannerByIds(Long[] ids);

    public int changeStatus(CoreBanner banner);
}
