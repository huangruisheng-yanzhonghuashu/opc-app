package com.opc.mobile.controller;

import com.opc.common.core.domain.AjaxResult;
import com.opc.common.core.redis.RedisCache;
import com.opc.core.domain.CoreActivationCode;
import com.opc.core.domain.CoreMember;
import com.opc.core.service.ICoreActivationCodeService;
import com.opc.core.service.ICoreMemberService;
import com.opc.mobile.dto.MobileRegisterDTO;
import com.opc.system.service.ISysConfigService;
import com.opc.web.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MobileRegisterControllerTest {

    @Mock
    private ICoreMemberService memberService;

    @Mock
    private ISysConfigService configService;

    @Mock
    private RedisCache redisCache;

    @Mock
    private EmailService emailService;

    @Mock
    private ICoreActivationCodeService activationCodeService;

    @InjectMocks
    private MobileRegisterController registerController;

    @BeforeEach
    public void setup() {
        // 设置跳过邮箱验证码验证，便于测试
        ReflectionTestUtils.setField(registerController, "skipEmailCode", true);
    }

    @Test
    public void testRegisterByEmail_InviteCodeEmpty() {
        MobileRegisterDTO dto = new MobileRegisterDTO();
        dto.setUsername("testuser");
        dto.setPassword("password123");
        dto.setEmail("test@example.com");
        dto.setCode("123456");
        dto.setInviteCode(""); // 空激活码

        AjaxResult result = registerController.registerByEmail(dto);

        assertNotNull(result);
        assertEquals("激活码（邀请码）不能为空", result.get("msg"));
    }

    @Test
    public void testRegisterByEmail_InviteCodeNull() {
        MobileRegisterDTO dto = new MobileRegisterDTO();
        dto.setUsername("testuser");
        dto.setPassword("password123");
        dto.setEmail("test@example.com");
        dto.setCode("123456");
        dto.setInviteCode(null); // null激活码

        AjaxResult result = registerController.registerByEmail(dto);

        assertNotNull(result);
        assertEquals("激活码（邀请码）不能为空", result.get("msg"));
    }

    @Test
    public void testRegisterByEmail_InviteCodeNotFound() {
        MobileRegisterDTO dto = new MobileRegisterDTO();
        dto.setUsername("testuser");
        dto.setPassword("password123");
        dto.setEmail("test@example.com");
        dto.setCode("123456");
        dto.setInviteCode("ACNOTEXIST12345678");

        when(activationCodeService.selectCoreActivationCodeByCode("ACNOTEXIST12345678")).thenReturn(null);
        when(memberService.checkMemberNameUnique(any())).thenReturn(true);
        when(memberService.checkEmailUnique(any())).thenReturn(true);

        AjaxResult result = registerController.registerByEmail(dto);

        assertNotNull(result);
        assertEquals("激活码不存在", result.get("msg"));
    }

    @Test
    public void testRegisterByEmail_InviteCodeAlreadyUsed() {
        MobileRegisterDTO dto = new MobileRegisterDTO();
        dto.setUsername("testuser");
        dto.setPassword("password123");
        dto.setEmail("test@example.com");
        dto.setCode("123456");
        dto.setInviteCode("ACALREADYUSED12345");

        CoreActivationCode activationCode = new CoreActivationCode();
        activationCode.setId(1L);
        activationCode.setCode("ACALREADYUSED12345");
        activationCode.setStatus("2"); // 已使用

        when(activationCodeService.selectCoreActivationCodeByCode("ACALREADYUSED12345")).thenReturn(activationCode);
        when(memberService.checkMemberNameUnique(any())).thenReturn(true);
        when(memberService.checkEmailUnique(any())).thenReturn(true);

        AjaxResult result = registerController.registerByEmail(dto);

        assertNotNull(result);
        assertEquals("激活码已被使用", result.get("msg"));
    }

    @Test
    public void testRegisterByEmail_InviteCodeCancelled() {
        MobileRegisterDTO dto = new MobileRegisterDTO();
        dto.setUsername("testuser");
        dto.setPassword("password123");
        dto.setEmail("test@example.com");
        dto.setCode("123456");
        dto.setInviteCode("ACCANCELLED1234567");

        CoreActivationCode activationCode = new CoreActivationCode();
        activationCode.setId(1L);
        activationCode.setCode("ACCANCELLED1234567");
        activationCode.setStatus("3"); // 已注销

        when(activationCodeService.selectCoreActivationCodeByCode("ACCANCELLED1234567")).thenReturn(activationCode);
        when(memberService.checkMemberNameUnique(any())).thenReturn(true);
        when(memberService.checkEmailUnique(any())).thenReturn(true);

        AjaxResult result = registerController.registerByEmail(dto);

        assertNotNull(result);
        assertEquals("激活码已注销", result.get("msg"));
    }

    @Test
    public void testRegisterByEmail_InviteCodeExpiredStatus() {
        MobileRegisterDTO dto = new MobileRegisterDTO();
        dto.setUsername("testuser");
        dto.setPassword("password123");
        dto.setEmail("test@example.com");
        dto.setCode("123456");
        dto.setInviteCode("ACEXPIRED123456789");

        CoreActivationCode activationCode = new CoreActivationCode();
        activationCode.setId(1L);
        activationCode.setCode("ACEXPIRED123456789");
        activationCode.setStatus("4"); // 已过期状态

        when(activationCodeService.selectCoreActivationCodeByCode("ACEXPIRED123456789")).thenReturn(activationCode);
        when(memberService.checkMemberNameUnique(any())).thenReturn(true);
        when(memberService.checkEmailUnique(any())).thenReturn(true);

        AjaxResult result = registerController.registerByEmail(dto);

        assertNotNull(result);
        assertEquals("激活码已过期", result.get("msg"));
    }

    @Test
    public void testRegisterByEmail_InviteCodeExpiredTime() {
        MobileRegisterDTO dto = new MobileRegisterDTO();
        dto.setUsername("testuser");
        dto.setPassword("password123");
        dto.setEmail("test@example.com");
        dto.setCode("123456");
        dto.setInviteCode("ACTIMEEXPIRED12345");

        CoreActivationCode activationCode = new CoreActivationCode();
        activationCode.setId(1L);
        activationCode.setCode("ACTIMEEXPIRED12345");
        activationCode.setStatus("1"); // 已发送-未使用
        // 设置过期时间为昨天
        Date yesterday = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
        activationCode.setExpireTime(yesterday);

        when(activationCodeService.selectCoreActivationCodeByCode("ACTIMEEXPIRED12345")).thenReturn(activationCode);
        when(memberService.checkMemberNameUnique(any())).thenReturn(true);
        when(memberService.checkEmailUnique(any())).thenReturn(true);

        AjaxResult result = registerController.registerByEmail(dto);

        assertNotNull(result);
        assertEquals("激活码已过期", result.get("msg"));
    }

    @Test
    public void testRegisterByEmail_InviteCodeNotUsedSuccess() {
        MobileRegisterDTO dto = new MobileRegisterDTO();
        dto.setUsername("testuser");
        dto.setPassword("password123");
        dto.setEmail("test@example.com");
        dto.setCode("123456");
        dto.setInviteCode("ACNOTUSED123456789");

        CoreActivationCode activationCode = new CoreActivationCode();
        activationCode.setId(1L);
        activationCode.setCode("ACNOTUSED123456789");
        activationCode.setStatus("0"); // 未使用
        // 设置过期时间为明天
        Date tomorrow = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
        activationCode.setExpireTime(tomorrow);

        when(activationCodeService.selectCoreActivationCodeByCode("ACNOTUSED123456789")).thenReturn(activationCode);
        when(memberService.checkMemberNameUnique(any())).thenReturn(true);
        when(memberService.checkEmailUnique(any())).thenReturn(true);
        when(memberService.insertMember(any(CoreMember.class))).thenAnswer(invocation -> {
            CoreMember member = invocation.getArgument(0);
            member.setId(100L);
            return 1;
        });
        when(activationCodeService.useActivationCode(anyString(), anyLong())).thenReturn(1);

        AjaxResult result = registerController.registerByEmail(dto);

        assertNotNull(result);
        assertEquals("注册成功", result.get("msg"));
        verify(activationCodeService).useActivationCode(eq("ACNOTUSED123456789"), eq(100L));
    }

    @Test
    public void testRegisterByEmail_InviteCodeSentSuccess() {
        MobileRegisterDTO dto = new MobileRegisterDTO();
        dto.setUsername("testuser");
        dto.setPassword("password123");
        dto.setEmail("test@example.com");
        dto.setCode("123456");
        dto.setInviteCode("ACSENT123456789012");

        CoreActivationCode activationCode = new CoreActivationCode();
        activationCode.setId(1L);
        activationCode.setCode("ACSENT123456789012");
        activationCode.setStatus("1"); // 已发送-未使用
        // 设置过期时间为明天
        Date tomorrow = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
        activationCode.setExpireTime(tomorrow);

        when(activationCodeService.selectCoreActivationCodeByCode("ACSENT123456789012")).thenReturn(activationCode);
        when(memberService.checkMemberNameUnique(any())).thenReturn(true);
        when(memberService.checkEmailUnique(any())).thenReturn(true);
        when(memberService.insertMember(any(CoreMember.class))).thenAnswer(invocation -> {
            CoreMember member = invocation.getArgument(0);
            member.setId(100L);
            return 1;
        });
        when(activationCodeService.useActivationCode(anyString(), anyLong())).thenReturn(1);

        AjaxResult result = registerController.registerByEmail(dto);

        assertNotNull(result);
        assertEquals("注册成功", result.get("msg"));
        verify(activationCodeService).useActivationCode(eq("ACSENT123456789012"), eq(100L));
    }

    @Test
    public void testRegisterByEmail_MemberInviteCodeSet() {
        MobileRegisterDTO dto = new MobileRegisterDTO();
        dto.setUsername("testuser");
        dto.setPassword("password123");
        dto.setEmail("test@example.com");
        dto.setCode("123456");
        dto.setInviteCode("ACINVITE1234567890");

        CoreActivationCode activationCode = new CoreActivationCode();
        activationCode.setId(1L);
        activationCode.setCode("ACINVITE1234567890");
        activationCode.setStatus("1");
        Date tomorrow = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
        activationCode.setExpireTime(tomorrow);

        when(activationCodeService.selectCoreActivationCodeByCode("ACINVITE1234567890")).thenReturn(activationCode);
        when(memberService.checkMemberNameUnique(any())).thenReturn(true);
        when(memberService.checkEmailUnique(any())).thenReturn(true);

        // 捕获插入的会员对象，验证inviteCode字段
        when(memberService.insertMember(any(CoreMember.class))).thenAnswer(invocation -> {
            CoreMember member = invocation.getArgument(0);
            member.setId(100L);
            // 验证inviteCode被正确设置
            assertEquals("ACINVITE1234567890", member.getInviteCode());
            return 1;
        });
        when(activationCodeService.useActivationCode(anyString(), anyLong())).thenReturn(1);

        AjaxResult result = registerController.registerByEmail(dto);

        assertNotNull(result);
        assertEquals("注册成功", result.get("msg"));
    }

    @Test
    public void testRegisterByEmail_RegisterFailActivationCodeNotUsed() {
        MobileRegisterDTO dto = new MobileRegisterDTO();
        dto.setUsername("testuser");
        dto.setPassword("password123");
        dto.setEmail("test@example.com");
        dto.setCode("123456");
        dto.setInviteCode("ACFAIL123456789012");

        CoreActivationCode activationCode = new CoreActivationCode();
        activationCode.setId(1L);
        activationCode.setCode("ACFAIL123456789012");
        activationCode.setStatus("1");
        Date tomorrow = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
        activationCode.setExpireTime(tomorrow);

        when(activationCodeService.selectCoreActivationCodeByCode("ACFAIL123456789012")).thenReturn(activationCode);
        when(memberService.checkMemberNameUnique(any())).thenReturn(true);
        when(memberService.checkEmailUnique(any())).thenReturn(true);
        when(memberService.insertMember(any(CoreMember.class))).thenReturn(0); // 插入失败

        AjaxResult result = registerController.registerByEmail(dto);

        assertNotNull(result);
        assertEquals("注册失败，请联系系统管理人员", result.get("msg"));
        // 验证useActivationCode没有被调用
        verify(activationCodeService, never()).useActivationCode(anyString(), anyLong());
    }
}
