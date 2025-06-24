package org.meeuw.math;

import jakarta.validation.constraints.*;

import java.math.BigInteger;
import java.util.*;
import java.util.function.LongConsumer;
import java.util.stream.LongStream;
import java.util.stream.StreamSupport;

import org.checkerframework.checker.index.qual.NonNegative;
import org.meeuw.configuration.ConfigurationService;
import org.meeuw.math.exceptions.*;
import org.meeuw.math.numbers.MathContextConfiguration;
import org.meeuw.math.text.TextUtils;

/**
 * @since 0.9
 */
public final class IntegerUtils {

    /**
     * {@link BigInteger#TWO}, but for java 8.
     */
    public static final BigInteger TWO = BigInteger.valueOf(2);

    public static final BigInteger MINUS_TWO = BigInteger.valueOf(-2);

    public static final BigInteger MINUS_ONE = BigInteger.valueOf(-1);

    private IntegerUtils() {}

    /**
     * Returns 10 to the power i, a utility in java.lang.Math for that lacks.
     *
     * @see #positivePow(long, int)
     * @param e  the exponent
     * @return 10<sup>e</sup>
     */
    public static long positivePow10(@PositiveOrZero int e) {
        return positivePow(10, e);
    }


    /**
     * Returns base to the power i, a utility in java.lang.Math for that lacks.
     * <p>
     * This is more exact than {@link Math#pow(double, double)} (though probably not faster)
     * @param base the base
     * @param e  the exponent
     * @return base<sup>e</sup>
     */
    public static long positivePow(@NotNull long base, @PositiveOrZero int e) {
        if (e < 0) {
            throw new IllegalPowerException("Cannot raise to negative", base + TextUtils.superscript( e));
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
            throw new IllegalPowerException("Cannot raise to negative",  base +  TextUtils.superscript(e));
        }
        return base.pow(e);
    }

    /**
     * A crude order of magnitude implementation
     * <p>
     * This is like {@code Math.log10(Mat.abs(l))}
     *
     * @param l a long
     * @return round(<sup>10</sup>log(d))
     */
    public static int log10(long l) {
        // it's hard to improve performance of Math.log.
        return (int) Math.log10(Math.abs(l));

        // this brancheless version would just be 'approximately' correct, and it is only just a bit faster.

        //return (63 - Long.numberOfLeadingZeros(l)) >> 2;
    }

    public static final long MAX_SQUARABLE = BigInteger.valueOf(Long.MAX_VALUE ).sqrt().longValueExact();

    /**
     *
     * <p>A quick square root implementation for integers.</p>
     * <p>
     * Returns the biggest integer that is smaller than or equal to the square root of an integer.
     * </p>
     * <p>
     * Using a binary search algorithm.
     * </p>
     */
    public static long floorSqrt(@NonNegative final long radicand) {
        // The optimizations of BigInteger are very good, so even though we don't need big numbers, it is still much faster than anything I can come up with quickly
        if (radicand < 0) {
            throw new IllegalSqrtException("Cannot take square root of negative number", "" + radicand);
        }
        return BigInteger.valueOf(radicand).sqrt().longValueExact();


    }

    /**
     * <p>A quick square root implementation for integers.</p>
     * @see #floorSqrt(long)
     * @throws NotASquareException If the given argument is not a square.
     */
    public static long sqrt(@NonNegative final long radicand) throws NotASquareException {
        long proposal = floorSqrt(radicand);
        if (proposal * proposal < radicand) {
            throw new NotASquareException(radicand + " is not a square");
        }
        return proposal;
    }

     public static boolean isSquare(final long radicand) {
        if (radicand < 0) {
            return false;
        }
        long proposal = floorSqrt(radicand);
        if (proposal * proposal < radicand) {
            return false;
        }
        return true;
    }
    public static boolean isSquare(final BigInteger radicand) {
        if (radicand.signum() < 0) {
            return false;
        }
        BigInteger[] proposal = radicand.sqrtAndRemainder();
        return proposal[1].equals(BigInteger.ZERO);
    }

    /**
     *
     * @see #sqrt(long)
     */
    public static int sqrt(final int radicand) throws NotASquareException {
        return (int) sqrt((long) radicand);
    }

    /**
     * Simple prime test. 6k ± 1 optimization only. Not suitable for (very) large numbers.
     * @param n an integer to test for primeness
     * @return whether the argument is prime or not
     */
    public static boolean isPrime(long n) {
        if (n <= 3) {
            return n > 1;
        } else if (n % 2 == 0 || n % 3 == 0) {
            return false;
        }
        long i = 5;
        while (i * i <= n) {
            if (n % i == 0 || n % (i + 2) == 0) {
                return false;
            }
            i += 6;
        }
        return true;
    }

    public static long nextPrime(long n) {
        if (n < 2) {
            return 2;
        }
        if (n % 2 == 0) {
            n++;
        } else {
            n += 2;
        }
        while (!isPrime(n)) {
            n += 2;
        }
        return n;
    }

    /**
     * Returns a stream of the prime factors of the given number.
     * <p>
     * The stream is ordered and contains each prime factor as many times as it occurs in the factorization.
     * </p>
     * <p>
     * For example, {@code primeFactorization(12)} will return {@code 2, 2, 3}.
     * </p>
     *
     * @param number the number to factorize
     * @return a stream of the prime factors
     */
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
     * Checks whether {@code n} is a power of {@code p}.
     * @return the exponent, or {@code -1} if it's not.
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
     * Finds out which integer power the argument can be.
     * @return an array of 2 ints {@code (i, j)}, such that {@code i^j == n},  for smallest possible {@code i}. If there is no such combination, then {@code (0,0)}
     * @param n The number to check
     *
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

