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
        // null会导致空指针异常，但方法内部捕获了异常
        String hash = Md5Utils.hash(null);
        assertNull(hash);
    }

    @Test
    @DisplayName("MD5哈希-中文字符")
    public void testHash_Chinese() {
        String text = "你好世界";
        String hash = Md5Utils.hash(text);
        assertNotNull(hash);
        assertEquals(32, hash.length());
        assertTrue(hash.matches("^[0-9