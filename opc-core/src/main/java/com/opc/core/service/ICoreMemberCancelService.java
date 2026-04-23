package com.opc.core.service;

import java.util.List;
import com.opc.core.domain.CoreMemberCancel;

public interface ICoreMemberCancelService
{
    /**
     * 查询注销会员列表
     *
     * @param memberCancel 注销会员信息
     * @return 注销会员集合
     */
    public List<CoreMemberCancel> selectMemberCancelList(CoreMemberCancel memberCancel);

    /**
     * 根据ID查询注销会员
     *
     * @param id 会员ID
     * @return 注销会员信息
     */
    public CoreMemberCancel selectMemberCancelById(Long id);

    /**
     * 新增注销会员
     *
     * @param memberCancel 注销会员信息
     * @return 结果
     */
    public int insertMemberCancel(CoreMemberCancel memberCancel);
}
