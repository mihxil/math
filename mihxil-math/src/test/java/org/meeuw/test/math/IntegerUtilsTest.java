package org.meeuw.test.math;

import lombok.extern.log4j.Log4j2;

import net.jqwik.api.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import org.meeuw.math.IntegerUtils;
import org.meeuw.math.exceptions.*;
import org.meeuw.math.text.TextUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Log4j2
class IntegerUtilsTest {



    @Test
    public void checkPower() {
        assertThat(IntegerUtils.checkPower(128, 2)).isEqualTo(7);
        assertThat(IntegerUtils.checkPower(127, 2)).isEqualTo(-1);
        assertThat(IntegerUtils.checkPower(128)).containsExactly(2, 7);
        assertThat(IntegerUtils.checkPower(IntegerUtils.positivePow(7, 3))).containsExactly(7, 3);
        assertThat(IntegerUtils.checkPower(13)).containsExactly(0, 0);
    }

    @Test
    public void checkPower2() {
        assertThat(IntegerUtils.checkPower(4)).containsExactly(2, 2);
    }

    @Test
    void gcd() {
        assertThat(IntegerUtils.gcd(123L, 13L)).isEqualTo(1L);
        assertThat(IntegerUtils.gcd(169L, 26L)).isEqualTo(13L);
        assertThat(IntegerUtils.gcd(10, 8)).isEqualTo(2);
        assertThat(IntegerUtils.gcd(8, 12)).isEqualTo(4);
        assertThat(IntegerUtils.gcd(-8, 12)).isEqualTo(4);
        assertThat(IntegerUtils.gcd(-8, -12)).isEqualTo(4);
        assertThat(IntegerUtils.gcd(8, -12)).isEqualTo(4);
    }


    @Test
    public void digits() {
        assertThat(IntegerUtils.fromDigits(1, 2, 3)).isEqualTo(123);

        assertThatThrownBy(() -> IntegerUtils.fromDigitsInBase(4, 1, 2, 4)).isInstanceOf(IllegalArgumentException.class);

    }



    @ParameterizedTest
    @ValueSource(ints = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199})
    public void isPrime(int prime) {
        assertThat(IntegerUtils.isPrime(prime)).isTrue();
    }
    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 1, 4, 6, 8, 9, 10, 12, 14, 15, 16, 18, 20, 21, 22, 24, 25, 26, 27, 28, 30, 32, 33, 34, 35, 36, 38, 39, 40, 42, 44, 45, 46, 48, 49, 50, 51, 52, 54, 55, 56, 57, 58, 60, 62, 63, 64, 65, 66, 68, 69, 70, 72, 74, 75, 76, 77, 78, 80, 81,82, 84, 85, 86, 87, 88, 90, 91, 92, 93, 94, 95, 96, 98, 99, 100})
    public void isNotPrime(int composite) {
        assertThat(IntegerUtils.isPrime(composite)).isFalse();
    }


    @Test
    public void factorization() {
        assertThat(IntegerUtils.primeFactorization(25)).containsExactly(5L, 5L);
        assertThat(IntegerUtils.primeFactorization(13)).containsExactly(13L);
        assertThat(IntegerUtils.primeFactorization(64)).containsExactly(2L, 2L, 2L, 2L, 2L, 2L);
        assertThat(IntegerUtils.primeFactorization(12345L)).containsExactly(3L, 5L, 823L);
        assertThat(IntegerUtils.primeFactorization(-12345L)).containsExactly(3L, 5L, 823L);
        assertThat(IntegerUtils.primeFactorization(1)).containsExactly();
        assertThat(IntegerUtils.primeFactorization(0)).containsExactly();
    }

    @Test
    public void isNotPrimePower() {
        assertThat(IntegerUtils.isPrimePower(13 * 13 * 2 * 2)).isFalse();
    }
    @Test
    public void isPrimePower() {
        assertThat(IntegerUtils.isPrimePower(13 * 13)).isTrue();
    }



    @Test
    public void positivePower() {
        assertThatThrownBy(() -> IntegerUtils.positivePow10(-1)).isInstanceOf(IllegalPowerException.class);
        assertThat(IntegerUtils.positivePow10(2)).isEqualTo(100);
    }


    @Property
    public void factorization(@ForAll("positiveLongs") long random) {
        StringBuilder builder = new StringBuilder();

        assertThat(IntegerUtils.primeFactorization(random)
            .reduce(1L, (l1, l2) -> {
                if (builder.length() > 0 ) {
                    builder.append(" " + TextUtils.TIMES + " ");
                }
                builder.append(l2);
                return l1 * l2;
            })).isEqualTo(random);
        log.info("{} = {} ({} {})", random, builder.toString(), IntegerUtils.isPrime((int) random), IntegerUtils.isPrimePower(random));
    }


   @Test
    public void sqrt() {
        assertThat(IntegerUtils.sqrt(4)).isEqualTo(2);
        assertThat(IntegerUtils.floorSqrt(5)).isEqualTo(2);
        assertThatThrownBy(() -> IntegerUtils.sqrt(5)).isInstanceOf(MathException.class);
    }

    @Test
    public void factorial() {
        assertThat(IntegerUtils.factorial(4)).isEqualTo(24);
        assertThat(IntegerUtils.factorial(0)).isEqualTo(1);
        assertThatThrownBy(() -> IntegerUtils.factorial(-1)).isInstanceOf(InvalidFactorial.class);
    }


    @Test
    public void log10() {
        long start = System.currentTimeMillis();
        int d = 0;
        for (int i = 0; i < 1000000L; i++) {
            d = IntegerUtils.log10(123456789);
        }
        log.info("{} : {}", d, System.currentTimeMillis() - start);
        assertThat(IntegerUtils.log10(10)).isEqualTo(1);
        assertThat(IntegerUtils.log10(100)).isEqualTo(2);
        assertThat(IntegerUtils.log10(20)).isEqualTo(1);
    }



    @Provide
    Arbitrary<Long> positiveLongs() {
        return Arbitraries.randomValue(random -> (long) random.nextInt(1_000_000_000));
    }

}
