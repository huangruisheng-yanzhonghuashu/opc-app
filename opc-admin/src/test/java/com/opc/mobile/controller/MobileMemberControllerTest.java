package com.opc.mobile.controller;

import com.opc.common.core.domain.AjaxResult;
import com.opc.common.core.redis.RedisCache;
import com.opc.core.domain.CoreMember;
import com.opc.core.domain.vo.MemberLoginVO;
import com.opc.core.service.ICoreMemberService;
import com.opc.core.service.MemberTokenService;
import com.opc.mobile.dto.MemberUpdatePasswordDTO;
import com.opc.mobile.dto.MemberBindEmailDTO;
import com.opc.mobile.dto.EmailCodeRequestDTO;
import com.opc.framework.config.ServerConfig;
import com.opc.web.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MobileMemberControllerTest
{
    @Mock
    private MemberTokenService memberTokenService;

    @Mock
    private ICoreMemberService memberService;

    @Mock
    private ServerConfig serverConfig;

    @Mock
    private RedisCache redisCache;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private MobileMemberController mobileMemberController;

    private MemberLoginVO memberLoginVO;
    private CoreMember member;

    @BeforeEach
    public void setup()
    {
        ReflectionTestUtils.setField(mobileMemberController, "mailFrom", "test@example.com");

        memberLoginVO = new MemberLoginVO();
        memberLoginVO.setMemberId(1L);
        memberLoginVO.setUsername("testuser");
        memberLoginVO.setEmail("test@example.com");

        member = new CoreMember();
        member.setId(1L);
        member.setUsername("testuser");
        member.setEmail("test@example.com");
        member.setPassword("$2a$10$encodedpassword");
        member.setStatus("0");
    }

    @Test
    public void testUpdatePassword_NotLoggedIn()
    {
        when(memberTokenService.getLoginUser(any())).thenReturn(null);

        MemberUpdatePasswordDTO dto = new MemberUpdatePasswordDTO();
        dto.setOldPassword("oldPassword");
        dto.setNewPassword("newPassword123");
        dto.setConfirmPassword("newPassword123");

        AjaxResult result = mobileMemberController.updatePassword(dto, null);

        assertNotNull(result);
        assertEquals("请先登录", result.get("msg"));
    }

    @Test
    public void testUpdatePassword_PasswordTooShort()
    {
        when(memberTokenService.getLoginUser(any())).thenReturn(memberLoginVO);

        MemberUpdatePasswordDTO dto = new MemberUpdatePasswordDTO();
        dto.setOldPassword("oldPassword");
        dto.setNewPassword("123");
        dto.setConfirmPassword("123");

        AjaxResult result = mobileMemberController.updatePassword(dto, null);

        assertNotNull(result);
        assertEquals("新密码长度必须在6-20位之间", result.get("msg"));
    }

    @Test
    public void testUpdatePassword_ConfirmPasswordMismatch()
    {
        when(memberTokenService.getLoginUser(any())).thenReturn(memberLoginVO);

        MemberUpdatePasswordDTO dto = new MemberUpdatePasswordDTO();
        dto.setOldPassword("oldPassword");
        dto.setNewPassword("newPassword123");
        dto.setConfirmPassword("differentPassword");

        AjaxResult result = mobileMemberController.updatePassword(dto, null);

        assertNotNull(result);
        assertEquals("两次输入的密码不一致", result.get("msg"));
    }

    @Test
    public void testUpdatePassword_MemberNotExist()
    {
        when(memberTokenService.getLoginUser(any())).thenReturn(memberLoginVO);
        when(memberService.selectMemberById(1L)).thenReturn(null);

        MemberUpdatePasswordDTO dto = new MemberUpdatePasswordDTO();
        dto.setOldPassword("oldPassword");
        dto.setNewPassword("newPassword123");
        dto.setConfirmPassword("newPassword123");

        AjaxResult result = mobileMemberController.updatePassword(dto, null);

        assertNotNull(result);
        assertEquals("会员不存在", result.get("msg"));
    }

    @Test
    public void testSendBindEmailCode_NotLoggedIn()
    {
        when(memberTokenService.getLoginUser(any())).thenReturn(null);

        EmailCodeRequestDTO dto = new EmailCodeRequestDTO();
        dto.setEmail("test@example.com");

        AjaxResult result = mobileMemberController.sendBindEmailCode(dto, null);

        assertNotNull(result);
        assertEquals("请先登录", result.get("msg"));
    }

    @Test
    public void testSendBindEmailCode_InvalidEmailFormat()
    {
        when(memberTokenService.getLoginUser(any())).thenReturn(memberLoginVO);

        EmailCodeRequestDTO dto = new EmailCodeRequestDTO();
        dto.setEmail("invalid-email");

        AjaxResult result = mobileMemberController.sendBindEmailCode(dto, null);

        assertNotNull(result);
        assertEquals("邮箱格式不正确", result.get("msg"));
    }

    @Test
    public void testSendBindEmailCode_EmailAlreadyBound()
    {
        when(memberTokenService.getLoginUser(any())).thenReturn(memberLoginVO);

        EmailCodeRequestDTO dto = new EmailCodeRequestDTO();
        dto.setEmail("existing@example.com");

        when(memberService.checkEmailUnique(any())).thenReturn(false);

        AjaxResult result = mobileMemberController.sendBindEmailCode(dto, null);

        assertNotNull(result);
        assertEquals("该邮箱已被其他账户绑定", result.get("msg"));
    }

    @Test
    public void testSendBindEmailCode_Success()
    {
        when(memberTokenService.getLoginUser(any())).thenReturn(memberLoginVO);
        when(memberService.checkEmailUnique(any())).thenReturn(true);
        when(redisCache.getCacheObject(anyString())).thenReturn(null);
        when(emailService.sendHtmlEmail(any())).thenReturn(true);

        EmailCodeRequestDTO dto = new EmailCodeRequestDTO();
        dto.setEmail("newemail@example.com");

        AjaxResult result = mobileMemberController.sendBindEmailCode(dto, null);

        assertNotNull(result);
        assertEquals("验证码已发送至您的邮箱，有效期5分钟", result.get("msg"));
        verify(redisCache).setCacheObject(anyString(), anyString(), eq(5), eq(TimeUnit.MINUTES));
    }

    @Test
    public void testBindEmail_NotLoggedIn()
    {
        when(memberTokenService.getLoginUser(any())).thenReturn(null);

        MemberBindEmailDTO dto = new MemberBindEmailDTO();
        dto.setEmail("test@example.com");
        dto.setCode("123456");

        AjaxResult result = mobileMemberController.bindEmail(dto, null);

        assertNotNull(result);
        assertEquals("请先登录", result.get("msg"));
    }

    @Test
    public void testBindEmail_InvalidEmailFormat()
    {
        when(memberTokenService.getLoginUser(any())).thenReturn(memberLoginVO);

        MemberBindEmailDTO dto = new MemberBindEmailDTO();
        dto.setEmail("invalid-email");
        dto.setCode("123456");

        AjaxResult result = mobileMemberController.bindEmail(dto, null);

        assertNotNull(result);
        assertEquals("邮箱格式不正确", result.get("msg"));
    }

    @Test
    public void testBindEmail_MemberNotExist()
    {
        when(memberTokenService.getLoginUser(any())).thenReturn(memberLoginVO);
        when(memberService.selectMemberById(1L)).thenReturn(null);

        MemberBindEmailDTO dto = new MemberBindEmailDTO();
        dto.setEmail("newemail@example.com");
        dto.setCode("123456");

        AjaxResult result = mobileMemberController.bindEmail(dto, null);

        assertNotNull(result);
        assertEquals("会员不存在", result.get("msg"));
    }

    @Test
    public void testBindEmail_EmailAlreadyBoundByOther()
    {
        when(memberTokenService.getLoginUser(any())).thenReturn(memberLoginVO);
        when(memberService.selectMemberById(1L)).thenReturn(member);
        when(memberService.checkEmailUnique(any())).thenReturn(false);

        MemberBindEmailDTO dto = new MemberBindEmailDTO();
        dto.setEmail("existing@example.com");
        dto.setCode("123456");

        AjaxResult result = mobileMemberController.bindEmail(dto, null);

        assertNotNull(result);
        assertEquals("该邮箱已被其他账户绑定", result.get("msg"));
    }

    @Test
    public void testBindEmail_CodeExpired()
    {
        when(memberTokenService.getLoginUser(any())).thenReturn(memberLoginVO);
        when(memberService.selectMemberById(1L)).thenReturn(member);
        when(memberService.checkEmailUnique(any())).thenReturn(true);
        when(redisCache.getCacheObject(anyString())).thenReturn(null);

        MemberBindEmailDTO dto = new MemberBindEmailDTO();
        dto.setEmail("newemail@example.com");
        dto.setCode("123456");

        AjaxResult result = mobileMemberController.bindEmail(dto, null);

        assertNotNull(result);
        assertEquals("验证码已过期，请重新获取", result.get("msg"));
    }

    @Test
    public void testBindEmail_WrongCode()
    {
        when(memberTokenService.getLoginUser(any())).thenReturn(memberLoginVO);
        when(memberService.selectMemberById(1L)).thenReturn(member);
        when(memberService.checkEmailUnique(any())).thenReturn(true);
        when(redisCache.getCacheObject(anyString())).thenReturn("123456");

        MemberBindEmailDTO dto = new MemberBindEmailDTO();
        dto.setEmail("newemail@example.com");
        dto.setCode("000000");

        AjaxResult result = mobileMemberController.bindEmail(dto, null);

        assertNotNull(result);
        assertEquals("验证码错误", result.get("msg"));
    }

    @Test
    public void testBindEmail_Success()
    {
        when(memberTokenService.getLoginUser(any())).thenReturn(memberLoginVO);
        when(memberService.selectMemberById(1L)).thenReturn(member);
        when(memberService.checkEmailUnique(any())).thenReturn(true);
        when(redisCache.getCacheObject(anyString())).thenReturn("123456");
        when(memberService.updateMember(any())).thenReturn(1);

        MemberBindEmailDTO dto = new MemberBindEmailDTO();
        dto.setEmail("newemail@example.com");
        dto.setCode("123456");

        AjaxResult result = mobileMemberController.bindEmail(dto, null);

        assertNotNull(result);
        assertEquals("邮箱绑定成功", result.get("msg"));
        verify(redisCache).deleteObject(anyString());
    }
}
