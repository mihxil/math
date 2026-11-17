package org.meeuw.test.math.abstractalgebra.padic;

import lombok.extern.java.Log;

import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import org.meeuw.math.abstractalgebra.padic.PAdicNumber;
import org.meeuw.math.abstractalgebra.padic.PAdicNumbers;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.exceptions.InvalidStructureCreationException;
import org.meeuw.math.exceptions.NotFiniteException;
import org.meeuw.theories.abstractalgebra.FieldTheory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Log
@Timeout(unit = TimeUnit.SECONDS, value = 10)
class PAdicNumbersTest {

    PAdicNumbers p5 = PAdicNumbers.of(5);

    @Test
    void simple() {
        PAdicNumber a = p5.of("0", "234");
        PAdicNumber b = p5.of("0", "340");

        PAdicNumber c = a.p(b);
        System.out.printf("\n %s\n %s\n---------+\n%s%n", a, b, c);

        assertThat(c.bigIntegerValue().intValue()).isEqualTo((4 + 3 * 5  + 2 * 5 * 5 ) + (4 * 5 + 3 * 5 * 5));


        assertThat(c.toString()).isEqualTo("...0 1124₅");

        log.info("%s + %s = %s".formatted(a.bigIntegerValue(), b.bigIntegerValue(), c.bigIntegerValue()));
    }

    @Test
    void negationOfOne() {
        PAdicNumber minusOne = p5.one().negation();
        assertThat(minusOne.toString()).isEqualTo("...4 ₅");

        assertThat(p5.one().plus(minusOne)).isEqualTo(p5.zero());
        assertThat(minusOne.negation()).isEqualTo(p5.one());
    }


    @Test
    void negationOfMinusOne() {
        PAdicNumber minusOne = p5.of("4", "");
        assertThat(minusOne.negation()).isEqualTo(p5.one());
    }
    @ParameterizedTest
    @CsvSource(value = {
        "040, 4233301",
        "0430, 123031240"
    })
    void timesOne(String repetend, String digits) {
        PAdicNumber sample = p5.of(repetend, digits);
        PAdicNumber timesOne = sample.times(p5.one());
        log.info("%s x %s = %s".formatted(sample, p5.one(), timesOne));
        assertThat(timesOne).isEqualTo(sample);
    }

    @Test
    void negation() {
        PAdicNumber minusOne = p5.one().negation();
        assertThat(minusOne.toString()).isEqualTo("...4 ₅");
    }

    @Test
    void random() {
        Random r = new Random(1);
        PAdicNumber random = p5.nextRandom(r);
        assertThat(random.toString()).isEqualTo("...120444 44133432324221₅");
        for (int i = 0; i < 200; i++) {
            log.info("" + p5.nextRandom(r));
        }
    }

    @Test
    void withRepetitive() {
        PAdicNumber a = p5.of("0", "234");
        PAdicNumber b = a.withRepetend(1, 3);

    }

    @Test
    void reciprocalOfTwo() {
        // In 5-adic integers, 1/2 should satisfy 2 * (1/2) = 1
        PAdicNumber two = p5.of(new int[]{2});
        PAdicNumber reciprocal = two.reciprocal();
        log.info("1/2 in 5-adic = " + reciprocal);

        PAdicNumber product = two.times(reciprocal);
        log.info("2 * (1/2) = " + product);

        assertThat(product).isEqualTo(p5.one());
    }

    @Test
    void reciprocalOfThree() {
        // In 5-adic integers, 1/3 should satisfy 3 * (1/3) = 1
        PAdicNumber three = p5.of(new int[]{3});
        PAdicNumber reciprocal = three.reciprocal();
        log.info("1/3 in 5-adic = " + reciprocal);

        PAdicNumber product = three.times(reciprocal);
        log.info("3 * (1/3) = " + product);

        assertThat(product).isEqualTo(p5.one());
    }

    @Test
    void dividedByTwo() {
        // Test that 4 / 2 = 2
        PAdicNumber four = p5.of(new int[]{4});
        PAdicNumber two = p5.of(new int[]{2});
        PAdicNumber result = four.dividedBy(two);
        log.info("4 / 2 = " + result);

        assertThat(result).isEqualTo(two);
    }


