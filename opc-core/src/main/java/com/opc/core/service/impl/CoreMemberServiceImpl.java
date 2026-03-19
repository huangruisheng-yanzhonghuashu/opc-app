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
    public CoreMember selectMemberById(Long id)
    {
        return memberMapper.selectMemberById(id);
    }

    @Override
    public int insertMember(CoreMember member)
    {
        convertPackageToLevel(member);
        return memberMapper.insertMember(member);
    }

    @Override
    public int updateMember(CoreMember member)
    {
        convertPackageToLevel(member);
        return memberMapper.updateMember(member);
    }

    private void convertPackageToLevel(CoreMember member) {
        if (member.getCurrentPackage() != null) {
            String packageName = member.getCurrentPackage();
            if ("一级".equals(packageName)) {
                member.setCurrentPackageLevel(1);
            } else if ("二级".equals(packageName)) {
                member.setCurrentPackageLevel(2);
            } else if ("三级".equals(packageName)) {
                member.setCurrentPackageLevel(3);
            }
        }
    }

    @Override
    public boolean checkMemberNameUnique(CoreMember member)
    {
        Long id = StringUtils.isNull(member.getId()) ? -1L : member.getId();
        CoreMember info = memberMapper.checkMemberNameUnique(member.getUsername());
        if (StringUtils.isNotNull(info) && info.getId().longValue() != id.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public boolean checkPhoneUnique(CoreMember member)
    {
        Long id = StringUtils.isNull(member.getId()) ? -1L : member.getId();
        CoreMember info = memberMapper.checkPhoneUnique(member.getPhoneNumber());
        if (StringUtils.isNotNull(info) && info.getId().longValue() != id.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public boolean checkEmailUnique(CoreMember member)
    {
        Long id = StringUtils.isNull(member.getId()) ? -1L : member.getId();
        CoreMember info = memberMapper.checkEmailUnique(member.getEmail());
        if (StringUtils.isNotNull(info) && info.getId().longValue() != id.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public int blockMember(Long id)
    {
        return memberMapper.blockMember(id);
    }

    @Override
    public int unblockMember(Long id)
    {
        return memberMapper.unblockMember(id);
    }
}
