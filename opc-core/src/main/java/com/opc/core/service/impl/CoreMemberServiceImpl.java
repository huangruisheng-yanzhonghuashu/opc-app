package com.opc.core.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.opc.common.constant.UserConstants;
import com.opc.common.utils.StringUtils;
import com.opc.core.domain.CoreMember;
import com.opc.core.domain.CoreMemberCancel;
import com.opc.core.mapper.CoreMemberMapper;
import com.opc.core.mapper.CoreMemberCancelMapper;
import com.opc.core.service.ICoreMemberService;

@Service
public class CoreMemberServiceImpl implements ICoreMemberService
{
    @Autowired
    private CoreMemberMapper memberMapper;

    @Autowired
    private CoreMemberCancelMapper memberCancelMapper;

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
        if (member.getPassword() == null || member.getPassword().isEmpty()) {
            member.setPassword("123456@654321");
        }
        // 设置注册时间为当前时间
        if (member.getRegisterTime() == null) {
            member.setRegisterTime(java.time.Instant.now());
        }
        convertPackageToType(member);
        return memberMapper.insertMember(member);
    }

    @Override
    public int updateMember(CoreMember member)
    {
        convertPackageToType(member);
        return memberMapper.updateMember(member);
    }

    private void convertPackageToType(CoreMember member) {
        if (member.getCurrentPackage() != null) {
            String packageName = member.getCurrentPackage();
            if ("普通会员".equals(packageName)) {
                member.setPackageType(1);
            } else if ("VIP会员".equals(packageName)) {
                member.setPackageType(2);
            } else if ("超级VIP会员".equals(packageName)) {
                member.setPackageType(3);
            }
        }
        // 如果packageType为空，默认为1（普通会员）
        if (member.getPackageType() == null) {
            member.setPackageType(1);
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
    public boolean checkNicknameUnique(CoreMember member)
    {
        Long id = StringUtils.isNull(member.getId()) ? -1L : member.getId();
        CoreMember info = memberMapper.checkNicknameUnique(member.getNickname());
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

    @Override
    @Transactional
    public int cancelMember(Long id)
    {
        CoreMember member = memberMapper.selectMemberById(id);
        if (member == null)
        {
            return 0;
        }

        // 将会员数据复制到注销表
        CoreMemberCancel cancel = new CoreMemberCancel();
        cancel.setId(member.getId());
        cancel.setUsername(member.getUsername());
        cancel.setPassword(member.getPassword());
        cancel.setNickname(member.getNickname());
        cancel.setPhoneNumber(member.getPhoneNumber());
        cancel.setEmail(member.getEmail());
        cancel.setAvatar(member.getAvatar());
        cancel.setLastActiveTime(member.getLastActiveTime());
        cancel.setCurrentPackage(member.getCurrentPackage());
        cancel.setPackageType(member.getPackageType());
        cancel.setSource(member.getSource());
        cancel.setSourceId(member.getSourceId());
        cancel.setToken(member.getToken());
        cancel.setStatus("2");
        cancel.setRegisterTime(member.getRegisterTime());
        cancel.setInviteCode(member.getInviteCode());
        cancel.setCancelTime(LocalDateTime.now());
        cancel.setCreateBy(member.getCreateBy());
        cancel.setCreateTime(member.getCreateTime());
        cancel.setUpdateBy(member.getUpdateBy());
        cancel.setUpdateTime(member.getUpdateTime());
        cancel.setRemark(member.getRemark());

        memberCancelMapper.insertMemberCancel(cancel);

        // 删除原会员表数据
        return memberMapper.deleteMemberById(id);
    }

    @Override
    public CoreMember selectMemberByEmail(String email)
    {
        return memberMapper.selectMemberByEmail(email);
    }

    @Override
    public int updateLoginInfo(Long id, String ipaddr)
    {
        return memberMapper.updateLoginInfo(id, ipaddr);
    }

    @Override
    public List<Map<String, Object>> getMemberOverview(String startDate, String endDate)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("startDate", startDate);
        params.put("endDate", endDate);

        List<Map<String, Object>> rawData = memberMapper.selectMemberOverview(params);

        Map<String, Map<String, Object>> mergedData = new HashMap<>();

        for (Map<String, Object> row : rawData)
        {
            String date = String.valueOf(row.get("date"));
            if (!mergedData.containsKey(date))
            {
                mergedData.put(date, new HashMap<>());
                mergedData.get(date).put("date", date);
                mergedData.get(date).put("newUserCount", 0L);
                mergedData.get(date).put("activeUserCount", 0L);
                mergedData.get(date).put("normalUserCount", 0L);
                mergedData.get(date).put("vipUserCount", 0L);
                mergedData.get(date).put("svipUserCount", 0L);
            }

            long newUserCount = toLongValue(row.get("newUserCount"));
            long activeUserCount = toLongValue(row.get("activeUserCount"));
            long normalUserCount = toLongValue(row.get("normalUserCount"));
            long vipUserCount = toLongValue(row.get("vipUserCount"));
            long svipUserCount = toLongValue(row.get("svipUserCount"));

            if (newUserCount > 0)
            {
                mergedData.get(date).put("newUserCount", newUserCount);
            }
            if (activeUserCount > 0)
            {
                mergedData.get(date).put("activeUserCount", activeUserCount);
            }
            if (normalUserCount > 0)
            {
                mergedData.get(date).put("normalUserCount", normalUserCount);
            }
            if (vipUserCount > 0)
            {
                mergedData.get(date).put("vipUserCount", vipUserCount);
            }
            if (svipUserCount > 0)
            {
                mergedData.get(date).put("svipUserCount", svipUserCount);
            }
        }

        return new ArrayList<>(mergedData.values());
    }

    private long toLongValue(Object obj)
    {
        if (obj == null) return 0L;
        if (obj instanceof Long) return (Long) obj;
        if (obj instanceof Integer) return ((Integer) obj).longValue();
        if (obj instanceof java.math.BigInteger) return ((java.math.BigInteger) obj).longValue();
        if (obj instanceof java.math.BigDecimal) return ((java.math.BigDecimal) obj).longValue();
        try
        {
            return Long.parseLong(String.valueOf(obj));
        }
        catch (NumberFormatException e)
        {
            return 0L;
        }
    }
}
