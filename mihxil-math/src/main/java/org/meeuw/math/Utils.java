package org.meeuw.math;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author Michiel Meeuwissen
 */
public class Utils {

    private Utils() {}

    /**
     * 1000 decimals of pi
     */
    public static String PI = "3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679821480865132823066470938446095505822317253594081284811174502841027019385211055596446229489549303819644288109756659334461284756482337867831652712019091456485669234603486104543266482133936072602491412737245870066063155881748815209209628292540917153643678925903600113305305488204665213841469519415116094330572703657595919530921861173819326117931051185480744623799627495673518857527248912279381830119491298336733624406566430860213949463952247371907021798609437027705392171762931767523846748184676694051320005681271452635608277857713427577896091736371787214684409012249534301465495853710507922796892589235420199561121290219608640344181598136297747713099605187072113499999983729780499510597317328160963185950244594553469083026425223082533446850352619311881710100031378387528865875332083814206171776691473035982534904287554687311595628638823537875937519577818577805321712268066130019278766111959092164201989";

    public static String e = "2.7182818284590452353602874713526624977572470936999595749669676277240766303535475945713821785251664274274663919320030599218174135966290435729003342952605956307381323286279434907632338298807531952510190115738341879307021540891499348841675092447614606680822648001684774118537423454424371075390777449920695517027618386062613313845830007520449338265602976067371132007093287091274437470472306969772093101416928368190255151086574637721112523897844250569536967707854499699679468644549059879316368892300987931277361782154249992295763514822082698951936680331825288693984964651058209392398294887933203625094431173012381970684161403970198376793206832823764648042953118023287825098194558153017567173613320698112509961818815930416903515988885193458072738667385894228792284998920868058257492796104841984443634632449684875602336248270419786232090021609902353043699418491463140934317381436405462531520961836908887070167683964243781405927145635490613031072085103837505101157477041718986106873969655212671546889570350354";

    /**
     * Returns 10 to the power i, a utility in java.lang.Math for that lacks.
     */
    public static double pow10(int i) {
        double result = 1;
        while (i > 0) {
            result *= 10;
            i--;
        }
        while (i < 0) {
            result /= 10;
            i++;
        }
        assert i == 0;
        return result;
    }

     /**
     * Returns 10 to the power i, a utility in java.lang.Math for that lacks.
     */
     public static long positivePow10(@Min(0) int i) {
         return positivePow(10, i);

     }

     /**
     * Returns base to the power i, a utility in java.lang.Math for that lacks.
     */
     public static long positivePow(@NotNull long base, @Min(0) int i) {
        long result = 1;
        while (i > 0) {
            result *= base;
            i--;
        }
        if (i < 0) {
            throw new IllegalArgumentException();
        }
        return result;
    }


     /**
     * A crude order of magnitude implemention
     */
    public static int log10(double d) {
        d = Math.abs(d);
        int result = 0;
        while (d >= 1) {
            d /= 10;
            result++;
        }
        while (d > 0 && d < 0.1) {
            d *= 10;
            result--;
        }
        return result;
    }

    public static int log10(long d) {
        d = Math.abs(d);
        int result = 0;
        while (d >= 1) {
            d /= 10;
            result++;
        }
        return result;
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

    public static Stream<int[]> stream(int degree) {
        return Stream.iterate(new State(degree), State::next)
            .map(State::array);
    }

    static class State {
        final int degree;
        final int[] counters;
        int max = 0;
        int[] next;

        State(int degree) {
            this.degree = degree;
            counters = new int[degree];
            makeNext();
        }
        int[] array() {
            return next;
        }

        void makeNext() {
            this.next = new int[degree];
            int offset = max / 2;
            for (int i = 0; i < degree; i++) {
                this.next[i] = counters[i] - offset;
            }
        }

        public State next() {
            max = inc(counters, max);
            makeNext();
            if (max() < max) {
                return next();
            }
            return this;
        }

        private int max() {
            int m = 0;
            for (int c : next) {
                int abs = Math.abs(c);
                if (abs > m) {
                    m = abs;
                }
            }
            return m * 2;
        }
    }

    /**
     * Fills the array as if it is kind of a number system (with infinite base, but fixed number of digits)
     * Also we fill from the left.
     *
     * The idea is to first produce values with low base, and if we filled those, then increase the base with 2 (we want to produce
     * negative values, and afterwards shift base /2).
     *
     * This will produce duplicates, which are filtered out in {@link State}
     *
     */
    static int inc(int[] counters, int base) {
        return inc(counters, 0, base);
    }

    private static int inc(int[] counters, int index, int base) {
        counters[index]++;
        if (counters[index] > base) {
            counters[index] = 0;
            if (index + 1 < counters.length) {
                return inc(counters, index + 1, base);
            } else {
                counters[0] = base;
                return inc(counters, 0, base + 2);
            }
        }
        return base;
    }

}
