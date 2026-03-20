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
        assertEquals("d41d8