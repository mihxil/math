package org.meeuw.math;

import org.junit.Test;

/**
 * @author Michiel Meeuwissen
 */
public class MeasurementTest {

    @Test
    public void test() {
        System.out.println(new Measurement().enter(-10, 20));
        System.out.println(new Measurement().enter(0, 0));
        System.out.println(new Measurement().enter(0, 1));
        System.out.println(new Measurement().enter(-0.001, -0.002));
        System.out.println(new Measurement().enter(0.5, 1.5));
        System.out.println(new Measurement().enter(20000, 20001, 20002, 20003));
        System.out.println(new Measurement().enter(20000, 20010, 20020, 20030));
        System.out.println(new Measurement().enter(20000, 20100, 20200, 20300));
        System.out.println(new Measurement().enter(0.000002, 0.0000021, 0.0000022, 0.0000023));
        System.out.println(new Measurement().enter(0.0000002, 0.000000201, 0.000000202, 0.000000203));
    }

}
