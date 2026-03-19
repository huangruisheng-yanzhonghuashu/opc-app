package com.opc.core.service.impl;

import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.opc.common.exception.ServiceException;
import com.opc.common.utils.ip.IpUtils;
import com.opc.core.domain.CoreMember;
import com.opc.core.domain.vo.MemberLoginVO;
import com.opc.core.service.ICoreMemberService;
import com.opc.core.service.IMemberLoginService;
import com.opc.core.service.MemberTokenService;

@Service
public class MemberLoginServiceImpl implements IMemberLoginService
{
    @Autowired
    private MemberTokenService memberTokenService;

    @Autowired
    private ICoreMemberService memberService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public String login(String email, String password)
    {
        if (email == null || email.isBlank() || password == null || password.isBlank())
        {
            throw new ServiceException("邮箱或密码不能为空");
        }

        CoreMember member = memberService.selectMemberByEmail(email);
        if (member == null)
        {
            throw new ServiceException("会员不存在");
        }

        if (!passwordEncoder.matches(password, member.getPassword()))
        {
            throw new ServiceException("密码错误");
        }

        if ("1".equals(member.getStatus()))
        {
            throw new ServiceException("会员已被禁用");
        }

        Set<String> permissions = new HashSet<>();
        permissions.add("member:user");

        MemberLoginVO memberLoginVO = new MemberLoginVO();
        memberLoginVO.setMemberId(member.getId());
        memberLoginVO.setEmail(member.getEmail());
        memberLoginVO.setUsername(member.getUsername());
        memberLoginVO.setNickname(member.getNickname());
        memberLoginVO.setAvatar(member.getAvatar());
        memberLoginVO.setStatus(member.getStatus());
        memberLoginVO.setPermissions(permissions);

        memberService.updateLoginInfo(member.getId(), IpUtils.getIpAddr());

        return memberTokenService.createToken(memberLoginVO);
    }

    @Override
    public MemberLoginVO getMemberLoginUser()
    {
        return memberTokenService.getLoginUser(
                com.opc.common.utils.ServletUtils.getRequest());
    }

    @Override
    public void logout(String token)
    {
        memberTokenService.delLoginUser(token);
    }
}