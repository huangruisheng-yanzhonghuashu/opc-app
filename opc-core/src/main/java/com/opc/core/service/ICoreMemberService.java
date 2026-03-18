package com.opc.core.service;

import java.util.List;
import com.opc.core.domain.CoreMember;

public interface ICoreMemberService
{
    public List<CoreMember> selectMemberList(CoreMember member);

    public CoreMember selectMemberById(Long memberId);

    public int deleteMemberById(Long memberId);

    public int deleteMemberByIds(Long[] memberIds);

    public int insertMember(CoreMember member);

    public int updateMember(CoreMember member);

    public boolean checkMemberNameUnique(CoreMember member);

    public boolean checkPhoneUnique(CoreMember member);

    public boolean checkEmailUnique(CoreMember member);
}
