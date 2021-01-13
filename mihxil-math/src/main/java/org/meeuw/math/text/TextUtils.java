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
public final class TextUtils {

    private TextUtils() {
    }

    /**
     * Returns an integer in 'superscript' notation, using unicode.
     */
    public static String superscript(long i) {
        return script(i, SUPER_MINUS, SUPERSCRIPTS);
    }

    /**
     * Returns an integer in 'superscript' notation, using unicode.
     */
    public static String subscript(long i) {
        return script(i, SUB_MINUS, SUBSCRIPTS);
    }

    public static String subscript(CharSequence i) {
        return script(i, SUB_MINUS, SUBSCRIPTS);
    }

    public static String superscript(CharSequence i) {
        return script(i, SUPER_MINUS, SUPERSCRIPTS);
    }

    private static String script(long i, char minusChar, char[] digits) {
        StringBuilder bul = new StringBuilder();
        boolean minus = script(bul, i, digits);
        if (minus) bul.insert(0, minusChar);
        return bul.toString();
    }

    private static String script(CharSequence i, char minus, char[] digits) {
        StringBuilder bul = new StringBuilder();

        i.chars().forEach(c -> {
            if (Character.isDigit(c)) {
                bul.append(digits[c - '0']);
            } else if (c == '-') {
                bul.append(minus);
            } else {
                bul.append((char) c);
            }
        });
        return bul.toString();
    }

    private static boolean script(StringBuilder bul, long i, char[] digits) {
        boolean minus = false;
        if (i < 0) {
            minus = true;
            i = -1 * i;
        }
        if (i == 0) {
            bul.insert(0, digits[0]);
        }
        while (i > 0) {
            int j = (int) (i % 10);
            i /= 10;
            bul.insert(0, digits[j]);
        }
        return minus;
    }

    private static final char[] SUPERSCRIPTS = {
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
    private static final char SUPER_MINUS = '\u207B';

    private static final char[] SUBSCRIPTS = {
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
    private static final char SUB_MINUS = '\u208B';

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

        /**
     * Gives a representation of the string which is completely 'underlined' (using unicode control characters)
     * @since 2.11
     */
    public static String underLine(CharSequence s) {
        return controlEach(s, '\u0332');
    }

    /**
     * Gives a representation of the string which is completely 'double underlined' (using unicode control characters)
     */
    public static String underLineDouble(CharSequence s) {
        return controlEach(s, '\u0333');
    }

    /**
     * Gives a representation of the string which is completely 'overlined' (using unicode control characters)
     */
    public static String overLine(CharSequence s) {
        return controlEach(s, '\u0305');
    }

    /**
     * Gives a representation of the string which is completely 'double overlined' (using unicode control characters)
     */
    public static String overLineDouble(CharSequence s) {
        return controlEach(s, '\u033f');
    }

    /**
     * @since 2.11
     */
    public static String controlEach(CharSequence s, Character control) {
        if (s == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i <  s.length(); i++) {
            result.append(s.charAt(i));
            result.append(control);
        }
        return result.toString();
    }
}
