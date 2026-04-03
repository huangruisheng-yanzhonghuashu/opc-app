package com.opc.common.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ArithTest {

    @Test
    public void testAdd() {
        double result = Arith.add(1.5, 2.5);
        assertEquals(4.0, result, 0.0001);
    }

    @Test
    public void testAddWithNegative() {
        double result = Arith.add(-1.5, 2.5);
        assertEquals(1.0, result, 0.0001);
    }

    @Test
    public void testAddWithZero() {
        double result = Arith.add(0.0, 5.0);
        assertEquals(5.0, result, 0.0001);
    }

    @Test
    public void testSub() {
        double result = Arith.sub(5.0, 3.0);
        assertEquals(2.0, result, 0.0001);
    }

    @Test
    public void testSubWithNegative() {
        double result = Arith.sub(5.0, -3.0);
        assertEquals(8.0, result, 0.0001);
    }

    @Test
    public void testSubResultNegative() {
        double result = Arith.sub(3.0, 5.0);
        assertEquals(-2.0, result, 0.0001);
    }

    @Test
    public void testMul() {
        double result = Arith.mul(3.0, 4.0);
        assertEquals(12.0, result, 0.0001);
    }

    @Test
    public void testMulWithDecimal() {
        double result = Arith.mul(0.1, 0.2);
        assertEquals(0.02, result, 0.0001);
    }

    @Test
    public void testMulWithZero() {
        double result = Arith.mul(5.0, 0.0);
        assertEquals(0.0, result, 0.0001);
    }

    @Test
    public void testDiv() {
        double result = Arith.div(10.0, 2.0);
        assertEquals(5.0, result, 0.0001);
    }

    @Test
    public void testDivWithDecimal() {
        double result = Arith.div(10.0, 3.0);
        assertEquals(3.3333333333, result, 0.0001);
    }

    @Test
    public void testDivWithScale() {
        double result = Arith.div(10.0, 3.0, 2);
        assertEquals(3.33, result, 0.01);
    }

    @Test
    public void testDivWithZeroDividend() {
        double result = Arith.div(0.0, 5.0);
        assertEquals(0.0, result, 0.0001);
    }

    @Test
    public void testDivWithNegativeScale() {
        assertThrows(IllegalArgumentException.class, () -> {
            Arith.div(10.0, 2.0, -1);
        });
    }

    @Test
    public void testRound() {
        double result = Arith.round(3.14159, 2);
        assertEquals(3.14, result, 0.01);
    }

    @Test
    public void testRoundUp() {
        double result = Arith.round(3.146, 2);
        assertEquals(3.15, result, 0.01);
    }

    @Test
    public void testRoundWithZeroScale() {
        double result = Arith.round(3.7, 0);
        assertEquals(4.0, result, 0.01);
    }

    @Test
    public void testRoundWithNegativeScale() {
        assertThrows(IllegalArgumentException.class, () -> {
            Arith.round(3.14, -1);
        });
    }

    @Test
    public void testPrecision() {
        double result = Arith.add(0.1, 0.2);
        assertEquals(0.3, result, 0.0001);
    }

    @Test
    public void testComplexCalculation() {
        double sum = Arith.add(10.5, 20.3);  // 30.8
        double diff = Arith.sub(sum, 5.2);   // 25.6
        double product = Arith.mul(diff, 2.0); // 51.2
        double quotient = Arith.div(product, 4.0, 4); // 12.8
        assertEquals(12.8, quotient, 0.0001);
    }
}
