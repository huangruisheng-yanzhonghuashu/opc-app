package com.opc.core.mapper;

import java.util.List;
import com.opc.core.domain.CoreMember;

public interface CoreMemberMapper
{
    public List<CoreMember> selectMemberList(CoreMember member);

    public CoreMember selectMemberById(Long id);

    public CoreMember selectMemberByEmail(String email);

    public int updateMember(CoreMember member);

    public int insertMember(CoreMember member);

    public CoreMember checkMemberNameUnique(String username);

    public CoreMember checkPhoneUnique(String phoneNumber);

    public CoreMember checkEmailUnique(String email);

    public int blockMember(Long id);

    public int unblockMember(Long id);

    public int updateLoginInfo(Long id, String ipaddr);
}
