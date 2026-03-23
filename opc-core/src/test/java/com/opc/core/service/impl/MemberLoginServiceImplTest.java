package com.opc.core.service.impl;

import com.opc.common.exception.ServiceException;
import com.opc.core.domain.CoreMember;
import com.opc.core.domain.vo.MemberLoginVO;
import com.opc.core.service.ICoreMemberService;
import com.opc.core.service.MemberTokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberLoginServiceImplTest
{
    @Mock
    private MemberTokenService memberTokenService;

    @Mock
    private ICoreMemberService memberService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberLoginServiceImpl memberLoginService;

    @Test
    public void testLogin_Success()
    {
        String email = "test@example.com";
        String password = "password123";

        CoreMember member = new CoreMember();
        member.setId(1L);
        member.setEmail(email);
        member.setUsername("testuser");
        member.setNickname("Test User");
        member.setAvatar("https://example.com/avatar.jpg");
        member.setStatus("0");
        member.setPassword("$2a$10$encodedpassword");

        when(memberService.selectMemberByEmail(email)).thenReturn(member);
        when(passwordEncoder.matches(password, member.getPassword())).thenReturn(true);
        when(memberTokenService.createToken(any(MemberLoginVO.class))).thenReturn("mock-token");

        String token = memberLoginService.login(email, password);

        assertNotNull(token);
        assertEquals("mock-token", token);
        verify(memberService).updateLoginInfo(eq(1L), anyString());
    }

    @Test
    public void testLogin_EmailIsBlank()
    {
        assertThrows(ServiceException.class, () -> {
            memberLoginService.login("", "password");
        });
    }

    @Test
    public void testLogin_PasswordIsBlank()
    {
        assertThrows(ServiceException.class, () -> {
            memberLoginService.login("test@example.com", "");
        });
    }

    @Test
    public void testLogin_MemberNotExist()
    {
        String email = "notexist@example.com";
        String password = "password123";

        when(memberService.selectMemberByEmail(email)).thenReturn(null);

        assertThrows(ServiceException.class, () -> {
            memberLoginService.login(email, password);
        });
    }

    @Test
    public void testLogin_WrongPassword()
    {
        String email = "test@example.com";
        String password = "wrongpassword";

        CoreMember member = new CoreMember();
        member.setId(1L);
        member.setEmail(email);
        member.setPassword("$2a$10$encodedpassword");

        when(memberService.selectMemberByEmail(email)).thenReturn(member);
        when(passwordEncoder.matches(password, member.getPassword())).thenReturn(false);

        assertThrows(ServiceException.class, () -> {
            memberLoginService.login(email, password);
        });
    }

    @Test
    public void testLogin_MemberIsDisabled()
    {
        String email = "test@example.com";
        String password = "password123";

        CoreMember member = new CoreMember();
        member.setId(1L);
        member.setEmail(email);
        member.setPassword("$2a$10$encodedpassword");
        member.setStatus("1");

        when(memberService.selectMemberByEmail(email)).thenReturn(member);
        when(passwordEncoder.matches(password, member.getPassword())).thenReturn(true);

        assertThrows(ServiceException.class, () -> {
            memberLoginService.login(email, password);
        });
    }

    @Test
    public void testGetMemberLoginUser()
    {
        MemberLoginVO loginVO = new MemberLoginVO();
        loginVO.setMemberId(1L);
        loginVO.setUsername("testuser");

        when(memberTokenService.getLoginUser(any())).thenReturn(loginVO);

        MemberLoginVO result = memberLoginService.getMemberLoginUser();

        assertNotNull(result);
        assertEquals(1L, result.getMemberId());
        assertEquals("testuser", result.getUsername());
    }

    @Test
    public void testLogout()
    {
        String token = "test-token";

        memberLoginService.logout(token);

        verify(memberTokenService).delLoginUser(token);
    }
}
