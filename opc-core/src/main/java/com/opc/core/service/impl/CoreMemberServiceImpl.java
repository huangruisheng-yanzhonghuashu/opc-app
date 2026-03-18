package com.opc.core.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.opc.common.constant.UserConstants;
import com.opc.common.utils.StringUtils;
import com.opc.core.domain.CoreMember;
import com.opc.core.mapper.CoreMemberMapper;
import com.opc.core.service.ICoreMemberService;

@Service
public class CoreMemberServiceImpl implements ICoreMemberService
{
    @Autowired
    private CoreMemberMapper memberMapper;

    @Override
    public List<CoreMember> selectMemberList(CoreMember member)
    {
        return memberMapper.selectMemberList(member);
    }

    @Override
    public CoreMember selectMemberById(Long memberId)
    {
        return memberMapper.selectMemberById(memberId);
    }

    @Override
    public int deleteMemberById(Long memberId)
    {
        return memberMapper.deleteMemberById(memberId);
    }

    @Override
    public int deleteMemberByIds(Long[] memberIds)
    {
        return memberMapper.deleteMemberByIds(memberIds);
    }

    @Override
    public int insertMember(CoreMember member)
    {
        return memberMapper.insertMember(member);
    }

    @Override
    public int updateMember(CoreMember member)
    {
        return memberMapper.updateMember(member);
    }

    @Override
    public boolean checkMemberNameUnique(CoreMember member)
    {
        Long memberId = StringUtils.isNull(member.getMemberId()) ? -1L : member.getMemberId();
        CoreMember info = memberMapper.checkMemberNameUnique(member.getUsername());
        if (StringUtils.isNotNull(info) && info.getMemberId().longValue() != memberId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public boolean checkPhoneUnique(CoreMember member)
    {
        Long memberId = StringUtils.isNull(member.getMemberId()) ? -1L : member.getMemberId();
        CoreMember info = memberMapper.checkPhoneUnique(member.getPhoneNumber());
        if (StringUtils.isNotNull(info) && info.getMemberId().longValue() != memberId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public boolean checkEmailUnique(CoreMember member)
    {
        Long memberId = StringUtils.isNull(member.getMemberId()) ? -1L : member.getMemberId();
        CoreMember info = memberMapper.checkEmailUnique(member.getEmail());
        if (StringUtils.isNotNull(info) && info.getMemberId().longValue() != memberId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }
}
