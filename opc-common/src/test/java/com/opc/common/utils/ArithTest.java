package com.opc.common.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ArithTest
{
    @Test
    public void testAdd()
    {
        assertEquals(3.0, Arith.add(1.0, 2.0), 0.0001);
        assertEquals(0.3, Arith.add(0.1, 0.2), 0.0001);
        assertEquals(-1.0, Arith.add(1.0, -2.0), 0.0001);
        assertEquals(100.0, Arith.add(50.0, 50.0), 0.0001);
    }

    @Test
    public void testSub()
    {
        assertEquals(1.0, Arith.sub(3.0, 2.0), 0.0001);
        assertEquals(-0.1, Arith.sub(0.1, 0.2), 0.0001);
        assertEquals(3.0, Arith.sub(1.0, -2.0), 0.0001);
        assertEquals(0.0, Arith.sub(50.0, 50.0), 0.0001);
    }

    @Test
    public void testMul()
    {
        assertEquals(6.0, Arith.mul(2.0, 3.0), 0.0001);
        assertEquals(0.02, Arith.mul(0.1, 0.2), 0.0001);
        assertEquals(-6.0, Arith.mul(2.0, -3.0), 0.0001);
        assertEquals(0.0, Arith.mul(100.0, 0.0), 0.0001);
    }

    @Test
    public void testDiv()
    {
        assertEquals(2.0, Arith.div(6.0, 3.0), 0.0001);
        assertEquals(0.5, Arith.div(1.0, 2.0), 0.0001);
        assertEquals(-2.0, Arith.div(6.0, -3.0), 0.0001);
    }

    @Test
    public void testDivWithScale()
    {
        assertEquals(0.3333, Arith.div(1.0, 3.0, 4), 0.0001);
        assertEquals(0.3333333333, Arith.div(1.0, 3.0, 10), 0.0000000001);
    }

    @Test
    public void testDivByZero()
    {
        assertEquals(0.0, Arith.div(0.0, 0.0), 0.0001);
    }

    @Test
    public void testDivNegativeScale()
    {
        assertThrows(IllegalArgumentException.class, () -> {
            Arith.div(1.0, 2.0, -1);
        });
    }

    @Test
    public void testRound()
    {
        assertEquals(1.23, Arith.round(1.234, 2), 0.0001);
        assertEquals(1.24, Arith.round(1.235, 2), 0.0001);
        assertEquals(1.24, Arith.round(1.236, 2), 0.0001);
        assertEquals(1.0, Arith.round(1.234, 0), 0.0001);
        assertEquals(0.0, Arith.round(0.0, 2), 0.0001);
    }

    @Test
    public void testRoundNegativeScale()
    {
        assertThrows(IllegalArgumentException.class, () -> {
            Arith.round(1.234, -1);
        });
    }
}
