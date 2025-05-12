package org.meeuw.test.math.abstractalgebra.padic;

import lombok.extern.log4j.Log4j2;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import org.junit.jupiter.api.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import org.meeuw.math.abstractalgebra.padic.PAdicInteger;
import org.meeuw.math.abstractalgebra.padic.PAdicIntegers;
import org.meeuw.theories.abstractalgebra.FieldTheory;

import static org.assertj.core.api.Assertions.assertThat;

@Log4j2
@Timeout(unit = TimeUnit.SECONDS, value = 10)
@Disabled("WIP")
class PadicIntegersTest {

    PAdicIntegers p5 = PAdicIntegers.of(5);

    @Test
    public void simple() {
        PAdicInteger a = p5.of("0", "234");
        PAdicInteger b = p5.of("0", "340");

        PAdicInteger c = a.p(b);
        System.out.printf("\n %s\n %s\n---------+\n%s%n", a, b, c);

        assertThat(c.bigIntegerValue().intValue()).isEqualTo((4 + 3 * 5  + 2 * 5 * 5 ) + (4 * 5 + 3 * 5 * 5));


        assertThat(c.toString()).isEqualTo("...0 1124₅");

        log.info("{} + {} = {}", a.bigIntegerValue(), b.bigIntegerValue(), c.bigIntegerValue());
    }

    @Test
    public void negationOfOne() {
        PAdicInteger minusOne = p5.one().negation();
        assertThat(minusOne.toString()).isEqualTo("...4 ₅");

        assertThat(p5.one().plus(minusOne)).isEqualTo(p5.zero());
        assertThat(minusOne.negation()).isEqualTo(p5.one());
    }


    @Test
    public void negationOfMinusOne() {
        PAdicInteger minusOne = p5.of("4", "");
        assertThat(minusOne.negation()).isEqualTo(p5.one());
    }
    @ParameterizedTest
    @CsvSource(value = {
        "040, 4233301",
        "0430, 123031240"
    })
    public void timesOne(String repetend, String digits) {
        PAdicInteger sample = p5.of(repetend, digits);
        PAdicInteger timesOne = sample.times(p5.one());
        log.info("{} x {} = {}", sample, p5.one(), timesOne);
        assertThat(timesOne).isEqualTo(sample);
    }

    @Test
    public void negation() {
        PAdicInteger minusOne = p5.one().negation();
        assertThat(minusOne.toString()).isEqualTo("...4 ₅");
    }

    @Test
    public void random() {
        Random r = new Random(1);
        PAdicInteger random = p5.nextRandom(r);
        assertThat(random.toString()).isEqualTo("...120444 44133432324221₅");
        for (int i = 0; i < 200; i++) {
            log.info("{}", p5.nextRandom(r));
        }
    }

    @Test
    public void withRepetitive() {
        PAdicInteger a = p5.of("0", "234");
        PAdicInteger b = a.withRepetend(1, 3);

    }

    @Disabled("WIP")
    public static class PAdic5Test implements FieldTheory<PAdicInteger> {

        PAdicIntegers p5 = PAdicIntegers.of(5);


        @Override
        public Arbitrary<PAdicInteger> elements() {
            return Arbitraries
                .randomValue(r -> p5.nextRandom(r));
        }
    }


}
