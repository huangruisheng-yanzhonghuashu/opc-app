package com.opc.common.utils.sign;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * MD5加密工具类测试
 */
public class Md5UtilsTest {

    @Test
    @DisplayName("MD5哈希-正常文本")
    public void testHash_Normal() {
        String text = "Hello World";
        String hash = Md5Utils.hash(text);
        assertNotNull(hash);
        assertEquals(32, hash.length());
        // 验证是十六进制字符串
        assertTrue(hash.matches("^[0-9a-f]{32}$"));
    }

    @Test
    @DisplayName("MD5哈希-空字符串")
    public void testHash_Empty() {
        String hash = Md5Utils.hash("");
        assertNotNull(hash);
        assertEquals(32, hash.length());
        // 空字符串的MD5值
        assertEquals("d41d8cd98f00b204e9800998ecf8427e", hash);
    }

    @Test
    @DisplayName("MD5哈希-null")
    public void testHash_Null() {
        // null会导致异常，方法返回null
        String hash = Md5Utils.hash(null);
        // 实际返回null，因为内部捕获了异常
        assertNull(hash);
    }

    @Test
    @DisplayName("MD5哈希-中文字符")
    public void testHash_Chinese() {
        String text = "你好世界";
        String hash = Md5Utils.hash(text);
        assertNotNull(hash);
        assertEquals(32, hash.length());
        assertTrue(hash.matches("^[0-9a-f]{32}$"));
    }

    @Test
    @DisplayName("MD5哈希-特殊字符")
    public void testHash_SpecialChars() {
        String text = "!@#$%^&*()_+-=[]{}|;':\",./<>?";
        String hash = Md5Utils.hash(text);
        assertNotNull(hash);
        assertEquals(32, hash.length());
    }

    @Test
    @DisplayName("MD5哈希-长文本")
    public void testHash_LongText() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            sb.append("A");
        }
        String hash = Md5Utils.hash(sb.toString());
        assertNotNull(hash);
        assertEquals(32, hash.length());
    }

    @Test
    @DisplayName("MD5哈希-确定性")
    public void testHash_Deterministic() {
        String text = "Test String";
        String hash1 = Md5Utils.hash(text);
        String hash2 = Md5Utils.hash(text);
        assertEquals(hash1, hash2);
    }

    @Test
    @DisplayName("MD5哈希-不同输入不同输出")
    public void testHash_DifferentInput() {
        String hash1 = Md5Utils.hash("Hello");
        String hash2 = Md5Utils.hash("World");
        assertNotEquals(hash1, hash2);
    }

    @Test
    @DisplayName("MD5哈希-大小写敏感")
    public void testHash_CaseSensitive() {
        String hash1 = Md5Utils.hash("Hello");
        String hash2 = Md5Utils.hash("hello");
        assertNotEquals(hash1, hash2);
    }

    @Test
    @DisplayName("MD5哈希-数字")
    public void testHash_Numbers() {
        String hash = Md5Utils.hash("123456");
        assertNotNull(hash);
        assertEquals(32, hash.length());
        // 123456的MD5值
        assertEquals("e10adc3949ba59abbe56e057f20f883e", hash);
    }

    @Test
    @DisplayName("MD5哈希-已知值验证")
    public void testHash_KnownValues() {
        // 验证一些已知的MD5值
        assertEquals("5d41402abc4b2a76b9719d911017c592", Md5Utils.hash("hello"));
        assertEquals("202cb962ac59075b964b07152d234b70", Md5Utils.hash("123"));
        assertEquals("098f6bcd4621d373cade4e832627b4f6", Md5Utils.hash("test"));
    }

    @Test
    @DisplayName("MD5哈希-包含空格")
    public void testHash_WithSpaces() {
        String hash1 = Md5Utils.hash("Hello World");
        String hash2 = Md5Utils.hash("HelloWorld");
        assertNotEquals(hash1, hash2);
        
        String hash3 = Md5Utils.hash(" Hello World ");
        assertNotEquals(hash1, hash3);
    }

    @Test
    @DisplayName("MD5哈希-包含换行")
    public void testHash_WithNewline() {
        String hash1 = Md5Utils.hash("Hello\nWorld");
        String hash2 = Md5Utils.hash("HelloWorld");
        assertNotEquals(hash1, hash2);
    }

    @Test
    @DisplayName("MD5哈希-Unicode字符")
    public void testHash_Unicode() {
        String text = "Hello 世界 🌍";
        String hash = Md5Utils.hash(text);
        assertNotNull(hash);
        assertEquals(32, hash.length());
    }

    @Test
    @DisplayName("MD5哈希-重复调用一致性")
    public void testHash_Consistency() {
        String text = "Consistency Test";
        String expectedHash = Md5Utils.hash(text);
        for (int i = 0; i < 100; i++) {
            String hash = Md5Utils.hash(text);
            assertEquals(expectedHash, hash);
        }
    }
}
