/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.test.math;

import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Duration;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import net.jqwik.api.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import org.meeuw.math.Utils;
import org.meeuw.math.exceptions.*;
import org.meeuw.math.text.TextUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Michiel Meeuwissen
 */
@Log4j2
class UtilsTest {



    @Test
    public void log10() {
        long start = System.currentTimeMillis();
        int d = 0;
        for (int i = 0; i < 1000000L; i++) {
            d = Utils.log10(123456789);
        }
        log.info("{} : {}", d, System.currentTimeMillis() -start);
        assertThat(Utils.log10(10)).isEqualTo(1);
        assertThat(Utils.log10(100)).isEqualTo(2);
        assertThat(Utils.log10(10d)).isEqualTo(1);
        assertThat(Utils.log10(20)).isEqualTo(1);
        assertThat(Utils.log10(20d)).isEqualTo(1);
    }

    @Test
    public void positivePower() {
        assertThatThrownBy(() -> Utils.positivePow10(-1)).isInstanceOf(ReciprocalException.class);
        assertThat(Utils.positivePow10(2)).isEqualTo(100);
    }

    @Test
    public void uncertaintityForDouble() {
        assertThat(Utils.uncertaintyForDouble(0)).isEqualTo(4.9E-324);
        assertThat(Utils.uncertaintyForDouble(1e-300)).isEqualTo(3.31561842E-316);
        assertThat(Utils.uncertaintyForDouble(1e-100)).isEqualTo(2.5379418373156492E-116);
        assertThat(Utils.uncertaintyForDouble(1e-16)).isEqualTo(2.465190328815662E-32);
        assertThat(Utils.uncertaintyForDouble(-1)).isEqualTo(4.440892098500626E-16);
        assertThat(Utils.uncertaintyForDouble(1)).isEqualTo(4.440892098500626E-16);
    }


    @Test
    public void uncertaintityForBigDecimal() {
        assertThat(Utils.uncertaintyForBigDecimal(BigDecimal.TEN, MathContext.DECIMAL128)).isEqualTo(BigDecimal.ZERO);
        assertThat(Utils.uncertaintyForBigDecimal(new BigDecimal("0.123"), MathContext.DECIMAL32)).isEqualTo(new BigDecimal("1E-7"));

        assertThat(Utils.uncertaintyForBigDecimal(new BigDecimal("0.123456"), new MathContext(2))).isEqualTo(new BigDecimal("0.01"));


    }


    @ParameterizedTest
    @ValueSource(ints = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199})
    public void isPrime(int prime) {
        assertThat(Utils.isPrime(prime)).isTrue();
    }
    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 1, 4, 6, 8, 9, 10, 12, 14, 15, 16, 18, 20, 21, 22, 24, 25, 26, 27, 28, 30, 32, 33, 34, 35, 36, 38, 39, 40, 42, 44, 45, 46, 48, 49, 50, 51, 52, 54, 55, 56, 57, 58, 60, 62, 63, 64, 65, 66, 68, 69, 70, 72, 74, 75, 76, 77, 78, 80, 81,82, 84, 85, 86, 87, 88, 90, 91, 92, 93, 94, 95, 96, 98, 99, 100})
    public void isNotPrime(int composite) {
        assertThat(Utils.isPrime(composite)).isFalse();
    }


    @Test
    public void factorization() {
        assertThat(Utils.primeFactorization(25)).containsExactly(5L, 5L);
        assertThat(Utils.primeFactorization(13)).containsExactly(13L);
        assertThat(Utils.primeFactorization(64)).containsExactly(2L, 2L, 2L, 2L, 2L, 2L);
        assertThat(Utils.primeFactorization(12345L)).containsExactly(3L, 5L, 823L);
        assertThat(Utils.primeFactorization(-12345L)).containsExactly(3L, 5L, 823L);
        assertThat(Utils.primeFactorization(1)).containsExactly();
        assertThat(Utils.primeFactorization(0)).containsExactly();
    }

    @Test
    public void isNotPrimePower() {
        assertThat(Utils.isPrimePower(13 * 13 * 2 * 2)).isFalse();
    }
    @Test
    public void isPrimePower() {
        assertThat(Utils.isPrimePower(13 * 13)).isTrue();

    }

    @Property
    public void factorization(@ForAll("positiveLongs") long random) {
        StringBuilder builder = new StringBuilder();

        assertThat(Utils.primeFactorization(random)
            .reduce(1L, (l1, l2) -> {
                if (builder.length() > 0 ) {
                    builder.append(" " + TextUtils.TIMES + " ");
                }
                builder.append(l2);
                return l1 * l2;
            })).isEqualTo(random);
        log.info("{} = {} ({} {})", random, builder.toString(), Utils.isPrime((int) random), Utils.isPrimePower(random));
    }

    @Test
    void floorSqrt() {
    }

    @Test
    void gcd() {
        assertThat(Utils.gcd(10, 8)).isEqualTo(2);
        assertThat(Utils.gcd(8, 12)).isEqualTo(4);
        assertThat(Utils.gcd(-8, 12)).isEqualTo(4);
        assertThat(Utils.gcd(-8, -12)).isEqualTo(4);
        assertThat(Utils.gcd(8, -12)).isEqualTo(4);
    }

    @Provide
    Arbitrary<Long> positiveLongs() {
        return Arbitraries.randomValue(random -> random.nextLong(1_000_000_000));
    }

    @Test
    public void checkPower() {
        assertThat(Utils.checkPower(128, 2)).isEqualTo(7);
        assertThat(Utils.checkPower(127, 2)).isEqualTo(-1);
        assertThat(Utils.checkPower(128)).containsExactly(2, 7);
        assertThat(Utils.checkPower(Utils.positivePow(7, 3))).containsExactly(7, 3);
        assertThat(Utils.checkPower(13)).containsExactly(0, 0);
    }

    @Test
    public void checkPower2() {
        assertThat(Utils.checkPower(4)).containsExactly(2, 2);

    }

    @Test
    public void spiral() {
        Supplier<Stream<Integer>> streamSupplier = () -> IntStream.iterate(0, i -> i + 1).boxed();
    }

    @Test
    public void sqrt() {
        assertThat(Utils.sqrt(4)).isEqualTo(2);
        assertThat(Utils.floorSqrt(5)).isEqualTo(2);
        assertThatThrownBy(() -> Utils.sqrt(5)).isInstanceOf(MathException.class);
    }

    @Test
    public void factorial() {
        assertThat(Utils.factorial(4)).isEqualTo(24);
        assertThat(Utils.factorial(0)).isEqualTo(1);
        assertThatThrownBy(() -> Utils.factorial(-1)).isInstanceOf(InvalidFactorial.class);
    }


    public Duration time(Runnable run) {
        long nanoStart = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            run.run();
        }
        return Duration.ofNanos(System.nanoTime() - nanoStart);
    }



}
