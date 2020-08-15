package org.meeuw.statistics;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;

import org.junit.jupiter.api.Test;
import org.meeuw.math.abstractalgebra.test.FieldTheory;
import org.meeuw.math.uncertainnumbers.UncertainNumberElement;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author Michiel Meeuwissen
 */
public class StatisticalDoubleTest implements FieldTheory<UncertainNumberElement> {


    @Test
    public void test1() {
        assertThat(new StatisticalDouble().enter(-10, 20).toString()).isEqualTo("5 ± 15");
    }

    @Test
    public void test2() {
        assertThat(new StatisticalDouble().enter(0, 0).toString()).isEqualTo("0.0 ± 0.0");
    }

    @Test
    public void test3() {
        assertThat(new StatisticalDouble().enter(0, 1).toString()).isEqualTo("0.5 ± 0.5");
    }

    @Test
    public void test4() {
        assertThat(new StatisticalDouble().enter(-0.001, -0.002).toString()).isEqualTo("-0.0015 ± 0.0005");
    }

    @Test
    public void test5() {
        assertThat(new StatisticalDouble().enter(0.5, 1.5).toString()).isEqualTo("1.0 ± 0.5");
    }

    @Test
    public void test6() {
        assertThat(new StatisticalDouble().enter(20000, 20001, 20002, 20003).toString()).isEqualTo("20001.5 ± 1.1");
    }

    @Test
    public void test7() {
        assertThat(new StatisticalDouble().enter(20000, 20010, 20020, 20030).toString()).isEqualTo("20015 ± 11");
    }

    @Test
    public void test8() {
        assertThat(new StatisticalDouble().enter(20000, 20100, 20200, 20300).toString()).isEqualTo("(2.015 ± 0.011)·10⁴");
    }
    @Test
    public void test9 () {
        assertThat(new StatisticalDouble().enter(0.000002, 0.0000021, 0.0000022, 0.0000023).toString()).isEqualTo("(2.15 ± 0.11)·10⁻⁶");
    }

    @Test
    public void test10() {
        assertThat(new StatisticalDouble().enter(0.0000002, 0.000000201, 0.000000202, 0.000000203).toString()).isEqualTo("(2.015 ± 0.011)·10⁻⁷");
    }

    @Override
    public Arbitrary<UncertainNumberElement> elements() {
        StatisticalDouble v1 = new StatisticalDouble().enter(0.000002, 0.0000021, 0.0000022, 0.0000023);
        StatisticalDouble v2 = new StatisticalDouble().enter(0.000002, 0.0000021, 0.0000022, 0.0000023).times(2);
        StatisticalDouble v3 = new StatisticalDouble().enter(0.000002, 0.0000021, 0.0000022, 0.0000023).times(3);
        return Arbitraries.of(v1, v2, v3);
    }
}
