/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.math;

import jakarta.validation.constraints.*;

import java.math.*;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.LongConsumer;
import java.util.stream.LongStream;
import java.util.stream.StreamSupport;

import org.meeuw.math.exceptions.*;

/**
 * @author Michiel Meeuwissen
 */
public final class Utils {

    private Utils() {}

    /**
     * 1000 decimals of pi
     */
    public static final String PI = "3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679821480865132823066470938446095505822317253594081284811174502841027019385211055596446229489549303819644288109756659334461284756482337867831652712019091456485669234603486104543266482133936072602491412737245870066063155881748815209209628292540917153643678925903600113305305488204665213841469519415116094330572703657595919530921861173819326117931051185480744623799627495673518857527248912279381830119491298336733624406566430860213949463952247371907021798609437027705392171762931767523846748184676694051320005681271452635608277857713427577896091736371787214684409012249534301465495853710507922796892589235420199561121290219608640344181598136297747713099605187072113499999983729780499510597317328160963185950244594553469083026425223082533446850352619311881710100031378387528865875332083814206171776691473035982534904287554687311595628638823537875937519577818577805321712268066130019278766111959092164201989";

    public static final String e = "2.7182818284590452353602874713526624977572470936999595749669676277240766303535475945713821785251664274274663919320030599218174135966290435729003342952605956307381323286279434907632338298807531952510190115738341879307021540891499348841675092447614606680822648001684774118537423454424371075390777449920695517027618386062613313845830007520449338265602976067371132007093287091274437470472306969772093101416928368190255151086574637721112523897844250569536967707854499699679468644549059879316368892300987931277361782154249992295763514822082698951936680331825288693984964651058209392398294887933203625094431173012381970684161403970198376793206832823764648042953118023287825098194558153017567173613320698112509961818815930416903515988885193458072738667385894228792284998920868058257492796104841984443634632449684875602336248270419786232090021609902353043699418491463140934317381436405462531520961836908887070167683964243781405927145635490613031072085103837505101157477041718986106873969655212671546889570350354";

    /**
     * Returns 10 to the power e, a utility in java.lang.Math for that lacks.
     * @param e the exponent
     * @return 10<sup>e</sup>
     */
    public static double pow10(int e) {
        return pow(10, e);
    }

    /**
     * Returns 2 to the power e, a utility in java.lang.Math for that lacks.
     * @param e the exponent
     * @return 2<sup>e</sup>
     */
    public static double pow2(int e) {
        return pow(2, e);
    }

    /**
     * Returns base to the power e, using integer operations only.
     *
     * @param base the base
     * @param e the exponent
     * @return base<sup>e</sup>
     */
    public static double pow(int base, int e) {
        double result = 1;
        while (e > 0) {
            result *= base;
            e--;
        }
        while (e < 0) {
            result /= base;
            e++;
        }
        assert e == 0;
        return result;
    }

    /**
     * Returns base to the power of an integer, a utility in java.lang.Math for that lacks.
     * @param base the base
     * @param e the exponent
     * @return base<sup>e</sup>
     */
    public static double pow(double base, int e) {
        double result = 1;
        if (base == 0) {
            return result;
        }
        // branching will make this slow

        // Math.pow(base, i); will probably perform better?
        while (e > 0) {
            result *= base;
            e--;
        }
        while (e < 0) {
            result /= base;
            e++;
        }
        assert e == 0;
        return result;
    }

    /**
     * Returns 10 to the power i, a utility in java.lang.Math for that lacks.
     *
     * @see #positivePow(long, int)
     * @param e  the exponent
     * @return 10<sup>e</sup>
     */
    public static long positivePow10(@Min(0) int e) {
        return positivePow(10, e);
    }

    /**
     * Returns base to the power i, a utility in java.lang.Math for that lacks.
     *
     * This is more exact than {@link Math#pow(double, double)} (though probably not faster)
     * @param base the base
     * @param e  the exponent
     * @return base<sup>e</sup>
     */
    public static long positivePow(@NotNull long base, @Min(0) int e) {
        if (e < 0) {
            throw new ReciprocalException(base +  "^" + e + " is impossible");
        }
        long result = 1;
        while (e > 0) {
            result *= base;
            e--;
        }
        return result;
    }

    public static BigInteger positivePow(@NotNull BigInteger base, @PositiveOrZero int e) {
        if (e < 0) {
            throw new ReciprocalException(base +  "^" + e + " is impossible");
        }
        return base.pow(e);
    }


    /**
     * A crude order of magnitude implemention
     *
     * This is like {@code Math.log10(Mat.abs(d))}
     * @param d a double
     * @return <sup>10</sup>log(d)
     */
    public static int log10(double d) {
        return (int) Math.floor(Math.log10(Math.abs(d)));
    }

    /**
     * A crude order of magnitude implemention
     *
     * This is like {@code Math.log10(Mat.abs(l))}
     * @param l a long
     * @return round(<sup>10</sup>log(d))
     */
    public static int log10(long l) {
        // it's hard to improve performance of Math.log.
        return (int) Math.log10(Math.abs(l));

        // this branchless version would just be 'approximately' correct, and it is only just a bit faster.

        //return (63 - Long.numberOfLeadingZeros(l)) >> 2;
    }

    /**
     * <p>A quick square root implementation fpr integers.</p>
     * <p>
     * Returns the biggest integer that is smaller or equals then the square root of an integer.
     * </p>
     * <p>
     * Using in  binary search algorithm.
     * </p>
     */
    public static long floorSqrt(final long radicand) {
        long proposal = 0;
        long m;
        long r = radicand + 1;

        while (proposal != r - 1) {
            m = (proposal + r) / 2;

            if (m * m <= radicand) {
                proposal = m;
            } else {
                r = m;
            }
        }
        return proposal;
    }