    /**
     * Returns the greatest common divisor of two long integers.
     * <p>
     * Uses <a href="https://en.wikipedia.org/wiki/Euclidean_algorithm">Euclid's algorithm</a>.

     * @param n1 the first integer
     * @param n2 the second integer
     * @return the greatest common divisor of {@code n1} and {@code n2}
     */
    public static long gcd(long n1, long n2) {
        return gcdByEuclidsAlgorithm(n1, n2);
    }

     /**
     * Returns the greatest common divisor of two integers.
     * <p>
     * Uses <a href="https://en.wikipedia.org/wiki/Euclidean_algorithm">Euclid's algorithm</a>.
     *
     * @param n1 the first integer
     * @param n2 the second integer
     * @return the greatest common divisor of {@code n1} and {@code n2}
     */
    public static int gcd(int n1, int n2) {
        return (int) gcdByEuclidsAlgorithm(n1, n2);
    }


    /**
     * Straightforward implementation of the factorial function.
     * @param value the value to take the factorial of
     * @return the factorial: {@code value!}
    */
    public static long factorial(@Min(0) int value) {
        if (value < 0) {
            throw new InvalidFactorial("Cannot take factorial of negative", Integer.toString(value));
        }
        long i = 1;
        long answer = 1;
        while(++i <= value) {
            answer *= i;
        }
        return answer;
    }


    public static BigInteger bigIntegerFactorial(BigInteger value)  {
        if (value.signum() == -1) {
            throw new InvalidFactorial("Cannot take factorial of negative integer", value.toString());
        }

        BigInteger product = BigInteger.ONE;
        for (BigInteger i = BigInteger.ONE; i.compareTo(value) <= 0; i = i.add(BigInteger.ONE)) {
            product = product.multiply(i);
        }
        return product;
    }
    /**
     * Using a cache for already calculated sub factorials, will because of the recursive nature of the calculation speed
     * up things a lot for bigger values.
     */
    private static final Map<BigInteger, BigInteger> SUBFACT_CACHE = new HashMap<>();


    public static  BigInteger bigIntegerSubfactorial(BigInteger value) {
        if (value.signum() == -1) {
            throw new InvalidFactorial("Cannot take subfactorial of negative integer", value.toString());
        }
        return bigIntegerSubfactorial(value, SUBFACT_CACHE);
    }

    public static BigInteger pow(BigInteger base, BigInteger e) {

       if (base.equals(BigInteger.ZERO)) {
           if (e.signum() == 0) {
               return BigInteger.ONE;
           }
           if (e.signum() < 0) {
               throw new IllegalPowerException("Cannot raise to negative power", base + TextUtils.superscript(e.toString()));
           }

           return BigInteger.ZERO;
       }
       BigInteger result = BigInteger.ONE;
       // branching will make this slow
       while (e.signum() > 0) {
           result = result.multiply(base);
           if (result.bitLength() > ConfigurationService.getConfigurationAspect(MathContextConfiguration.class).getMaxBits()) {
               throw new OverflowException("Too big", base + TextUtils.superscript(e.toString()));
           }
           e = e.add(BigInteger.ONE.negate());
       }
       return result;
   }

    private static synchronized  BigInteger bigIntegerSubfactorial(BigInteger n, Map<BigInteger, BigInteger> answers) {
        if (n.equals(BigInteger.ZERO)) {
            return BigInteger.ONE;
        }
        if (n.equals(BigInteger.ONE)) {
            return BigInteger.ZERO;
        }
        BigInteger nMinusOne = n.add(MINUS_ONE);
        BigInteger nMinusOneSub = answers.get(nMinusOne);
        if (nMinusOneSub == null) {
            nMinusOneSub =  bigIntegerSubfactorial(nMinusOne, answers);
            answers.put(nMinusOne, nMinusOneSub);
        }
        BigInteger nMinusTwo = n.add(MINUS_TWO);
        BigInteger nMinusTwoSub = answers.get(nMinusTwo);
        if (nMinusTwoSub == null) {
             nMinusTwoSub = bigIntegerSubfactorial(nMinusTwo, answers);
             answers.put(nMinusTwo, nMinusTwoSub);
        }
        return nMinusOne.multiply(nMinusOneSub.add(nMinusTwoSub));
    }

    public static  BigInteger bigIntegerDoubleFactorial(BigInteger value) {
        if (value.signum() == -1) {
            throw new InvalidFactorial("Cannot take subfactorial of negative integer", value.toString());
        }
        BigInteger product = BigInteger.ONE;
        while(value.compareTo(BigInteger.ONE) > 0) {
            product = product.multiply(value);
            value = value.add(MINUS_TWO);
        }
        return product;

    }

    static long gcdByEuclidsAlgorithm(final long n1, final long n2) {
        if (n2 == 0) {
            return n1;
        }
        return gcdByEuclidsAlgorithm(n2, Math.abs(n1 % n2));
    }


    /**
     * @see ComparableUtils#max(Comparable, Comparable[])
     */
    public static int max(int value, int... values) {
        int result = value;
        for (int v : values) {
            if (v > result) {
                result = v;
            }
        }
        return result;
    }

}
