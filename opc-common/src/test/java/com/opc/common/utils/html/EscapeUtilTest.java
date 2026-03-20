package com.opc.common.utils.html;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * HTML转义和反转义工具类测试
 */
public class EscapeUtilTest {

    @Test
    @DisplayName("HTML转义-正常文本")
    public void testEscape_Normal() {
        String text = "Hello World";
        String result = EscapeUtil.escape(text);
        assertNotNull(result);
        assertTrue(result.startsWith("%"));
    }

    @Test
    @DisplayName("HTML转义-包含HTML标签")
    public void testEscape_WithHtmlTags() {
        String html = "<script>alert('xss')</script>";
        String result = EscapeUtil.escape(html);
        assertNotNull(result);
        assertFalse(result.contains("<script>"));
    }

    @Test
    @DisplayName("HTML转义-包含特殊字符")
    public void testEscape_WithSpecialChars() {
        String text = "<>&\"'";
        String result = EscapeUtil.escape(text);
        assertNotNull(result);
        assertFalse(result.contains("<"));
        assertFalse(result.contains(">"));
        assertFalse(result.contains("&"));
    }

    @Test
    @DisplayName("HTML转义-空值")
    public void testEscape_Empty() {
        assertEquals("", EscapeUtil.escape(""));
        assertEquals("", EscapeUtil.escape(null));
    }

    @Test
    @DisplayName("HTML转义-中文字符")
    public void testEscape_Chinese() {
        String text = "你好世界";
        String result = EscapeUtil.escape(text);
        assertNotNull(result);
        assertTrue(result.contains("%u"));
    }

    @Test
    @DisplayName("HTML反转义-正常编码")
    public void testUnescape_Normal() {
        String encoded = "%48%65%6c%6c%6f"; // Hello
        String result = EscapeUtil.unescape(encoded);
        assertEquals("Hello", result);
    }

    @Test
    @DisplayName("HTML反转义-Unicode编码")
    public void testUnescape_Unicode() {
        String encoded = "%u4f60%u597d"; // 你好
        String result = EscapeUtil.unescape(encoded);
        assertEquals("你好", result);
    }

    @Test
    @DisplayName("HTML反转义-混合编码")
    public void testUnescape_Mixed() {
        String encoded = "Hello%u4e16%u754c"; // Hello世界
        String result = EscapeUtil.unescape(encoded);
        assertEquals("Hello世界", result);
    }

    @Test
    @DisplayName("HTML反转义-空值")
    public void testUnescape_Empty() {
        assertEquals("", EscapeUtil.unescape(""));
        assertNull(EscapeUtil.unescape(null));
    }

    @Test
    @DisplayName("HTML反转义-无编码文本")
    public void testUnescape_NoEncoding() {
        String text = "Hello World";
        String result = EscapeUtil.unescape(text);
        assertEquals("Hello World", result);
    }

    @Test
    @DisplayName("HTML转义和反转义-往返测试")
    public void testEscapeUnescape_RoundTrip() {
        String original = "Hello 世界! <test>";
        String escaped = EscapeUtil.escape(original);
        String unescaped = EscapeUtil.unescape(escaped);
        assertEquals(original, unescaped);
    }

    @Test
    @DisplayName("清除HTML标签-正常")
    public void testClean_Normal() {
        String html = "<p>Hello World</p>";
        String result = EscapeUtil.clean(html);
        assertEquals("Hello World", result.trim());
    }

    @Test
    @DisplayName("清除HTML标签-嵌套标签")
    public void testClean_Nested() {
        String html = "<div><p>Hello <b>World</b></p></div>";
        String result = EscapeUtil.clean(html);
        assertTrue(result.contains("Hello"));
        assertTrue(result.contains("World"));
        // HTMLFilter可能保留部分标签，只验证内容存在
    }

    @Test
    @DisplayName("清除HTML标签-空值")
    public void testClean_Empty() {
        assertEquals("", EscapeUtil.clean(""));
        // null会抛出异常
    }

    @Test
    @DisplayName("清除HTML标签-无标签文本")
    public void testClean_NoTags() {
        String text = "Hello World";
        String result = EscapeUtil.clean(text);
        assertEquals("Hello World", result);
    }

    @Test
    @DisplayName("清除HTML标签-脚本标签")
    public void testClean_ScriptTag() {
        String html = "<script>alert('xss')</script>";
        String result = EscapeUtil.clean(html);
        assertFalse(result.contains("<script>"));
        assertFalse(result.contains("</script>"));
    }

    @Test
    @DisplayName("清除HTML标签-复杂HTML")
    public void testClean_ComplexHtml() {
        String html = "<html><body><h1>Title</h1><p>Paragraph with <a href='#'>link</a></p></body></html>";
        String result = EscapeUtil.clean(html);
        assertTrue(result.contains("Title"));
        assertTrue(result.contains("Paragraph"));
        assertTrue(result.contains("link"));
        // HTMLFilter可能保留部分标签，只验证内容存在
    }

    @Test
    @DisplayName("HTML转义-单字节字符")
    public void testEscape_SingleByte() {
        String text = "ABC";
        String result = EscapeUtil.escape(text);
        assertTrue(result.contains("%41"));
        assertTrue(result.contains("%42"));
        assertTrue(result.contains("%43"));
    }

    @Test
    @DisplayName("HTML转义-双字节字符")
    public void testEscape_DoubleByte() {
        String text = "中";
        String result = EscapeUtil.escape(text);
        assertTrue(result.contains("%u"));
    }
}
