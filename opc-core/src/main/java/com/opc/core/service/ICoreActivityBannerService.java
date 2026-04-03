package com.opc.core.service;

import java.util.List;
import com.opc.core.domain.CoreActivityBanner;

public interface ICoreActivityBannerService
{
    public List<CoreActivityBanner> selectActivityBannerList(CoreActivityBanner banner);

    public CoreActivityBanner selectActivityBannerById(Long id);

    public int insertActivityBanner(CoreActivityBanner banner);

    public int updateActivityBanner(CoreActivityBanner banner);

    public int deleteActivityBannerById(Long id);

    public int deleteActivityBannerByIds(Long[] ids);

    public int changeStatus(Long id, String status);
}
