package org.meeuw.test.math;

import lombok.extern.log4j.Log4j2;

import java.math.*;

import net.jqwik.api.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import org.meeuw.math.*;
import org.meeuw.math.exceptions.*;
import org.meeuw.math.text.TextUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.meeuw.math.IntegerUtils.MINUS_ONE;

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
        assertThat(DigitUtils.fromDigits(1, 2, 3)).isEqualTo(123);

        assertThatThrownBy(() -> DigitUtils.fromDigitsInBase(4, 1, 2, 4)).isInstanceOf(IllegalArgumentException.class);

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
    public void nextPrime() {
        assertThat(IntegerUtils.nextPrime(1)).isEqualTo(2);
        assertThat(IntegerUtils.nextPrime(13)).isEqualTo(17);
        assertThat(IntegerUtils.nextPrime(22)).isEqualTo(23);

    }



    @Test
    public void positivePower10() {
        assertThatThrownBy(() -> IntegerUtils.positivePow10(-1)).isInstanceOf(IllegalPowerException.class);
        assertThat(IntegerUtils.positivePow10(2)).isEqualTo(100);
    }

    @Test
    public void positivePower10TooBig() {
        assertThatThrownBy(() -> IntegerUtils.positivePow10(19)).isInstanceOf(IllegalPowerException.class);
        assertThat(IntegerUtils.positivePow10(18)).isEqualTo(1000000000000000000L);
    }

    @Test
    public void positivePowerTooBig() {
        assertThat(IntegerUtils.positivePow(11, 18)).isEqualTo(5_559_917_313_492_231_481L);
        assertThatThrownBy( () -> IntegerUtils.positivePow(11, 19)).isInstanceOf(IllegalPowerException.class);

    }


    @Test
    public void positivePower() {
        assertThatThrownBy(() -> IntegerUtils.positivePow(BigInteger.TEN, -1)).isInstanceOf(IllegalPowerException.class);
        assertThat(IntegerUtils.positivePow(BigInteger.TEN, 2)).isEqualTo(BigInteger.valueOf(100));
    }

    @Test
    public void pow() {
        assertThat(IntegerUtils.pow(BigInteger.ZERO, BigInteger.ZERO)).isEqualTo(BigInteger.ONE);
        assertThat(IntegerUtils.pow(BigInteger.ZERO, BigInteger.ONE)).isEqualTo(BigInteger.ZERO);
        assertThat(IntegerUtils.pow(BigInteger.ZERO, BigInteger.TWO)).isEqualTo(BigInteger.ZERO);
        assertThatThrownBy(() -> IntegerUtils.pow(BigInteger.ZERO, BigInteger.valueOf(-1)))
            .isInstanceOf(IllegalPowerException.class)
        ;
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
       assertThatThrownBy(() -> IntegerUtils.sqrt(5)).isInstanceOf(MathException.class);
   }
   @ParameterizedTest
   @ValueSource(longs = {0, 5, 2564287193236147620L, 9223372036854775806L, Long.MAX_VALUE })
   public void floorSqrt(long value) {
       assertThat(IntegerUtils.floorSqrt(value)).isEqualTo(BigInteger.valueOf(value).sqrt().longValue());
   }
   @Test
   public void floorSqrtNegative() {
       assertThatThrownBy(() -> IntegerUtils.floorSqrt(-1)).isInstanceOf(IllegalSqrtException.class);
   }

   @Test
   public void timeTest() {
       {// warm up
           for (int i = 0; i < 10000000; i++) {
               BigInteger.valueOf(9223372036854775806L - i).sqrt().longValueExact();
           }
       }
       {
           long start = System.currentTimeMillis();
           for (int i = 0; i < 10000000; i++) {
               BigInteger.valueOf(9223372036854775806L - i).sqrt().longValueExact();
           }

           log.info("Time for 1 million floorSqrt calls: {} ms", System.currentTimeMillis() - start);
       }
       {
           long start = System.currentTimeMillis();
           for (int i = 0; i < 10000000; i++) {
               IntegerUtils.floorSqrt(9223372036854775806L - i);
           }
           log.info("Time for 1 million floorSqrt calls (using IntegerUtils): {} ms", System.currentTimeMillis() - start);
       }


   }

    @Test
    public void factorial() {
        assertThat(IntegerUtils.factorial(4)).isEqualTo(24);
        assertThat(IntegerUtils.factorial(0)).isEqualTo(1);
        assertThatThrownBy(() -> IntegerUtils.factorial(-1)).isInstanceOf(InvalidFactorial.class);
    }

    @Test
    public void bigfactorial() {
        assertThat(IntegerUtils.bigIntegerFactorial(BigInteger.valueOf(4L))).isEqualTo(24);
        assertThat(IntegerUtils.bigIntegerFactorial(BigInteger.ZERO)).isEqualTo(1);
        assertThatThrownBy(() -> IntegerUtils.bigIntegerFactorial(MINUS_ONE)).isInstanceOf(InvalidFactorial.class);
    }

    @Test
    public void subfactorial() {
        assertThat(IntegerUtils.bigIntegerSubfactorial(BigInteger.valueOf(4L))).isEqualTo(9);
        assertThat(IntegerUtils.bigIntegerSubfactorial(BigInteger.valueOf(9L))).isEqualTo(133_496L);
        assertThat(IntegerUtils.bigIntegerSubfactorial(BigInteger.ZERO)).isEqualTo(1);
        assertThatThrownBy(() -> IntegerUtils.bigIntegerSubfactorial(MINUS_ONE)).isInstanceOf(InvalidFactorial.class);
    }

    @Test
    public void doubleFactorial() {
        assertThat(IntegerUtils.bigIntegerDoubleFactorial(BigInteger.valueOf(9L))).isEqualTo(945);
        assertThat(IntegerUtils.bigIntegerDoubleFactorial(BigInteger.valueOf(8L))).isEqualTo(384);

        assertThat(IntegerUtils.bigIntegerDoubleFactorial(BigInteger.ZERO)).isEqualTo(1);
        assertThatThrownBy(() -> IntegerUtils.bigIntegerDoubleFactorial(BigInteger.valueOf(-1))).isInstanceOf(InvalidFactorial.class);
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

    @Test
    public void max() {
        assertThat(IntegerUtils.max(1, 2, 3)).isEqualTo(3);
        assertThat(IntegerUtils.max(1, 2, 3, 4, 5)).isEqualTo(5);
        assertThat(IntegerUtils.max(1, 2, -3)).isEqualTo(2);
        assertThat(IntegerUtils.max(-1, -2, -3)).isEqualTo(-1);
    }


    @ParameterizedTest
    @ValueSource(ints = {0, 1, 4, 9, 16, 25})
    public void isSquare(int i) {
        assertThat(IntegerUtils.isSquare(i)).isTrue();
        assertThat(IntegerUtils.isSquare(BigInteger.valueOf(i))).isTrue();
    }
    @ParameterizedTest
    @ValueSource(ints = {-25, -24, 27, 27})
    public void isNotSquare(int i) {
        assertThat(IntegerUtils.isSquare(i)).isFalse();
        assertThat(IntegerUtils.isSquare(BigInteger.valueOf(i))).isFalse();
    }

    @Provide
    Arbitrary<Long> positiveLongs() {
        return Arbitraries.randomValue(random -> (long) random.nextInt(1_000_000_000));
    }



}
