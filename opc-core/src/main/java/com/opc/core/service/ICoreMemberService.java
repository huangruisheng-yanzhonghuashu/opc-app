package com.opc.core.service;

import java.util.List;
import java.util.Map;
import com.opc.core.domain.CoreMember;

public interface ICoreMemberService
{
    public List<CoreMember> selectMemberList(CoreMember member);

    public CoreMember selectMemberById(Long id);

    public CoreMember selectMemberByEmail(String email);

    public int insertMember(CoreMember member);

    public int updateMember(CoreMember member);

    public boolean checkMemberNameUnique(CoreMember member);

    public boolean checkPhoneUnique(CoreMember member);

    public boolean checkEmailUnique(CoreMember member);

    public int blockMember(Long id);

    public int unblockMember(Long id);

    public int updateLoginInfo(Long id, String ipaddr);

    public List<Map<String, Object>> getMemberOverview(String startDate, String endDate);
}
