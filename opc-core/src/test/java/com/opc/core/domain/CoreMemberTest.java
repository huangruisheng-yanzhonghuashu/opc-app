package com.opc.core.domain;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;

public class CoreMemberTest
{
    @Test
    public void testCoreMemberGettersAndSetters()
    {
        CoreMember member = new CoreMember();

        member.setId(1L);
        member.setUsername("testuser");
        member.setNickname("Test Nickname");
        member.setPhoneNumber("13800138000");
        member.setEmail("test@example.com");
        member.setAvatar("https://example.com/avatar.jpg");
        member.setLastActiveTime(Instant.parse("2024-01-01T10:00:00Z"));
        member.setCurrentPackage("VIP");
        member.setCurrentPackageLevel(3);
        member.setSource("email");
        member.setSourceId("SRC001");
        member.setToken("token123");
        member.setStatus("0");
        member.setRegisterTime(Instant.parse("2024-01-01T00:00:00Z"));

        assertEquals(1L, member.getId());
        assertEquals("testuser", member.getUsername());
        assertEquals("Test Nickname", member.getNickname());
        assertEquals("13800138000", member.getPhoneNumber());
        assertEquals("test@example.com", member.getEmail());
        assertEquals("https://example.com/avatar.jpg", member.getAvatar());
        assertEquals(Instant.parse("2024-01-01T10:00:00Z"), member.getLastActiveTime());
        assertEquals("VIP", member.getCurrentPackage());
        assertEquals(Integer.valueOf(3), member.getCurrentPackageLevel());
        assertEquals("email", member.getSource());
        assertEquals("SRC001", member.getSourceId());
        assertEquals("token123", member.getToken());
        assertEquals("0", member.getStatus());
        assertEquals(Instant.parse("2024-01-01T00:00:00Z"), member.getRegisterTime());
    }

    @Test
    public void testCoreMemberToString()
    {
        CoreMember member = new CoreMember();
        member.setId(1L);
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

        assertNull(member.getId());
        assertNull(member.getUsername());
        assertNull(member.getNickname());
        assertNull(member.getPhoneNumber());
        assertNull(member.getEmail());
        assertNull(member.getAvatar());
        assertNull(member.getCurrentPackageLevel());
        assertNull(member.getStatus());
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
    public void testCoreMemberSourceValues()
    {
        CoreMember member = new CoreMember();

        member.setSource("email");
        assertEquals("email", member.getSource());

        member.setSource("x");
        assertEquals("x", member.getSource());

        member.setSource("facebook");
        assertEquals("facebook", member.getSource());

        member.setSource("apple");
        assertEquals("apple", member.getSource());

        member.setSource("google");
        assertEquals("google", member.getSource());
    }

    @Test
    public void testCoreMemberPackageLevel()
    {
        CoreMember member = new CoreMember();

        member.setCurrentPackageLevel(1);
        assertEquals(Integer.valueOf(1), member.getCurrentPackageLevel());

        member.setCurrentPackageLevel(2);
        assertEquals(Integer.valueOf(2), member.getCurrentPackageLevel());

        member.setCurrentPackageLevel(3);
        assertEquals(Integer.valueOf(3), member.getCurrentPackageLevel());
    }
}
