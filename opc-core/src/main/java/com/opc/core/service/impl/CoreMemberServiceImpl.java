package com.opc.core.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        if (member.getPassword() == null || member.getPassword().isEmpty()) {
            member.setPassword("123456@654321");
        }
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
                mergedData.get(date).put("newUserCount", 0);
                mergedData.get(date).put("activeUserCount", 0);
            }

            Long newUserCount = toLong(row.get("newUserCount"));
            Long activeUserCount = toLong(row.get("activeUserCount"));
            Long normalUserCount = toLong(row.get("normalUserCount"));
            Long vipUserCount = toLong(row.get("vipUserCount"));

            if (newUserCount != null && newUserCount > 0)
            {
                mergedData.get(date).put("newUserCount", newUserCount);
            }
            if (activeUserCount != null && activeUserCount > 0)
            {
                mergedData.get(date).put("activeUserCount", activeUserCount);
            }
            if (normalUserCount != null && normalUserCount > 0)
            {
                mergedData.get(date).put("normalUserCount", normalUserCount);
            }
            if (vipUserCount != null && vipUserCount > 0)
            {
                mergedData.get(date).put("vipUserCount", vipUserCount);
            }
        }

        return new ArrayList<>(mergedData.values());
    }

    private Long toLong(Object obj)
    {
        if (obj == null) return null;
        if (obj instanceof Long) return (Long) obj;
        if (obj instanceof Integer) return ((Integer) obj).longValue();
        try
        {
            return Long.parseLong(String.valueOf(obj));
        }
        catch (NumberFormatException e)
        {
            return null;
        }
    }
}
