package com.opc.core.mapper;

import java.util.List;
import java.util.Map;
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

    public CoreMember checkNicknameUnique(String nickname);

    public int blockMember(Long id);

    public int unblockMember(Long id);

    /**
     * 注销会员（状态改为已注销）
     * @param id 会员ID
     * @return 结果
     */
    public int cancelMember(Long id);

    public int updateLoginInfo(Long id, String ipaddr);

    public List<Map<String, Object>> selectMemberOverview(Map<String, Object> params);
}
