package com.opc.common.utils.uuid;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UUIDTest {

    @Test
    public void testRandomUUID() {
        UUID uuid = UUID.randomUUID();
        assertNotNull(uuid);
        assertEquals(4, uuid.version());
    }

    @Test
    public void testFastUUID() {
        UUID uuid = UUID.fastUUID();
        assertNotNull(uuid);
        assertEquals(4, uuid.version());
    }

    @Test
    public void testRandomUUIDWithSecure() {
        UUID uuid = UUID.randomUUID(true);
        assertNotNull(uuid);
        assertEquals(4, uuid.version());
    }

    @Test
    public void testRandomUUIDWithoutSecure() {
        UUID uuid = UUID.randomUUID(false);
        assertNotNull(uuid);
        assertEquals(4, uuid.version());
    }

    @Test
    public void testNameUUIDFromBytes() {
        byte[] name = "test".getBytes();
        UUID uuid = UUID.nameUUIDFromBytes(name);
        assertNotNull(uuid);
        assertEquals(3, uuid.version());
    }

    @Test
    public void testFromString() {
        String uuidStr = "550e8400-e29b-41d4-a716-446655440000";
        UUID uuid = UUID.fromString(uuidStr);
        assertNotNull(uuid);
        assertEquals(uuidStr, uuid.toString());
    }

    @Test
    public void testFromStringInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            UUID.fromString("invalid-uuid");
        });
    }

    @Test
    public void testToString() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        assertNotNull(str);
        assertEquals(36, str.length());
        assertTrue(str.contains("-"));
    }

    @Test
    public void testToStringSimple() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString(true);
        assertNotNull(str);
        assertEquals(32, str.length());
        assertFalse(str.contains("-"));
    }

    @Test
    public void testGetLeastSignificantBits() {
        UUID uuid = UUID.randomUUID();
        long bits = uuid.getLeastSignificantBits();
        assertNotEquals(0, bits);
    }

    @Test
    public void testGetMostSignificantBits() {
        UUID uuid = UUID.randomUUID();
        long bits = uuid.getMostSignificantBits();
        assertNotEquals(0, bits);
    }

    @Test
    public void testVariant() {
        UUID uuid = UUID.randomUUID();
        int variant = uuid.variant();
        assertEquals(2, variant); // IETF RFC 4122 variant
    }

    @Test
    public void testTimestampThrowsException() {
        UUID uuid = UUID.randomUUID(); // Version 4
        assertThrows(UnsupportedOperationException.class, () -> {
            uuid.timestamp();
        });
    }

    @Test
    public void testClockSequenceThrowsException() {
        UUID uuid = UUID.randomUUID(); // Version 4
        assertThrows(UnsupportedOperationException.class, () -> {
            uuid.clockSequence();
        });
    }

    @Test
    public void testNodeThrowsException() {
        UUID uuid = UUID.randomUUID(); // Version 4
        assertThrows(UnsupportedOperationException.class, () -> {
            uuid.node();
        });
    }

    @Test
    public void testEquals() {
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();
        UUID uuid3 = uuid1;

        assertTrue(uuid1.equals(uuid3));
        assertFalse(uuid1.equals(uuid2));
        assertFalse(uuid1.equals(null));
        assertFalse(uuid1.equals("not a uuid"));
    }

    @Test
    public void testHashCode() {
        UUID uuid = UUID.randomUUID();
        int hashCode = uuid.hashCode();
        assertNotEquals(0, hashCode);
    }

    @Test
    public void testCompareTo() {
        UUID uuid1 = new UUID(1L, 1L);
        UUID uuid2 = new UUID(2L, 2L);
        UUID uuid3 = new UUID(1L, 1L);

        assertTrue(uuid1.compareTo(uuid2) < 0);
        assertTrue(uuid2.compareTo(uuid1) > 0);
        assertEquals(0, uuid1.compareTo(uuid3));
    }

    @Test
    public void testGetSecureRandom() {
        assertNotNull(UUID.getSecureRandom());
    }

    @Test
    public void testGetRandom() {
        assertNotNull(UUID.getRandom());
    }

    @Test
    public void testUUIDUniqueness() {
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();
        UUID uuid3 = UUID.randomUUID();

        assertNotEquals(uuid1, uuid2);
        assertNotEquals(uuid2, uuid3);
        assertNotEquals(uuid1, uuid3);
    }
}
