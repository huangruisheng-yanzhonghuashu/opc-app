package com.opc.common.utils.html;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EscapeUtilTest {

    @Test
    public void testEscape() {
        String text = "<script>alert(1);</script>";
        String escaped = EscapeUtil.escape(text);
        assertNotNull(escaped);
        assertFalse(escaped.contains("<"));
        assertTrue(escaped.startsWith("%"));
    }

    @Test
    public void testEscapeEmpty() {
        assertEquals("", EscapeUtil.escape(""));
        assertEquals("", EscapeUtil.escape(null));
    }

    @Test
    public void testUnescape() {
        String escaped = "%3c%73%63%72%69%70%74%3e";
        String unescaped = EscapeUtil.unescape(escaped);
        assertEquals("<script>", unescaped);
    }

    @Test
    public void testUnescapeWithUnicode() {
        String escaped = "%u4e2d%u6587";
        String unescaped = EscapeUtil.unescape(escaped);
        assertEquals("中文", unescaped);
    }

    @Test
    public void testUnescapeEmpty() {
        assertEquals("", EscapeUtil.unescape(""));
        assertNull(EscapeUtil.unescape(null));
    }

    @Test
    public void testClean() {
        String html = "<script>alert(1);</script>";
        String cleaned = EscapeUtil.clean(html);
        assertNotNull(cleaned);
        assertFalse(cleaned.contains("<script>"));
    }

    @Test
    public void testCleanWithText() {
        String html = "<p>Hello <b>World</b></p>";
        String cleaned = EscapeUtil.clean(html);
        assertNotNull(cleaned);
    }

    @Test
    public void testEscapeAndUnescape() {
        String original = "Hello World!";
        String escaped = EscapeUtil.escape(original);
        String unescaped = EscapeUtil.unescape(escaped);
        assertEquals(original, unescaped);
    }

    @Test
    public void testEscapeSpecialChars() {
        String text = "Test & Test < Test > Test \" Test ' Test";
        String escaped = EscapeUtil.escape(text);
        assertNotNull(escaped);
        assertFalse(escaped.contains("&"));
        assertFalse(escaped.contains("<"));
        assertFalse(escaped.contains(">"));
    }

    @Test
    public void testUnescapePlainText() {
        String text = "Hello World";
        String result = EscapeUtil.unescape(text);
        assertEquals(text, result);
    }
}
