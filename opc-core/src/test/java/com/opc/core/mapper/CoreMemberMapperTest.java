package com.opc.core.mapper;

import com.opc.core.domain.CoreMember;
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
public class CoreMemberMapperTest
{
    @Mock
    private CoreMemberMapper memberMapper;

    @Test
    public void testSelectMemberList()
    {
        CoreMember member = new CoreMember();
        member.setUsername("testuser");

        CoreMember resultMember = new CoreMember();
        resultMember.setMemberId(1L);
        resultMember.setUsername("testuser");

        when(memberMapper.selectMemberList(any(CoreMember.class))).thenReturn(Arrays.asList(resultMember));

        List<CoreMember> list = memberMapper.selectMemberList(member);

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals("testuser", list.get(0).getUsername());
        verify(memberMapper, times(1)).selectMemberList(member);
    }

    @Test
    public void testSelectMemberById()
    {
        CoreMember member = new CoreMember();
        member.setMemberId(1L);
        member.setUsername("testuser");

        when(memberMapper.selectMemberById(1L)).thenReturn(member);

        CoreMember result = memberMapper.selectMemberById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getMemberId());
        assertEquals("testuser", result.getUsername());
        verify(memberMapper, times(1)).selectMemberById(1L);
    }

    @Test
    public void testInsertMember()
    {
        CoreMember member = new CoreMember();
        member.setUsername("newuser");
        member.setEmail("new@example.com");

        when(memberMapper.insertMember(any(CoreMember.class))).thenReturn(1);

        int result = memberMapper.insertMember(member);

        assertEquals(1, result);
        verify(memberMapper, times(1)).insertMember(member);
    }

    @Test
    public void testUpdateMember()
    {
        CoreMember member = new CoreMember();
        member.setMemberId(1L);
        member.setNickname("Updated Nickname");

        when(memberMapper.updateMember(any(CoreMember.class))).thenReturn(1);

        int result = memberMapper.updateMember(member);

        assertEquals(1, result);
        verify(memberMapper, times(1)).updateMember(member);
    }

    @Test
    public void testDeleteMemberById()
    {
        when(memberMapper.deleteMemberById(1L)).thenReturn(1);

        int result = memberMapper.deleteMemberById(1L);

        assertEquals(1, result);
        verify(memberMapper, times(1)).deleteMemberById(1L);
    }

    @Test
    public void testDeleteMemberByIds()
    {
        Long[] ids = {1L, 2L, 3L};
        when(memberMapper.deleteMemberByIds(ids)).thenReturn(3);

        int result = memberMapper.deleteMemberByIds(ids);

        assertEquals(3, result);
        verify(memberMapper, times(1)).deleteMemberByIds(ids);
    }

    @Test
    public void testCheckMemberNameUnique()
    {
        CoreMember member = new CoreMember();
        member.setMemberId(1L);
        member.setUsername("existinguser");

        when(memberMapper.checkMemberNameUnique("existinguser")).thenReturn(member);

        CoreMember result = memberMapper.checkMemberNameUnique("existinguser");

        assertNotNull(result);
        assertEquals("existinguser", result.getUsername());
        verify(memberMapper, times(1)).checkMemberNameUnique("existinguser");
    }

    @Test
    public void testCheckPhoneUnique()
    {
        CoreMember member = new CoreMember();
        member.setMemberId(1L);
        member.setPhoneNumber("13800138000");

        when(memberMapper.checkPhoneUnique("13800138000")).thenReturn(member);

        CoreMember result = memberMapper.checkPhoneUnique("13800138000");

        assertNotNull(result);
        assertEquals("13800138000", result.getPhoneNumber());
        verify(memberMapper, times(1)).checkPhoneUnique("13800138000");
    }

    @Test
    public void testCheckEmailUnique()
    {
        CoreMember member = new CoreMember();
        member.setMemberId(1L);
        member.setEmail("test@example.com");

        when(memberMapper.checkEmailUnique("test@example.com")).thenReturn(member);

        CoreMember result = memberMapper.checkEmailUnique("test@example.com");

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(memberMapper, times(1)).checkEmailUnique("test@example.com");
    }
}
