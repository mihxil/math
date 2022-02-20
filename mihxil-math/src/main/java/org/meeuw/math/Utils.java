package org.meeuw.math;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Supplier;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.meeuw.math.exceptions.NotASquareException;
import org.meeuw.math.exceptions.ReciprocalException;
import org.meeuw.math.text.TextUtils;

import static java.util.Collections.unmodifiableNavigableSet;

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

    public static long sqrt(final long radicand) {
        long proposal = floorSqrt(radicand);
        if (proposal * proposal < radicand) {
            throw new NotASquareException(radicand + " is not a square");
        }
        return proposal;
    }


    public static ChronoUnit orderOfMagnitude(Duration stddev) {
        ChronoUnit order = ChronoUnit.DAYS;
        if (stddev.toDays() < 2) {
            order = ChronoUnit.HOURS;
            if (stddev.toHours() < 2) {
                order = ChronoUnit.MINUTES;
                if (stddev.toMinutes() < 2) {
                    order = ChronoUnit.SECONDS;
                    if (stddev.toMillis() < 2000) {
                        order = ChronoUnit.MILLIS;
                    }
                }
            }
        }
        return order;
    }

    public static Duration round(Duration duration, ChronoUnit order) {
        switch(order) {
            case DAYS:
                //return Duration.ofDays(Math.round(duration.getSeconds() / 86400f));
            case HOURS:
                return Duration.ofHours(Math.round(duration.getSeconds() / 3600f));
            case MINUTES:
                return Duration.ofMinutes(Math.round(duration.getSeconds() / 60f));
            case SECONDS:
                return Duration.ofSeconds(duration.toMillis() / 1000);
            case MILLIS:
                return Duration.ofMillis(duration.toMillis());
            default:
                throw new IllegalArgumentException();
        }
    }
    public static Instant round(Instant instant, ChronoUnit order) {
         ChronoUnit trunc = ChronoUnit.values()[Math.max(ChronoUnit.MILLIS.ordinal(), Math.min(ChronoUnit.DAYS.ordinal(), order.ordinal() - 1))];
         if (trunc == ChronoUnit.HALF_DAYS) {
             trunc = ChronoUnit.HOURS;
         }
         return instant.truncatedTo(trunc);
    }


    /**
     * Given an array of enums, and a array of integers, interpret the second array as exponents for the first one, and
     * create a string representation of that using superscript notation.
     * @param <T> the type of the enums
     * @param values the enum values
     * @param exponents the associated exponents
     * @return a string
     */
    public static <T extends Enum<T>> String toString(T[] values, int[] exponents) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < exponents.length; i++) {
            int b = exponents[i];
            if (b != 0) {
                builder.append(values[i].toString());
                if (b != 1) {
                    builder.append(TextUtils.superscript(b));
                }
            }
        }
        return builder.toString();
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



    static long gcdByEuclidsAlgorithm(long n1, long n2) {
        if (n2 == 0) {
            return n1;
        }
        return gcdByEuclidsAlgorithm(n2, n1 % n2);
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

    @SafeVarargs
    public static <E extends Comparable<E>> NavigableSet<E> navigableSet(E...ops) {
        return unmodifiableNavigableSet(new TreeSet<>(Arrays.asList(ops)));
    }

    public static <V> Supplier<V> memoize(final Supplier<V> wrapped){
        return new Supplier<V>() {
            transient volatile boolean initialized;
            transient V value;


            @Override
            public V get() {
                if (!initialized) {
                    synchronized (this) {
                        if (!initialized) {
                            V t = wrapped.get();
                            value = t;
                            initialized = true;
                            return t;
                        }
                    }
                    return null;
                }
                return value;
            }
        };
    }

}
