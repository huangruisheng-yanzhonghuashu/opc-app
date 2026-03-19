package com.opc.core.service.impl;

import com.opc.core.domain.CoreMember;
import com.opc.core.mapper.CoreMemberMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CoreMemberServiceImplTest
{
    @Mock
    private CoreMemberMapper memberMapper;

    @InjectMocks
    private CoreMemberServiceImpl memberService;

    @Test
    public void testSelectMemberList()
    {
        CoreMember member = new CoreMember();
        member.setUsername("testuser");

        CoreMember resultMember = new CoreMember();
        resultMember.setId(1L);
        resultMember.setUsername("testuser");

        when(memberMapper.selectMemberList(any(CoreMember.class))).thenReturn(Arrays.asList(resultMember));

        List<CoreMember> list = memberService.selectMemberList(member);

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals("testuser", list.get(0).getUsername());
    }

    @Test
    public void testSelectMemberById()
    {
        CoreMember member = new CoreMember();
        member.setId(1L);
        member.setUsername("testuser");

        when(memberMapper.selectMemberById(1L)).thenReturn(member);

        CoreMember result = memberService.selectMemberById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("testuser", result.getUsername());
    }

    @Test
    public void testInsertMember()
    {
        CoreMember member = new CoreMember();
        member.setUsername("newuser");
        member.setEmail("new@example.com");

        when(memberMapper.insertMember(any(CoreMember.class))).thenReturn(1);

        int result = memberService.insertMember(member);

        assertEquals(1, result);
    }

    @Test
    public void testUpdateMember()
    {
        CoreMember member = new CoreMember();
        member.setId(1L);
        member.setNickname("Updated Nickname");

        when(memberMapper.updateMember(any(CoreMember.class))).thenReturn(1);

        int result = memberService.updateMember(member);

        assertEquals(1, result);
    }

    @Test
    public void testCheckMemberNameUnique_WhenNameExists()
    {
        CoreMember existingMember = new CoreMember();
        existingMember.setId(1L);
        existingMember.setUsername("existinguser");

        CoreMember newMember = new CoreMember();
        newMember.setId(2L);
        newMember.setUsername("existinguser");

        when(memberMapper.checkMemberNameUnique("existinguser")).thenReturn(existingMember);

        boolean result = memberService.checkMemberNameUnique(newMember);

        assertFalse(result);
    }

    @Test
    public void testCheckMemberNameUnique_WhenNameNotExists()
    {
        CoreMember newMember = new CoreMember();
        newMember.setId(2L);
        newMember.setUsername("newuser");

        when(memberMapper.checkMemberNameUnique("newuser")).thenReturn(null);

        boolean result = memberService.checkMemberNameUnique(newMember);

        assertTrue(result);
    }

    @Test
    public void testCheckMemberNameUnique_SameId()
    {
        CoreMember member = new CoreMember();
        member.setId(1L);
        member.setUsername("testuser");

        when(memberMapper.checkMemberNameUnique("testuser")).thenReturn(member);

        boolean result = memberService.checkMemberNameUnique(member);

        assertTrue(result);
    }

    @Test
    public void testCheckPhoneUnique_WhenPhoneExists()
    {
        CoreMember existingMember = new CoreMember();
        existingMember.setId(1L);
        existingMember.setPhoneNumber("13800138000");

        CoreMember newMember = new CoreMember();
        newMember.setId(2L);
        newMember.setPhoneNumber("13800138000");

        when(memberMapper.checkPhoneUnique("13800138000")).thenReturn(existingMember);

        boolean result = memberService.checkPhoneUnique(newMember);

        assertFalse(result);
    }

    @Test
    public void testCheckPhoneUnique_WhenPhoneNotExists()
    {
        CoreMember newMember = new CoreMember();
        newMember.setId(2L);
        newMember.setPhoneNumber("13900139000");

        when(memberMapper.checkPhoneUnique("13900139000")).thenReturn(null);

        boolean result = memberService.checkPhoneUnique(newMember);

        assertTrue(result);
    }

    @Test
    public void testCheckEmailUnique_WhenEmailExists()
    {
        CoreMember existingMember = new CoreMember();
        existingMember.setId(1L);
        existingMember.setEmail("test@example.com");

        CoreMember newMember = new CoreMember();
        newMember.setId(2L);
        newMember.setEmail("test@example.com");

        when(memberMapper.checkEmailUnique("test@example.com")).thenReturn(existingMember);

        boolean result = memberService.checkEmailUnique(newMember);

        assertFalse(result);
    }

    @Test
    public void testCheckEmailUnique_WhenEmailNotExists()
    {
        CoreMember newMember = new CoreMember();
        newMember.setId(2L);
        newMember.setEmail("new@example.com");

        when(memberMapper.checkEmailUnique("new@example.com")).thenReturn(null);

        boolean result = memberService.checkEmailUnique(newMember);

        assertTrue(result);
    }
}
