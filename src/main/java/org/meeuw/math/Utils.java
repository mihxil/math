package org.meeuw.math;

import java.text.NumberFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

import javax.validation.constraints.Min;

/**
 * @author Michiel Meeuwissen
 */
public class Utils {
    public static final String TIMES = "\u00B7";  /* "·10' */
    public static final String TIMES_10 = TIMES + "10";  /* "·10' */
    /**
     * Returns an integer in 'superscript' notation, using unicode.
     */
    public static String superscript(int i) {
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
            int j = i % 10;
            i /= 10;
            bul.insert(0, SUPERSCRIPTS[j]);
        }
        if (minus) bul.insert(0, "\u207B");

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
        long result = 1;
        while (i > 0) {
            result *= 10;
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


    public static String scientificNotation(double meanDouble, int minimumExponent) {
        SplitNumber mean = SplitNumber.split(meanDouble);

        // For numbers close to 1, we don't use scientific notation.
        if (Math.abs(mean.exponent) < minimumExponent) {
            double pow = Utils.pow10(mean.exponent);
            mean.exponent = 0;
            mean.coefficient *= pow;
        }
        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        nf.setGroupingUsed(false);
        boolean useE = mean.exponent != 0;

        return
            nf.format(mean.coefficient)
            +
            (useE ? TIMES_10 + Utils.superscript(mean.exponent)  : "");

    }

     /**
     * Represents the mean value in a scientific notation (using unicode characters).
     * The value of the standard deviation is used to determin how many digits can sensibly be shown.
     */
     public static String scientificNotationWithUncertaintity(double meanDouble, double stdDouble, int minimumExponent) {
        SplitNumber std  = SplitNumber.split(stdDouble);
        SplitNumber mean = SplitNumber.split(meanDouble);
        boolean largeError = Math.abs(stdDouble) > Math.abs(meanDouble);

        // use difference of order of magnitude of std to determin how mean digits of the mean are
        // relevant
        int magnitudeDifference = mean.exponent - std.exponent;
        //System.out.println("Md: " + mean + " " + std + magnitudeDifference);
        int meanDigits = Math.max(0, magnitudeDifference) + 1;


        // for std starting with '1' we allow an extra digit.
        if (std.coefficient < 2 && std.coefficient > 0) {
            //System.out.println("Extra digit");
            meanDigits++;
        }

        //System.out.println("number of relevant digits " + meanDigits + " (" + std + ")");


        // The exponent of the mean is leading, so we simply justify the 'coefficient' of std to
        // match the exponent of mean.
        std.coefficient /= Utils.pow10(magnitudeDifference);


        // For numbers close to 1, we don't use scientific notation.
        if (Math.abs(mean.exponent) < minimumExponent ||
            // neither do we do that if the precission is so high, that we'd show the complete
            // number anyway
            (mean.exponent > 0 && meanDigits > mean.exponent)) {

            double pow = Utils.pow10(mean.exponent);
            mean.exponent = 0;
            mean.coefficient *= pow;
            std.coefficient  *= pow;

        }

        boolean useE = mean.exponent != 0;

        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        int fd = meanDigits -  Utils.log10(largeError ? std.coefficient :  mean.coefficient);
        //System.out.println(meanDigits + " -> " + mean + " -> " + fd);
        nf.setMaximumFractionDigits(fd);
        nf.setMinimumFractionDigits(fd);
        nf.setGroupingUsed(false);
        return
            (useE ? "(" : "") + valueAndError(nf.format(mean.coefficient), nf.format(std.coefficient))
            +
            (useE ? (")" + TIMES_10 + superscript(mean.exponent)) : "");
    }

    public static String valueAndError(String value, String error) {
         return value +  " \u00B1 " + error;
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

    public static <T extends Enum<T>> String toString(T[] values, int[] basic) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < basic.length; i++) {
            int b = basic[i];
            if (b != 0) {
                builder.append(values[i].name());
                if (b != 1) {
                    builder.append(Utils.superscript(b));
                }
            }
        }
        return builder.toString();
    }



}
