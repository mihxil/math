package org.meeuw.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Michiel Meeuwissen
 */
public class MeasurementTest {

    @Test
    public void test1() {
        assertEquals("5 ± 15", new Measurement().enter(-10, 20).toString());
    }

    @Test
    public void test2() {
        assertEquals("0.0 ± 0.0", new Measurement().enter(0, 0).toString());
    }

    @Test
    public void test3() {
        assertEquals("0.5 ± 0.5", new Measurement().enter(0, 1).toString());
    }

    @Test
    public void test4() {
        assertEquals("-0.0015 ± 0.0005", new Measurement().enter(-0.001, -0.002).toString());
    }

    @Test
    public void test5() {
        assertEquals("1.0 ± 0.5", new Measurement().enter(0.5, 1.5).toString());
    }

    @Test
    public void test6() {
        assertEquals("20001.5 ± 1.1", new Measurement().enter(20000, 20001, 20002, 20003).toString());
    }

    @Test
    public void test7() {
        assertEquals("20015 ± 11", new Measurement().enter(20000, 20010, 20020, 20030).toString());
    }

    @Test
    public void test8() {
        assertEquals("(2.015 ± 0.011)·10⁴", new Measurement().enter(20000, 20100, 20200, 20300).toString());
    }
    @Test
    public void test9 () {
        assertEquals("(2.15 ± 0.11)·10⁻⁶", new Measurement().enter(0.000002, 0.0000021, 0.0000022, 0.0000023).toString());
    }

    @Test
    public void test10() {
        assertEquals("(2.015 ± 0.011)·10⁻⁷", new Measurement().enter(0.0000002, 0.000000201, 0.000000202, 0.000000203).toString());
    }

}
