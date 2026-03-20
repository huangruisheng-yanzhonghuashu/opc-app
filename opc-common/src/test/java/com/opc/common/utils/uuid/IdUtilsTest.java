package com.opc.common.utils.uuid;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ID生成器工具类测试
 */
public class IdUtilsTest {

    @Test
    @DisplayName("生成随机UUID-标准格式")
    public void testRandomUUID() {
        String uuid = IdUtils.randomUUID();
        assertNotNull(uuid);
        assertEquals(36, uuid.length());
        assertTrue(uuid.contains("-"));
        // 验证UUID格式
        assertTrue(uuid.matches("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$"));
    }

    @Test
    @DisplayName("生成随机UUID-唯一性")
    public void testRandomUUID_Unique() {
        String uuid1 = IdUtils.randomUUID();
        String uuid2 = IdUtils.randomUUID();
        assertNotEquals(uuid1, uuid2);
    }

    @Test
    @DisplayName("生成简化UUID-无横线")
    public void testSimpleUUID() {
        String uuid = IdUtils.simpleUUID();
        assertNotNull(uuid);
        assertEquals(32, uuid.length());
        assertFalse(uuid.contains("-"));
        // 验证只包含十六进制字符
        assertTrue(uuid.matches("^[0-9a-f]{32}$"));
    }

    @Test
    @DisplayName("生成简化UUID-唯一性")
    public void testSimpleUUID_Unique() {
        String uuid1 = IdUtils.simpleUUID();
        String uuid2 = IdUtils.simpleUUID();
        assertNotEquals(uuid1, uuid2);
    }

    @Test
    @DisplayName("生成快速UUID-标准格式")
    public void testFastUUID() {
        String uuid = IdUtils.fastUUID();
        assertNotNull(uuid);
        assertEquals(36, uuid.length());
        assertTrue(uuid.contains("-"));
        // 验证UUID格式
        assertTrue(uuid.matches("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$"));
    }

    @Test
    @DisplayName("生成快速UUID-唯一性")
    public void testFastUUID_Unique() {
        String uuid1 = IdUtils.fastUUID();
        String uuid2 = IdUtils.fastUUID();
        assertNotEquals(uuid1, uuid2);
    }

    @Test
    @DisplayName("生成快速简化UUID")
    public void testFastSimpleUUID() {
        String uuid = IdUtils.fastSimpleUUID();
        assertNotNull(uuid);
        assertEquals(32, uuid.length());
        assertFalse(uuid.contains("-"));
        // 验证只包含十六进制字符
        assertTrue(uuid.matches("^[0-9a-f]{32}$"));
    }

    @Test
    @DisplayName("生成快速简化UUID-唯一性")
    public void testFastSimpleUUID_Unique() {
        String uuid1 = IdUtils.fastSimpleUUID();
        String uuid2 = IdUtils.fastSimpleUUID();
        assertNotEquals(uuid1, uuid2);
    }

    @Test
    @DisplayName("UUID生成性能-快速模式")
    public void testFastUUID_Performance() {
        // 快速生成100个UUID，应该很快完成
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            IdUtils.fastUUID();
        }
        long duration = System.currentTimeMillis() - start;
        // 应该在100ms内完成
        assertTrue(duration < 1000, "Fast UUID generation took too long: " + duration + "ms");
    }
}
