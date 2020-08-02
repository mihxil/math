package org.meeuw.math;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * @author Michiel Meeuwissen
 */
public class Utils {

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

    public static String format(Instant instant, ChronoUnit order) {
         return format(ZoneId.systemDefault(), instant, order);
    }



    public static String format(ZoneId zoneId, Instant instant, ChronoUnit order) {
         Instant toFormat = round(instant, order);
         if (order.ordinal() < ChronoUnit.DAYS.ordinal()) {
             return DateTimeFormatter.ISO_DATE_TIME.format(toFormat.atZone(zoneId).toLocalDateTime());
         } else {
             return DateTimeFormatter.ISO_DATE.format(toFormat.atZone(zoneId).toLocalDate());
         }
    }


    /**
     * Returns an integer in 'superscript' notation, using unicode.
     */
    public static String superscript(long i) {
        StringBuilder bul = new StringBuilder();
        boolean minus = false;
        if (i < 0) {
            minus = true;
            i = -1 * i;
        }
        if (i == 0) {
            bul.insert(0, SUPERSCRIPTS[0]);
        }
        while (i > 0) {
            int j = (int) (i % 10);
            i /= 10;
            bul.insert(0, SUPERSCRIPTS[j]);
        }
        if (minus) bul.insert(0, "\u207B");

        return bul.toString();
    }

      /**
     * Returns an integer in 'superscript' notation, using unicode.
     */
    public static String subscript(long i) {
        StringBuilder bul = new StringBuilder();
        boolean minus = false;
        if (i < 0) {
            minus = true;
            i = -1 * i;
        }
        if (i == 0) {
            bul.insert(0, SUBSCRIPTS[0]);
        }
        while (i > 0) {
            int j = (int) (i % 10);
            i /= 10;
            bul.insert(0, SUBSCRIPTS[j]);
        }
        if (minus) bul.insert(0, "\u208B");

        return bul.toString();
    }

    public static String subscript(CharSequence i) {
        return script(i, '\u208B', SUBSCRIPTS);
    }
    public static String superscript(CharSequence i) {
        return script(i, '\u207B', SUPERSCRIPTS);
    }

    private static String script(CharSequence i, char minus, char[] digits) {
        StringBuilder bul = new StringBuilder();

        i.chars().forEach(c -> {
            if (Character.isDigit(c)) {
                bul.append(digits[c - '0']);
            } else if (c == '-') {
                bul.append('\u208B');
            } else {
                bul.append(c);
            }
        });
        return bul.toString();
    }


    private static final char[] SUPERSCRIPTS = new char[] {
            0x2070,
            0x00B9,
            0x00B2,
            0x00B3,
            0x2074,
            0x2075,
            0x2076,
            0x2077,
            0x2078,
            0x2079
    };
     private static final char[] SUBSCRIPTS = new char[] {
             0x2080,
             0x2081,
             0x2082,
             0x2083,
             0x2084,
             0x2085,
             0x2086,
             0x2087,
             0x2088,
             0x2089
    };
}
