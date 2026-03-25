package com.opc.core.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.opc.common.utils.StringUtils;
import com.opc.core.domain.CoreCommunity;
import com.opc.core.mapper.CoreCommunityMapper;
import com.opc.core.service.ICoreCommunityService;

@Service
public class CoreCommunityServiceImpl implements ICoreCommunityService
{
    @Autowired
    private CoreCommunityMapper communityMapper;

    /**
     * 查询社区信息
     *
     * @param id 社区ID
     * @return 社区信息
     */
    @Override
    public CoreCommunity selectCommunityById(Long id)
    {
        return communityMapper.selectCommunityById(id);
    }

    /**
     * 查询社区列表
     *
     * @param community 社区信息
     * @return 社区集合
     */
    @Override
    public List<CoreCommunity> selectCommunityList(CoreCommunity community)
    {
        return communityMapper.selectCommunityList(community);
    }

    /**
     * 新增社区
     *
     * @param community 社区信息
     * @return 结果
     */
    @Override
    public int insertCommunity(CoreCommunity community)
    {
        return communityMapper.insertCommunity(community);
    }

    /**
     * 修改社区
     *
     * @param community 社区信息
     * @return 结果
     */
    @Override
    public int updateCommunity(CoreCommunity community)
    {
        return communityMapper.updateCommunity(community);
    }

    /**
     * 批量删除社区
     *
     * @param ids 需要删除的社区ID
     * @return 结果
     */
    @Override
    public int deleteCommunityByIds(Long[] ids)
    {
        return communityMapper.deleteCommunityByIds(ids);
    }

    /**
     * 删除社区信息
     *
     * @param id 社区ID
     * @return 结果
     */
    @Override
    public int deleteCommunityById(Long id)
    {
        return communityMapper.deleteCommunityById(id);
    }

    /**
     * 校验社区名称是否唯一
     *
     * @param community 社区信息
     * @return 结果
     */
    @Override
    public boolean checkCommunityNameUnique(CoreCommunity community)
    {
        Long communityId = StringUtils.isNull(community.getId()) ? -1L : community.getId();
        CoreCommunity info = communityMapper.checkCommunityNameUnique(community.getName());
        if (StringUtils.isNotNull(info) && info.getId().longValue() != communityId.longValue())
        {
            return false;
        }
        return true;
    }
}
