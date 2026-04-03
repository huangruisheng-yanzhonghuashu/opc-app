package com.opc.common.utils.uuid;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IdUtilsTest {

    @Test
    public void testRandomUUID() {
        String uuid = IdUtils.randomUUID();
        assertNotNull(uuid);
        assertEquals(36, uuid.length());
        assertTrue(uuid.contains("-"));
    }

    @Test
    public void testSimpleUUID() {
        String uuid = IdUtils.simpleUUID();
        assertNotNull(uuid);
        assertEquals(32, uuid.length());
        assertFalse(uuid.contains("-"));
    }

    @Test
    public void testFastUUID() {
        String uuid = IdUtils.fastUUID();
        assertNotNull(uuid);
        assertEquals(36, uuid.length());
        assertTrue(uuid.contains("-"));
    }

    @Test
    public void testFastSimpleUUID() {
        String uuid = IdUtils.fastSimpleUUID();
        assertNotNull(uuid);
        assertEquals(32, uuid.length());
        assertFalse(uuid.contains("-"));
    }

    @Test
    public void testUUIDUniqueness() {
        String uuid1 = IdUtils.randomUUID();
        String uuid2 = IdUtils.randomUUID();
        String uuid3 = IdUtils.randomUUID();

        assertNotEquals(uuid1, uuid2);
        assertNotEquals(uuid2, uuid3);
        assertNotEquals(uuid1, uuid3);
    }

    @Test
    public void testSimpleUUIDUniqueness() {
        String uuid1 = IdUtils.simpleUUID();
        String uuid2 = IdUtils.simpleUUID();

        assertNotEquals(uuid1, uuid2);
    }

    @Test
    public void testFastUUIDUniqueness() {
        String uuid1 = IdUtils.fastUUID();
        String uuid2 = IdUtils.fastUUID();

        assertNotEquals(uuid1, uuid2);
    }

    @Test
    public void testUUIDFormat() {
        String uuid = IdUtils.randomUUID();
        // Check format: 8-4-4-4-12
        String[] parts = uuid.split("-");
        assertEquals(5, parts.length);
        assertEquals(8, parts[0].length());
        assertEquals(4, parts[1].length());
        assertEquals(4, parts[2].length());
        assertEquals(4, parts[3].length());
        assertEquals(12, parts[4].length());
    }

    @Test
    public void testFastUUIDPerformance() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            IdUtils.fastUUID();
        }
        long duration = System.currentTimeMillis() - start;
        assertTrue(duration < 5000); // Should complete within 5 seconds
    }
}
