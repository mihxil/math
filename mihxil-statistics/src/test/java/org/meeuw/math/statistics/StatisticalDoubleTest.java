package org.meeuw.math.statistics;

import lombok.extern.log4j.Log4j2;
import net.jqwik.api.*;

import java.util.Random;

import org.junit.jupiter.api.Test;
import org.meeuw.math.abstractalgebra.test.CompleteFieldTheory;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author Michiel Meeuwissen
 */
@Log4j2
public class StatisticalDoubleTest implements CompleteFieldTheory<UncertainReal> {


    @Test
    public void test1() {
        assertThat(new StatisticalDouble().enter(-10, 20).toString()).isEqualTo("5 ± 15");
    }

    @Test
    public void test2() {
        assertThat(new StatisticalDouble().enter(0, 0).toString()).isEqualTo("0");
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

    @Test
    public void testEqualsWhenOnlyOne() {
        StatisticalDouble d1 = new StatisticalDouble();
        StatisticalDouble d2 = new StatisticalDouble();
        assertThat(d1).isEqualTo(d2);
        d1.enter(0.5);
        d2.enter(0.5);
        assertThat(d1).isEqualTo(d2);
    }

    @Test
    public void reset() {
        StatisticalDouble d = new StatisticalDouble();
        d.enter(1, 2, 3);
        d.reset();
        d.enter(3, 4, 5);
        assertThat(d.getValue()).isEqualTo(4);
    }

    @Property
    public void testString(@ForAll(ELEMENTS) StatisticalDouble e) {
        log.info("{} {}", e.getCount(), e);
    }

    @Override
    public Arbitrary<UncertainReal> elements() {
        Arbitrary<Integer> amounts = Arbitraries.integers().between(1, 100).shrinkTowards(2).withDistribution(RandomDistribution.uniform());
        Arbitrary<Double> averages = Arbitraries.doubles().between(-1000d, 1000d);
        Arbitrary<Random> random = Arbitraries.randoms();
        return Combinators.combine(amounts, averages, random)
            .flatAs((am, av, r) -> {
                StatisticalDouble sd = new StatisticalDouble();
                r.doubles(am).forEach(d ->
                    sd.accept(av + d * av / 3)
                );
                return Arbitraries.of(sd);
            });

    }
}
