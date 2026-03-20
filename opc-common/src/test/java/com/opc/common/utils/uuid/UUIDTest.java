package com.opc.common.utils.uuid;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * UUID实现类测试
 */
public class UUIDTest {

    @Test
    @DisplayName("生成随机UUID-标准格式")
    public void testRandomUUID() {
        UUID uuid = UUID.randomUUID();
        assertNotNull(uuid);
        
        String uuidStr = uuid.toString();
        assertEquals(36, uuidStr.length());
        assertTrue(uuidStr.contains("-"));
        // 验证UUID格式
        assertTrue(uuidStr.matches("^[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$"));
    }

    @Test
    @DisplayName("生成快速UUID")
    public void testFastUUID() {
        UUID uuid = UUID.fastUUID();
        assertNotNull(uuid);
        
        String uuidStr = uuid.toString();
        assertEquals(36, uuidStr.length());
        // 验证是版本4的UUID
        assertEquals(4, uuid.version());
    }

    @Test
    @DisplayName("UUID唯一性")
    public void testUUID_Unique() {
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();
        assertNotEquals(uuid1, uuid2);
        assertNotEquals(uuid1.toString(), uuid2.toString());
    }

    @Test
    @DisplayName("UUID转字符串-标准格式")
    public void testToString_Normal() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        assertNotNull(str);
        assertEquals(36, str.length());
        assertTrue(str.contains("-"));
    }

    @Test
    @DisplayName("UUID转字符串-简化格式")
    public void testToString_Simple() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString(true);
        assertNotNull(str);
        assertEquals(32, str.length());
        assertFalse(str.contains("-"));
    }

    @Test
    @DisplayName("从字符串解析UUID-成功")
    public void testFromString_Success() {
        String uuidStr = "550e8400-e29b-41d4-a716-446655440000";
        UUID uuid = UUID.fromString(uuidStr);
        assertNotNull(uuid);
        assertEquals(uuidStr, uuid.toString());
    }

    @Test
    @DisplayName("从字符串解析UUID-无效格式")
    public void testFromString_Invalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            UUID.fromString("invalid-uuid");
        });
    }

    @Test
    @DisplayName("从字符串解析UUID-空值")
    public void testFromString_Null() {
        assertThrows(IllegalArgumentException.class, () -> {
            UUID.fromString(null);
        });
    }

    @Test
    @DisplayName("基于名称生成UUID")
    public void testNameUUIDFromBytes() {
        byte[] name = "test".getBytes();
        UUID uuid = UUID.nameUUIDFromBytes(name);
        assertNotNull(uuid);
        // 基于名称的UUID是版本3
        assertEquals(3, uuid.version());
        
        // 相同输入产生相同UUID
        UUID uuid2 = UUID.nameUUIDFromBytes(name);
        assertEquals(uuid, uuid2);
    }

    @Test
    @DisplayName("UUID相等性-相同")
    public void testEquals_Same() {
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.fromString(uuid1.toString());
        assertEquals(uuid1, uuid2);
        assertEquals(uuid1.hashCode(), uuid2.hashCode());
    }

    @Test
    @DisplayName("UUID相等性-不同")
    public void testEquals_Different() {
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();
        assertNotEquals(uuid1, uuid2);
    }

    @Test
    @DisplayName("UUID相等性-与null")
    public void testEquals_Null() {
        UUID uuid = UUID.randomUUID();
        assertNotEquals(uuid, null);
    }

    @Test
    @DisplayName("UUID相等性-不同类型")
    public void testEquals_DifferentType() {
        UUID uuid = UUID.randomUUID();
        assertNotEquals(uuid, "not-a-uuid");
    }

    @Test
    @DisplayName("UUID比较-小于")
    public void testCompareTo_Less() {
        UUID uuid1 = new UUID(0L, 0L);
        UUID uuid2 = new UUID(0L, 1L);
        assertTrue(uuid1.compareTo(uuid2) < 0);
    }

    @Test
    @DisplayName("UUID比较-等于")
    public void testCompareTo_Equal() {
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.fromString(uuid1.toString());
        assertEquals(0, uuid1.compareTo(uuid2));
    }

    @Test
    @DisplayName("UUID比较-大于")
    public void testCompareTo_Greater() {
        UUID uuid1 = new UUID(0L, 1L);
        UUID uuid2 = new UUID(0L, 0L);
        assertTrue(uuid1.compareTo(uuid2) > 0);
    }

    @Test
    @DisplayName("获取最高有效位")
    public void testGetMostSignificantBits() {
        UUID uuid = UUID.randomUUID();
        long msb = uuid.getMostSignificantBits();
        assertNotEquals(0L, msb);
    }

    @Test
    @DisplayName("获取最低有效位")
    public void testGetLeastSignificantBits() {
        UUID uuid = UUID.randomUUID();
        long lsb = uuid.getLeastSignificantBits();
        assertNotEquals(0L, lsb);
    }

    @Test
    @DisplayName("UUID版本号-版本4")
    public void testVersion_Version4() {
        UUID uuid = UUID.randomUUID();
        assertEquals(4, uuid.version());
    }

    @Test
    @DisplayName("UUID版本号-版本3")
    public void testVersion_Version3() {
        UUID uuid = UUID.nameUUIDFromBytes("test".getBytes());
        assertEquals(3, uuid.version());
    }

    @Test
    @DisplayName("UUID变体号")
    public void testVariant() {
        UUID uuid = UUID.randomUUID();
        // IETF变体是2
        assertEquals(2, uuid.variant());
    }

    @Test
    @DisplayName("UUID哈希码")
    public void testHashCode() {
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.fromString(uuid1.toString());
        assertEquals(uuid1.hashCode(), uuid2.hashCode());
    }

    @Test
    @DisplayName("时间戳-非时间UUID抛出异常")
    public void testTimestamp_NotTimeBased() {
        UUID uuid = UUID.randomUUID();
        assertThrows(UnsupportedOperationException.class, () -> {
            uuid.timestamp();
        });
    }

    @Test
    @DisplayName("时钟序列-非时间UUID抛出异常")
    public void testClockSequence_NotTimeBased() {
        UUID uuid = UUID.randomUUID();
        assertThrows(UnsupportedOperationException.class, () -> {
            uuid.clockSequence();
        });
    }

    @Test
    @DisplayName("节点值-非时间UUID抛出异常")
    public void testNode_NotTimeBased() {
        UUID uuid = UUID.randomUUID();
        assertThrows(UnsupportedOperationException.class, () -> {
            uuid.node();
        });
    }

    @Test
    @DisplayName("UUID构造-指定高低位")
    public void testConstructor_WithBits() {
        long msb = 0x1234567890ABCDEFL;
        long lsb = 0xFEDCBA0987654321L;
        UUID uuid = new UUID(msb, lsb);
        assertEquals(msb, uuid.getMostSignificantBits());
        assertEquals(lsb, uuid.getLeastSignificantBits());
    }

    @Test
    @DisplayName("快速UUID性能测试")
    public void testFastUUID_Performance() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            UUID.fastUUID();
        }
        long duration = System.currentTimeMillis() - start;
        // 应该在500ms内完成1000次
        assertTrue(duration < 500, "Fast UUID generation took too long: " + duration + "ms");
    }
}
