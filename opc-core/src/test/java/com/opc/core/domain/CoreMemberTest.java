package com.opc.core.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CoreMemberTest
{
    @Test
    public void testCoreMemberGettersAndSetters()
    {
        CoreMember member = new CoreMember();

        member.setMemberId(1L);
        member.setPhoneNumber("13800138000");
        member.setEmail("test@example.com");
        member.setUsername("testuser");
        member.setNickname("Test Nickname");
        member.setLastActiveTime("2024-01-01 10:00:00");
        member.setCurrentPackage("VIP");
        member.setLevel1Users(10);
        member.setLevel2Users(20);
        member.setLevel3Users(30);
        member.setSource("Website");
        member.setSourceId("SRC001");
        member.setStatus("0");
        member.setRegisterTime("2024-01-01 00:00:00");

        assertEquals(1L, member.getMemberId());
        assertEquals("13800138000", member.getPhoneNumber());
        assertEquals("test@example.com", member.getEmail());
        assertEquals("testuser", member.getUsername());
        assertEquals("Test Nickname", member.getNickname());
        assertEquals("2024-01-01 10:00:00", member.getLastActiveTime());
        assertEquals("VIP", member.getCurrentPackage());
        assertEquals(Integer.valueOf(10), member.getLevel1Users());
        assertEquals(Integer.valueOf(20), member.getLevel2Users());
        assertEquals(Integer.valueOf(30), member.getLevel3Users());
        assertEquals("Website", member.getSource());
        assertEquals("SRC001", member.getSourceId());
        assertEquals("0", member.getStatus());
        assertEquals("2024-01-01 00:00:00", member.getRegisterTime());
    }

    @Test
    public void testCoreMemberToString()
    {
        CoreMember member = new CoreMember();
        member.setMemberId(1L);
        member.setUsername("testuser");
        member.setNickname("Test Nickname");
        member.setStatus("0");

        String str = member.toString();
        assertNotNull(str);
        assertTrue(str.contains("testuser"));
        assertTrue(str.contains("Test Nickname"));
        assertTrue(str.contains("0"));
    }

    @Test
    public void testCoreMemberDefaultValues()
    {
        CoreMember member = new CoreMember();

        assertNull(member.getMemberId());
        assertNull(member.getPhoneNumber());
        assertNull(member.getEmail());
        assertNull(member.getUsername());
        assertNull(member.getNickname());
        assertNull(member.getStatus());
        assertNull(member.getLevel1Users());
        assertNull(member.getLevel2Users());
        assertNull(member.getLevel3Users());
    }

    @Test
    public void testCoreMemberStatusValues()
    {
        CoreMember member = new CoreMember();

        member.setStatus("0");
        assertEquals("0", member.getStatus());

        member.setStatus("1");
        assertEquals("1", member.getStatus());
    }

    @Test
    public void testCoreMemberUserLevels()
    {
        CoreMember member = new CoreMember();

        member.setLevel1Users(5);
        member.setLevel2Users(10);
        member.setLevel3Users(15);

        assertEquals(5, member.getLevel1Users());
        assertEquals(10, member.getLevel2Users());
        assertEquals(15, member.getLevel3Users());
    }
}
