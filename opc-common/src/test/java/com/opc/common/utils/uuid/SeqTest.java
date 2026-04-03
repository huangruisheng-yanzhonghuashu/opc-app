package com.opc.common.utils.uuid;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class SeqTest {

    @Test
    public void testGetId() {
        String id = Seq.getId();
        assertNotNull(id);
        assertTrue(id.length() >= 16);
    }

    @Test
    public void testGetIdWithType() {
        String id1 = Seq.getId(Seq.commSeqType);
        String id2 = Seq.getId(Seq.uploadSeqType);

        assertNotNull(id1);
        assertNotNull(id2);
        assertTrue(id1.length() >= 16);
        assertTrue(id2.length() >= 16);
    }

    @Test
    public void testGetIdUniqueness() {
        Set<String> ids = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            String id = Seq.getId();
            assertFalse(ids.contains(id), "Duplicate ID generated: " + id);
            ids.add(id);
        }
    }

    @Test
    public void testGetIdFormat() {
        String id = Seq.getId();
        // Format: yyMMddHHmmss + machineCode + sequence
        assertTrue(id.length() >= 16);
        assertTrue(id.matches("\\d{14}A\\d+"));
    }

    @Test
    public void testGetIdWithCustomAtomicInteger() {
        AtomicInteger atomicInt = new AtomicInteger(1);
        String id = Seq.getId(atomicInt, 3);
        assertNotNull(id);
        assertTrue(id.contains("A"));
    }

    @Test
    public void testSequenceIncrement() {
        String id1 = Seq.getId();
        String id2 = Seq.getId();

        assertNotEquals(id1, id2);
    }

    @Test
    public void testSequenceRollover() {
        // Test that sequence rolls over after reaching max
        AtomicInteger atomicInt = new AtomicInteger(998);
        String id1 = Seq.getId(atomicInt, 3);
        String id2 = Seq.getId(atomicInt, 3);
        String id3 = Seq.getId(atomicInt, 3);

        assertNotNull(id1);
        assertNotNull(id2);
        assertNotNull(id3);
    }

    @Test
    public void testCommonSequenceType() {
        assertEquals("COMMON", Seq.commSeqType);
    }

    @Test
    public void testUploadSequenceType() {
        assertEquals("UPLOAD", Seq.uploadSeqType);
    }

    @Test
    public void testMachineCode() {
        String id = Seq.getId();
        assertTrue(id.contains("A"));
    }

    @Test
    public void testConcurrentIdGeneration() throws InterruptedException {
        Set<String> ids = new HashSet<>();
        Thread[] threads = new Thread[10];

        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    String id = Seq.getId();
                    synchronized (ids) {
                        ids.add(id);
                    }
                }
            });
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        assertEquals(100, ids.size());
    }
}
