package com.opc.common.utils.sign;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Base64编解码工具类测试
 */
public class Base64Test {

    @Test
    @DisplayName("Base64编码-正常文本")
    public void testEncode_Normal() {
        String text = "Hello World";
        String encoded = Base64.encode(text.getBytes());
        assertNotNull(encoded);
        assertEquals("SGVsbG8gV29ybGQ=", encoded);
    }

    @Test
    @DisplayName("Base64编码-空字节数组")
    public void testEncode_Empty() {
        String encoded = Base64.encode(new byte[0]);
        assertEquals("", encoded);
    }

    @Test
    @DisplayName("Base64编码-null")
    public void testEncode_Null() {
        String encoded = Base64.encode(null);
        assertNull(encoded);
    }

    @Test
    @DisplayName("Base64编码-中文字符")
    public void testEncode_Chinese() {
        String text = "你好世界";
        String encoded = Base64.encode(text.getBytes());
        assertNotNull(encoded);
        // 中文字符编码后应该是有效的Base64
        assertTrue(encoded.matches("^[A-Za-z0-9+/]*={0,2}$"));
    }

    @Test
    @DisplayName("Base64编码-特殊字符")
    public void testEncode_SpecialChars() {
        String text = "!@#$%^&*()";
        String encoded = Base64.encode(text.getBytes());
        assertNotNull(encoded);
        assertTrue(encoded.length() > 0);
    }

    @Test
    @DisplayName("Base64编码-长文本")
    public void testEncode_LongText() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            sb.append("A");
        }
        String encoded = Base64.encode(sb.toString().getBytes());
        assertNotNull(encoded);
        assertTrue(encoded.length() > 0);
    }

    @Test
    @DisplayName("Base64解码-正常编码")
    public void testDecode_Normal() {
        String encoded = "SGVsbG8gV29ybGQ=";
        byte[] decoded = Base64.decode(encoded);
        assertNotNull(decoded);
        assertEquals("Hello World", new String(decoded));
    }

    @Test
    @DisplayName("Base64解码-空字符串")
    public void testDecode_Empty() {
        byte[] decoded = Base64.decode("");
        assertNotNull(decoded);
        assertEquals(0, decoded.length);
    }

    @Test
    @DisplayName("Base64解码-null")
    public void testDecode_Null() {
        byte[] decoded = Base64.decode(null);
        assertNull(decoded);
    }

    @Test
    @DisplayName("Base64解码-无效编码")
    public void testDecode_Invalid() {
        // 长度不是4的倍数
        byte[] decoded = Base64.decode("SGVsbG8");
        assertNull(decoded);
    }

    @Test
    @DisplayName("Base64解码-包含非法字符")
    public void testDecode_InvalidChars() {
        byte[] decoded = Base64.decode("SGVsbG8!V29ybGQ=");
        assertNull(decoded);
    }

    @Test
    @DisplayName("Base64编解码-往返测试")
    public void testEncodeDecode_RoundTrip() {
        String original = "Hello World! 你好世界 @#$%";
        String encoded = Base64.encode(original.getBytes());
        byte[] decoded = Base64.decode(encoded);
        assertEquals(original, new String(decoded));
    }

    @Test
    @DisplayName("Base64编码-不同长度")
    public void testEncode_DifferentLengths() {
        // 1字节 -> 2字符 + 2个填充
        assertEquals("QQ==", Base64.encode("A".getBytes()));
        
        // 2字节 -> 3字符 + 1个填充
        assertEquals("QUI=", Base64.encode("AB".getBytes()));
        
        // 3字节 -> 4字符
        assertEquals("QUJD", Base64.encode("ABC".getBytes()));
        
        // 4字节 -> 6字符 + 1个填充
        assertEquals("QUJDRA==", Base64.encode("ABCD".getBytes()));
    }

    @Test
    @DisplayName("Base64解码-不同填充")
    public void testDecode_DifferentPadding() {
        // 2个填充
        byte[] decoded1 = Base64.decode("QQ==");
        assertEquals("A", new String(decoded1));
        
        // 1个填充
        byte[] decoded2 = Base64.decode("QUI=");
        assertEquals("AB", new String(decoded2));
        
        // 无填充
        byte[] decoded3 = Base64.decode("QUJD");
        assertEquals("ABC", new String(decoded3));
    }

    @Test
    @DisplayName("Base64编码-二进制数据")
    public void testEncode_BinaryData() {
        byte[] binaryData = new byte[] {0x00, 0x01, 0x02, 0x03, (byte)0xFF};
        String encoded = Base64.encode(binaryData);
        assertNotNull(encoded);
        assertEquals("AAECA/8=", encoded);
    }

    @Test
    @DisplayName("Base64解码-二进制数据")
    public void testDecode_BinaryData() {
        String encoded = "AAECA/8=";
        byte[] decoded = Base64.decode(encoded);
        assertNotNull(decoded);
        assertArrayEquals(new byte[] {0x00, 0x01, 0x02, 0x03, (byte)0xFF}, decoded);
    }

    @Test
    @DisplayName("Base64解码-包含空白字符")
    public void testDecode_WithWhitespace() {
        // 包含空格
        byte[] decoded1 = Base64.decode("SGVs bG8g V29y bGQ=");
        assertEquals("Hello World", new String(decoded1));
        
        // 包含换行
        byte[] decoded2 = Base64.decode("SGVs\nbG8g\nV29y\nbGQ=");
        assertEquals("Hello World", new String(decoded2));
        
        // 包含制表符
        byte[] decoded3 = Base64.decode("SGVs\tbG8g\tV29y\tbGQ=");
        assertEquals("Hello World", new String(decoded3));
    }

    @Test
    @DisplayName("Base64编解码-空字节")
    public void testEncodeDecode_NullBytes() {
        byte[] original = new byte[] {0x00, 0x00, 0x00};
        String encoded = Base64.encode(original);
        assertEquals("AAAA", encoded);
        
        byte[] decoded = Base64.decode(encoded);
        assertArrayEquals(original, decoded);
    }

    @Test
    @DisplayName("Base64解码-无效的填充位置")
    public void testDecode_InvalidPadPosition() {
        // 填充符在中间
        byte[] decoded = Base64.decode("SGVs=G8g");
        assertNull(decoded);
    }

    @Test
    @DisplayName("Base64解码-过多的填充")
    public void testDecode_TooMuchPadding() {
        // 3个填充符
        byte[] decoded = Base64.decode("QQ===");
        assertNull(decoded);
    }
}
