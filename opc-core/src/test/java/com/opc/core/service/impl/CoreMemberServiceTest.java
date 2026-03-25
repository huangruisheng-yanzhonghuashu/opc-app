package com.opc.core.service.impl;

import com.opc.common.constant.UserConstants;
import com.opc.core.domain.CoreMember;
import com.opc.core.mapper.CoreMemberMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CoreMemberServiceTest
{
    @Mock
    private CoreMemberMapper memberMapper;

    @InjectMocks
    private CoreMemberServiceImpl memberService;

    private CoreMember createTestMember()
    {
        CoreMember member = new CoreMember();
        member.setId(1L);
        member.setUsername("testuser");
        member.setNickname("Test User");
        member.setEmail("test@example.com");
        member.setPhoneNumber("13800138000");
        member.setPassword("password123");
        member.setStatus("0");
        return member;
    }

    @Test
    public void testSelectMemberList()
    {
        CoreMember member = createTestMember();
        List<CoreMember> memberList = Arrays.asList(member);

        when(memberMapper.selectMemberList(any(CoreMember.class))).thenReturn(memberList);

        List<CoreMember> result = memberService.selectMemberList(new CoreMember());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("testuser", result.get(0).getUsername());
    }

    @Test
    public void testSelectMemberById()
    {
        CoreMember member = createTestMember();

        when(memberMapper.selectMemberById(1L)).thenReturn(member);

        CoreMember result = memberService.selectMemberById(1L);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
    }

    @Test
    public void testSelectMemberByEmail()
    {
        CoreMember member = createTestMember();

        when(memberMapper.selectMemberByEmail("test@example.com")).thenReturn(member);

        CoreMember result = memberService.selectMemberByEmail("test@example.com");

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    public void testInsertMember()
    {
        CoreMember member = createTestMember();
        member.setPassword(null);

        when(memberMapper.insertMember(any(CoreMember.class))).thenReturn(1);

        int result = memberService.insertMember(member);

        assertEquals(1, result);
        assertNotNull(member.getPassword());
        assertEquals("123456@654321", member.getPassword());
    }

    @Test
    public void testInsertMemberWithPassword()
    {
        CoreMember member = createTestMember();
        member.setPassword("customPassword");

        when(memberMapper.insertMember(any(CoreMember.class))).thenReturn(1);

        int result = memberService.insertMember(member);

        assertEquals(1, result);
        assertEquals("customPassword", member.getPassword());
    }

    @Test
    public void testInsertMemberWithPackage()
    {
        CoreMember member = createTestMember();
        member.setCurrentPackage("VIP会员");

        when(memberMapper.insertMember(any(CoreMember.class))).thenReturn(1);

        int result = memberService.insertMember(member);

        assertEquals(1, result);
        assertEquals(2, member.getPackageType());
    }

    @Test
    public void testUpdateMember()
    {
        CoreMember member = createTestMember();

        when(memberMapper.updateMember(any(CoreMember.class))).thenReturn(1);

        int result = memberService.updateMember(member);

        assertEquals(1, result);
    }

    @Test
    public void testCheckMemberNameUnique_Unique()
    {
        CoreMember member = createTestMember();

        when(memberMapper.checkMemberNameUnique("testuser")).thenReturn(null);

        boolean result = memberService.checkMemberNameUnique(member);

        assertTrue(result);
    }

    @Test
    public void testCheckMemberNameUnique_NotUnique()
    {
        CoreMember member = createTestMember();
        CoreMember existing = createTestMember();
        existing.setId(2L);

        when(memberMapper.checkMemberNameUnique("testuser")).thenReturn(existing);

        boolean result = memberService.checkMemberNameUnique(member);

        assertFalse(result);
    }

    @Test
    public void testCheckMemberNameUnique_SameId()
    {
        CoreMember member = createTestMember();
        CoreMember existing = createTestMember();

        when(memberMapper.checkMemberNameUnique("testuser")).thenReturn(existing);

        boolean result = memberService.checkMemberNameUnique(member);

        assertTrue(result);
    }

    @Test
    public void testCheckPhoneUnique_Unique()
    {
        CoreMember member = createTestMember();

        when(memberMapper.checkPhoneUnique("13800138000")).thenReturn(null);

        boolean result = memberService.checkPhoneUnique(member);

        assertTrue(result);
    }

    @Test
    public void testCheckPhoneUnique_NotUnique()
    {
        CoreMember member = createTestMember();
        CoreMember existing = createTestMember();
        existing.setId(2L);

        when(memberMapper.checkPhoneUnique("13800138000")).thenReturn(existing);

        boolean result = memberService.checkPhoneUnique(member);

        assertFalse(result);
    }

    @Test
    public void testCheckEmailUnique_Unique()
    {
        CoreMember member = createTestMember();

        when(memberMapper.checkEmailUnique("test@example.com")).thenReturn(null);

        boolean result = memberService.checkEmailUnique(member);

        assertTrue(result);
    }

    @Test
    public void testCheckEmailUnique_NotUnique()
    {
        CoreMember member = createTestMember();
        CoreMember existing = createTestMember();
        existing.setId(2L);

        when(memberMapper.checkEmailUnique("test@example.com")).thenReturn(existing);

        boolean result = memberService.checkEmailUnique(member);

        assertFalse(result);
    }

    @Test
    public void testCheckNicknameUnique_Unique()
    {
        CoreMember member = createTestMember();

        when(memberMapper.checkNicknameUnique("Test User")).thenReturn(null);

        boolean result = memberService.checkNicknameUnique(member);

        assertTrue(result);
    }

    @Test
    public void testCheckNicknameUnique_NotUnique()
    {
        CoreMember member = createTestMember();
        CoreMember existing = createTestMember();
        existing.setId(2L);

        when(memberMapper.checkNicknameUnique("Test User")).thenReturn(existing);

        boolean result = memberService.checkNicknameUnique(member);

        assertFalse(result);
    }

    @Test
    public void testBlockMember()
    {
        when(memberMapper.blockMember(1L)).thenReturn(1);

        int result = memberService.blockMember(1L);

        assertEquals(1, result);
    }

    @Test
    public void testUnblockMember()
    {
        when(memberMapper.unblockMember(1L)).thenReturn(1);

        int result = memberService.unblockMember(1L);

        assertEquals(1, result);
    }

    @Test
    public void testCancelMember()
    {
        when(memberMapper.cancelMember(1L)).thenReturn(1);

        int result = memberService.cancelMember(1L);

        assertEquals(1, result);
    }

    @Test
    public void testUpdateLoginInfo()
    {
        when(memberMapper.updateLoginInfo(1L, "192.168.1.1")).thenReturn(1);

        int result = memberService.updateLoginInfo(1L, "192.168.1.1");

        assertEquals(1, result);
    }

    @Test
    public void testGetMemberOverview()
    {
        List<Map<String, Object>> rawData = new ArrayList<>();
        Map<String, Object> row1 = new HashMap<>();
        row1.put("date", "2024-01-01");
        row1.put("newUserCount", 10L);
        row1.put("activeUserCount", 50L);
        rawData.add(row1);

        when(memberMapper.selectMemberOverview(any(Map.class))).thenReturn(rawData);

        List<Map<String, Object>> result = memberService.getMemberOverview("2024-01-01", "2024-01-31");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("2024-01-01", result.get(0).get("date"));
        assertEquals(10L, result.get(0).get("newUserCount"));
    }

    @Test
    public void testGetMemberOverviewWithNullId()
    {
        CoreMember member = new CoreMember();
        member.setUsername("newuser");

        when(memberMapper.checkMemberNameUnique("newuser")).thenReturn(null);

        boolean result = memberService.checkMemberNameUnique(member);

        assertTrue(result);
    }

    @Test
    public void testConvertPackageToType_Normal()
    {
        CoreMember member = createTestMember();
        member.setCurrentPackage("普通会员");

        when(memberMapper.insertMember(any(CoreMember.class))).thenReturn(1);

        memberService.insertMember(member);

        assertEquals(1, member.getPackageType());
    }

    @Test
    public void testConvertPackageToType_VIP()
    {
        CoreMember member = createTestMember();
        member.setCurrentPackage("VIP会员");

        when(memberMapper.insertMember(any(CoreMember.class))).thenReturn(1);

        memberService.insertMember(member);

        assertEquals(2, member.getPackageType());
    }

    @Test
    public void testConvertPackageToType_SuperVIP()
    {
        CoreMember member = createTestMember();
        member.setCurrentPackage("超级VIP会员");

        when(memberMapper.insertMember(any(CoreMember.class))).thenReturn(1);

        memberService.insertMember(member);

        assertEquals(3, member.getPackageType());
    }

    @Test
    public void testConvertPackageToType_Other()
    {
        CoreMember member = createTestMember();
        member.setCurrentPackage("其他套餐");

        when(memberMapper.insertMember(any(CoreMember.class))).thenReturn(1);

        memberService.insertMember(member);

        assertNull(member.getPackageType());
    }

    @Test
    public void testConvertPackageToType_NullPackage()
    {
        CoreMember member = createTestMember();
        member.setCurrentPackage(null);

        when(memberMapper.insertMember(any(CoreMember.class))).thenReturn(1);

        memberService.insertMember(member);

        assertNull(member.getPackageType());
    }
}