    @Test
    void examples() {
        assertThat(p5.valueOf(-1).toString()).isEqualTo("...4 ₅");
        assertThat(p5.valueOf(14).rightShift(1).toString()).isEqualTo("...0 2.4₅");
        assertThat(p5.one().dividedBy(BigInteger.TWO).toString()).isEqualTo("...2 3₅");
        assertThat(p5.one().dividedBy(BigInteger.valueOf(6)).toString()).isEqualTo("...04 1₅");
        assertThat(p5.one().dividedBy(BigInteger.valueOf(5)).toString()).isEqualTo("...0 .1₅");
    }

    @Test
    void fractions() {
        PAdicNumber fifth = p5.one().dividedBy(BigInteger.valueOf(5));
        assertThat(fifth.times(5)).isEqualTo(p5.one());
        assertThat(fifth.valuation()).isEqualTo(-1);
        assertThat(fifth.isInteger()).isFalse();
        assertThat(p5.of("0", ".1")).isEqualTo(fifth);
        assertThat(p5.of("0", "2.4")).isEqualTo(p5.valueOf(14).rightShift(1));
        assertThat(p5.of("4", "3.21").toString()).isEqualTo("...4 3.21₅");
        assertThat(p5.of("4", "3.21").leftShift(2)).isEqualTo(p5.of("4", "321"));

        // digits after the point of which a part comes from the repetend
        PAdicNumber x = p5.of("12", "").rightShift(3);
        assertThat(x.toString()).isEqualTo("...21 .212₅");
        assertThat(x.leftShift(3)).isEqualTo(p5.of("12", ""));
    }

    @Test
    void valuation() {
        assertThat(p5.valueOf(25).valuation()).isEqualTo(2);
        assertThat(p5.valueOf(3).valuation()).isEqualTo(0);
        assertThat(p5.valueOf(3).dividedBy(BigInteger.valueOf(125)).valuation()).isEqualTo(-3);
        assertThat(p5.zero().valuation()).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    void bigIntegerValue() {
        assertThat(p5.of("4", "").bigIntegerValue()).isEqualTo(BigInteger.valueOf(-1));
        assertThat(p5.of("0", "234").bigIntegerValue()).isEqualTo(BigInteger.valueOf(2 * 25 + 3 * 5 + 4));
        assertThatThrownBy(() -> p5.of("1", "").bigIntegerValue()).isInstanceOf(NotFiniteException.class);
    }

    @Test
    void reciprocalOfRepetitive() {
        PAdicNumber x = p5.of("1234", "3");
        assertThat(x.reciprocal().times(x)).isEqualTo(p5.one());
        assertThat(x.reciprocal().reciprocal()).isEqualTo(x);
    }

    /**
     * The repetend of 1/1000003 is too long to show, only the least significant digits are shown then.
     */
    @Test
    void longRepetend() {
        PAdicNumber x = p5.one().dividedBy(BigInteger.valueOf(1000003));
        assertThat(x.adicDigits(PAdicNumber.MAXIMAL_SHOWN_DIGITS)).isEmpty();
        String string = x.toString();
        assertThat(string).startsWith("…").endsWith("₅").hasSize(PAdicNumber.MAXIMAL_SHOWN_DIGITS + 2);
        assertThat(x.times(1000003)).isEqualTo(p5.one());
    }

    @Test
    void invalidDigit() {
        assertThatThrownBy(() -> p5.of("0", "5")).isInstanceOf(InvalidElementCreationException.class);
    }

    @Test
    void notAPrime() {
        assertThatThrownBy(() -> PAdicNumbers.of(10)).isInstanceOf(InvalidStructureCreationException.class);
    }

    public static class PAdic5Test implements FieldTheory<PAdicNumber> {

        PAdicNumbers p5 = PAdicNumbers.of(5);


        @Override
        public Arbitrary<PAdicNumber> elements() {
            return Arbitraries
                .randomValue(r -> p5.nextRandom(r));
        }
    }


}