    /**
     * <p>A quick square root implementation for integers.</p>
     * @see #floorSqrt(long)
     * @throws NotASquareException If the given argument is not a square.
     */
    public static long sqrt(final long radicand) throws NotASquareException {
        long proposal = floorSqrt(radicand);
        if (proposal * proposal < radicand) {
            throw new NotASquareException(radicand + " is not a square");
        }
        return proposal;
    }

    /**
     *
     * @see #sqrt(long)
     */
    public static int sqrt(final int radicand) throws NotASquareException {
        return (int) sqrt((long) radicand);
    }


    /**
     * pow(2, &lt;this value&gt;) is an estimate of the 'uncertainty' in this double. More precision it simply cannot represent.
     * @param doubleValue a double value
     * @return the bit the power of 2 exponent that the least significant bit in the IEEE 754 representation of the double represents.
     */
    public static int leastSignificantBit(double doubleValue) {
        long exponent = (int) ((((Double.doubleToLongBits(doubleValue) & 0x7ff0000000000000L) >> 52) - 1023) - 51);
        return (int) exponent;
    }

    public static double uncertaintyForDouble(double doubleValue) {
        return pow2(leastSignificantBit(doubleValue));
    }

    public static BigDecimal uncertaintyForBigDecimal(BigDecimal bigDecimal, MathContext context) {
        int scale = bigDecimal.scale();
        if (scale <= 0) { // whole number
            return BigDecimal.ZERO;
        }
        if (bigDecimal.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0) {
            // an integer too
            return BigDecimal.ZERO;
        }

        return BigDecimal.ONE.scaleByPowerOfTen(-1 * context.getPrecision()).stripTrailingZeros();
    }


    /**
     * Simple prime test. 6k ± 1 optimization only. Not suitable for (very) large numbers.
     * @param n an integer to test for primeness
     * @return whether the argument is prime or not
     */
    public static boolean isPrime(int n) {
        if (n <= 3) {
            return n > 1;
        } else if (n % 2 == 0 || n % 3 == 0) {
            return false;
        }
        int i = 5;
        while (i * i <= n) {
            if (n % i == 0 || n % (i + 2) == 0) {
                return false;
            }
            i += 6;
        }
        return true;
    }

    public static LongStream primeFactorization(long number) {
        final long argument = Math.abs(number);
        if (argument <= 1) {
            return LongStream.empty();
        }
        Spliterator.OfLong spliterator = new Spliterators.AbstractLongSpliterator(Long.MAX_VALUE,
               Spliterator.ORDERED | Spliterator.IMMUTABLE | Spliterator.NONNULL) {
            long n = argument;
            boolean evens = true;
            long sqrt;
            long i;

            @Override
            public boolean tryAdvance(LongConsumer action) {
                if (evens) {
                    if (n % 2 == 0) {
                        action.accept(2);
                        n /= 2;
                        return true;
                    }
                    evens = false;
                    sqrt = (long) Math.sqrt(n);
                    i = 3;
                }
                while (i <= sqrt) {
                    if (n % i == 0) {
                        action.accept(i);
                        n /= i;
                        return true;
                    }
                    i += 2;
                }
                if (n > 2) {
                    action.accept(n);
                    n = 1;
                    return true;
                }
                return false;
            }

        };
        return StreamSupport.longStream(spliterator, false);
    }

    public static boolean isPrimePower(long argument) {
        return primeFactorization(argument)
            .distinct()
            .limit(2)
            .count() == 1;
    }

    /**
     * Checks whether n is a power of p.
     * Returns the exponent, or -1.
     */
    public static long checkPower(long n, final long p) {
        int k = 0;
        while (n > 1 && n % p == 0) {
            n /= p;
            k++;
        }
        if (n == 1) {
            return k;
        } else {
            return -1;
        }
    }

    /**
     * Finds out which power the argument can be.
     * @return a set of 2 (i, j), such that i^j. For smallest possible i. If there is no such combination, returns (0,0)
     */
    public static int[] checkPower(final long n) {
        long p = 2;
        while(p * p <= n) {
            long found = checkPower(n, p);
            if (found > 0) {
                return new int[]{(int) p, (int)found};
            }
            p++;
        }
        return new int[] {0, 0};
    }



    static long gcdByEuclidsAlgorithm(final long n1, final long n2) {
        if (n2 == 0) {
            return n1;
        }
        return gcdByEuclidsAlgorithm(n2, Math.abs(n1 % n2));
    }

    public static long gcd(long n1, long n2) {
        return gcdByEuclidsAlgorithm(n1, n2);
    }


    public static double max(double... values) {
        double max = Double.NEGATIVE_INFINITY;
        for (double tmp : values) {
            max = Math.max(max, tmp);
        }
        return max;
    }

    /**
     * @see Math#round(double)
     * @throws IllegalArgumentException if {@code value} is {@code NaN}
     */
    public static long round(double value) {
        if (Double.isNaN(value)) {
            throw new IllegalArgumentException("Cannot round " + value + " to a long");
        }
        return Math.round(value);
    }

    public static long factorial(@Min(0) int value) {
        if (value < 0) {
            throw new InvalidFactorial("Cannot take factorial of negative");
        }
        long i = 1;
        long answer = 1;
        while(++i <= value) {
            answer *= i;
        }
        return answer;
    }

}
