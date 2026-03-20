package com.opc.common.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 脱敏工具类测试
 */
public class DesensitizedUtilTest {

    @Test
    @DisplayName("密码脱敏-正常密码")
    public void testPassword_Normal() {
        String password = "123456";
        String result = DesensitizedUtil.password(password);
        assertEquals("******", result);
    }

    @Test
    @DisplayName("密码脱敏-空密码")
    public void testPassword_Empty() {
        assertEquals("", DesensitizedUtil.password(""));
        assertEquals("", DesensitizedUtil.password(null));
        assertEquals("", DesensitizedUtil.password("   "));
    }

    @Test
    @DisplayName("密码脱敏-不同长度")
    public void testPassword_DifferentLength() {
        assertEquals("*", DesensitizedUtil.password("1"));
        assertEquals("**", DesensitizedUtil.password("12"));
        assertEquals("********", DesensitizedUtil.password("12345678"));
        assertEquals("****************", DesensitizedUtil.password("1234567890123456"));
    }

    @Test
    @DisplayName("车牌脱敏-普通车牌(7位)")
    public void testCarLicense_Normal() {
        String carLicense = "京A12345";
        String result = DesensitizedUtil.carLicense(carLicense);
        // 实际行为：从第3位开始到第6位（不包含）替换为*
        assertEquals("京A1***5", result);
    }

    @Test
    @DisplayName("车牌脱敏-新能源车牌(8位)")
    public void testCarLicense_NewEnergy() {
        String carLicense = "京A123456";
        String result = DesensitizedUtil.carLicense(carLicense);
        // 实际行为：从第3位开始到第7位（不包含）替换为*
        assertEquals("京A1****6", result);
    }

    @Test
    @DisplayName("车牌脱敏-空值")
    public void testCarLicense_Empty() {
        assertEquals("", DesensitizedUtil.carLicense(""));
        assertEquals("", DesensitizedUtil.carLicense(null));
        assertEquals("", DesensitizedUtil.carLicense("   "));
    }

    @Test
    @DisplayName("车牌脱敏-无效长度")
    public void testCarLicense_InvalidLength() {
        // 小于7位
        assertEquals("京A1", DesensitizedUtil.carLicense("京A1"));
        // 大于8位
        assertEquals("京A1234567", DesensitizedUtil.carLicense("京A1234567"));
    }

    @Test
    @DisplayName("车牌脱敏-特殊字符车牌")
    public void testCarLicense_SpecialChars() {
        String carLicense = "沪B·88888";
        String result = DesensitizedUtil.carLicense(carLicense);
        // 包含特殊字符后长度为9，按8位处理（3-7位替换）
        assertEquals("沪B·****8", result);
    }
}
