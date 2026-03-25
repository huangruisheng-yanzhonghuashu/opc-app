package com.opc.core.service;

import java.util.List;
import com.opc.core.domain.CoreCommunity;

public interface ICoreCommunityService
{
    /**
     * 查询社区信息
     *
     * @param id 社区ID
     * @return 社区信息
     */
    public CoreCommunity selectCommunityById(Long id);

    /**
     * 查询社区列表
     *
     * @param community 社区信息
     * @return 社区集合
     */
    public List<CoreCommunity> selectCommunityList(CoreCommunity community);

    /**
     * 新增社区
     *
     * @param community 社区信息
     * @return 结果
     */
    public int insertCommunity(CoreCommunity community);

    /**
     * 修改社区
     *
     * @param community 社区信息
     * @return 结果
     */
    public int updateCommunity(CoreCommunity community);

    /**
     * 批量删除社区
     *
     * @param ids 需要删除的社区ID
     * @return 结果
     */
    public int deleteCommunityByIds(Long[] ids);

    /**
     * 删除社区信息
     *
     * @param id 社区ID
     * @return 结果
     */
    public int deleteCommunityById(Long id);

    /**
     * 校验社区名称是否唯一
     *
     * @param community 社区信息
     * @return 结果
     */
    public boolean checkCommunityNameUnique(CoreCommunity community);
}
