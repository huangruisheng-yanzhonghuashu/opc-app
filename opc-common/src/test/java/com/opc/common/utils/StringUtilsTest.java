package com.opc.common.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StringUtilsTest
{
    @Test
    public void testIsEmpty()
    {
        assertTrue(com.opc.common.utils.StringUtils.isEmpty((String) null));
        assertTrue(com.opc.common.utils.StringUtils.isEmpty(""));
        assertFalse(com.opc.common.utils.StringUtils.isEmpty(" "));
        assertFalse(com.opc.common.utils.StringUtils.isEmpty("abc"));
    }

    @Test
    public void testIsNotEmpty()
    {
        assertFalse(com.opc.common.utils.StringUtils.isNotEmpty((String) null));
        assertFalse(com.opc.common.utils.StringUtils.isNotEmpty(""));
        assertTrue(com.opc.common.utils.StringUtils.isNotEmpty(" "));
        assertTrue(com.opc.common.utils.StringUtils.isNotEmpty("abc"));
    }

    @Test
    public void testIsBlank()
    {
        assertTrue(com.opc.common.utils.StringUtils.isBlank(null));
        assertTrue(com.opc.common.utils.StringUtils.isBlank(""));
        assertTrue(com.opc.common.utils.StringUtils.isBlank("   "));
        assertFalse(com.opc.common.utils.StringUtils.isBlank("abc"));
    }

    @Test
    public void testIsNotBlank()
    {
        assertFalse(com.opc.common.utils.StringUtils.isNotBlank(null));
        assertFalse(com.opc.common.utils.StringUtils.isNotBlank(""));
        assertFalse(com.opc.common.utils.StringUtils.isNotBlank("   "));
        assertTrue(com.opc.common.utils.StringUtils.isNotBlank("abc"));
    }

    @Test
    public void testTrim()
    {
        assertNull(com.opc.common.utils.StringUtils.trim(null));
        assertEquals("", com.opc.common.utils.StringUtils.trim(""));
        assertEquals("", com.opc.common.utils.StringUtils.trim("   "));
        assertEquals("abc", com.opc.common.utils.StringUtils.trim("abc"));
        assertEquals("abc", com.opc.common.utils.StringUtils.trim("  abc  "));
    }

    @Test
    public void testSubstring()
    {
        assertNull(com.opc.common.utils.StringUtils.substring(null, 0));
        assertEquals("", com.opc.common.utils.StringUtils.substring("", 0));
        assertEquals("abc", com.opc.common.utils.StringUtils.substring("abcdef", 0));
        assertEquals("def", com.opc.common.utils.StringUtils.substring("abcdef", 3));
        assertEquals("abc", com.opc.common.utils.StringUtils.substring("abcdef", 0, 3));
    }

    @Test
    public void testEquals()
    {
        assertTrue(com.opc.common.utils.StringUtils.equals(null, null));
        assertTrue(com.opc.common.utils.StringUtils.equals("abc", "abc"));
        assertFalse(com.opc.common.utils.StringUtils.equals(null, "abc"));
        assertFalse(com.opc.common.utils.StringUtils.equals("abc", null));
        assertFalse(com.opc.common.utils.StringUtils.equals("ABC", "abc"));
    }

    @Test
    public void testStartsWith()
    {
        assertTrue(com.opc.common.utils.StringUtils.startsWith("abcdef", "abc"));
        assertTrue(com.opc.common.utils.StringUtils.startsWith("abcdef", null));
        assertFalse(com.opc.common.utils.StringUtils.startsWith(null, "abc"));
        assertFalse(com.opc.common.utils.StringUtils.startsWith("abcdef", "xyz"));
    }

    @Test
    public void testEndsWith()
    {
        assertTrue(com.opc.common.utils.StringUtils.endsWith("abcdef", "def"));
        assertTrue(com.opc.common.utils.StringUtils.endsWith("abcdef", null));
        assertFalse(com.opc.common.utils.StringUtils.endsWith(null, "def"));
        assertFalse(com.opc.common.utils.StringUtils.endsWith("abcdef", "xyz"));
    }

    @Test
    public void testContains()
    {
        assertTrue(com.opc.common.utils.StringUtils.contains("abcdef", "cde"));
        assertFalse(com.opc.common.utils.StringUtils.contains("abcdef", "xyz"));
        assertFalse(com.opc.common.utils.StringUtils.contains(null, "abc"));
        assertFalse(com.opc.common.utils.StringUtils.contains("abc", null));
    }
}
