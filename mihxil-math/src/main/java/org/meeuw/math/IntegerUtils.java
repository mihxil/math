package org.meeuw.math;

import jakarta.validation.constraints.*;

import java.math.BigInteger;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.LongConsumer;
import java.util.stream.LongStream;
import java.util.stream.StreamSupport;

import org.meeuw.math.exceptions.*;
import org.meeuw.math.text.TextUtils;

/**
 * @since 0.9
 */
public class IntegerUtils {

    private IntegerUtils() {}

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
     * <p>
     * This is more exact than {@link Math#pow(double, double)} (though probably not faster)
     * @param base the base
     * @param e  the exponent
     * @return base<sup>e</sup>
     */
    public static long positivePow(@NotNull long base, @Min(0) int e) {
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
            throw new IllegalPowerException("Cannot rais to negative",  base +  TextUtils.superscript(e));
        }
        return base.pow(e);
    }

    /**
     * A crude order of magnitude implemention
     * <p>
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

    public static long gcd(long n1, long n2) {
        return gcdByEuclidsAlgorithm(n1, n2);
    }

    public static int gcd(int n1, int n2) {
        return (int) gcdByEuclidsAlgorithm(n1, n2);
    }


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

    static long gcdByEuclidsAlgorithm(final long n1, final long n2) {
        if (n2 == 0) {
            return n1;
        }
        return gcdByEuclidsAlgorithm(n2, Math.abs(n1 % n2));
    }

    public static long fromDigitsInBase(final int base, @Min(0) int... digits) {
        long result = 0;
        for (long d : digits) {
            if (d >= base) {
                throw new IllegalArgumentException();
            }
            result = base * result + d ;
        }
        return result;

    }

    public static long fromDigits(@Min(0) @Max(9) int... digits) {
        return fromDigitsInBase(10, digits);
    }
}
