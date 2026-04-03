package com.opc.core.mapper;

import java.util.List;
import com.opc.core.domain.CoreActivityBanner;

public interface CoreActivityBannerMapper
{
    public List<CoreActivityBanner> selectActivityBannerList(CoreActivityBanner banner);

    public CoreActivityBanner selectActivityBannerById(Long id);

    public int insertActivityBanner(CoreActivityBanner banner);

    public int updateActivityBanner(CoreActivityBanner banner);

    public int deleteActivityBannerById(Long id);

    public int deleteActivityBannerByIds(Long[] ids);

    public int changeStatus(CoreActivityBanner banner);
}
