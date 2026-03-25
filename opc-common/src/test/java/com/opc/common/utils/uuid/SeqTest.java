package com.opc.common.utils.uuid;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SeqTest
{
    @Test
    public void testGetId()
    {
        String id1 = Seq.getId();
        String id2 = Seq.getId();

        assertNotNull(id1);
        assertNotNull(id2);
        assertEquals(18, id1.length());
        assertEquals(18, id2.length());
        assertNotEquals(id1, id2);
    }

    @Test
    public void testGetIdWithTypeCommon()
    {
        String id = Seq.getId(Seq.commSeqType);

        assertNotNull(id);
        assertEquals(18, id.length());
    }

    @Test
    public void testGetIdWithTypeUpload()
    {
        String id = Seq.getId(Seq.uploadSeqType);

        assertNotNull(id);
        assertEquals(18, id.length());
    }

    @Test
    public void testGetIdWithUnknownType()
    {
        String id = Seq.getId("UNKNOWN");

        assertNotNull(id);
        assertEquals(18, id.length());
    }

    @Test
    public void testSeqIncrement()
    {
        String id1 = Seq.getId();
        String id2 = Seq.getId();

        String seq1 = id1.substring(id1.length() - 3);
        String seq2 = id2.substring(id2.length() - 3);

        int num1 = Integer.parseInt(seq1);
        int num2 = Integer.parseInt(seq2);

        assertEquals(num1 + 1, num2);
    }

    @Test
    public void testSeqFormat()
    {
        String id = Seq.getId();

        assertTrue(id.matches("\\d{14}A\\d{3}"));
    }

    @Test
    public void testCommSeqTypeConstant()
    {
        assertEquals("COMMON", Seq.commSeqType);
    }

    @Test
    public void testUploadSeqTypeConstant()
    {
        assertEquals("UPLOAD", Seq.uploadSeqType);
    }

    @Test
    public void testMultipleCalls()
    {
        for (int i = 0; i < 100; i++)
        {
            String id = Seq.getId();
            assertNotNull(id);
            assertEquals(18, id.length());
        }
    }
}
