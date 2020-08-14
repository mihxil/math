package org.meeuw.math.text;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.meeuw.math.Utils;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class TextUtils {

    private TextUtils() {
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
                bul.append((char) c);
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

    public static String format(Instant instant, ChronoUnit order) {
         return format(ZoneId.systemDefault(), instant, order);
    }

    public static String format(ZoneId zoneId, Instant instant, ChronoUnit order) {
         Instant toFormat = Utils.round(instant, order);
         if (order.ordinal() < ChronoUnit.DAYS.ordinal()) {
             return DateTimeFormatter.ISO_DATE_TIME.format(toFormat.atZone(zoneId).toLocalDateTime());
         } else {
             return DateTimeFormatter.ISO_DATE.format(toFormat.atZone(zoneId).toLocalDate());
         }
    }
}
