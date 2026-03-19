package com.opc.core.service;

import com.opc.core.domain.vo.MemberLoginVO;

public interface IMemberLoginService
{
    String login(String email, String password);

    MemberLoginVO getMemberLoginUser();

    void logout(String token);
}