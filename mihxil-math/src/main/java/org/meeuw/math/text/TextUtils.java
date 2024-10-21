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
package org.meeuw.math.text;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

import org.checkerframework.checker.nullness.qual.PolyNull;
import org.meeuw.math.time.TimeUtils;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public final class TextUtils {

    public static final String TIMES      = "·";  /* "·10' */
    public static final String PLUSMIN    = "±";
    public static final String INFINITY   = "∞";
    public static final String NOT_EQUALS = "≠";

    public static final String PLACEHOLDER = "x";

    private TextUtils() {
    }

    /**
     * @param i an integer
     * @return an integer in 'superscript' notation, using Unicode.
     */
    public static String superscript(long i) {
        return script(i, SUPER_MINUS, SUPERSCRIPTS);
    }

    /**
     * @param i an integer
     * @return an integer in 'superscript' notation, using Unicode.
     */
    public static String subscript(long i) {
        return script(i, SUB_MINUS, SUBSCRIPTS);
    }

    public static @PolyNull String subscript(@PolyNull CharSequence i) {
        return script(i, SUB, SUBSCRIPTS);
    }

    public static @PolyNull String superscript(@PolyNull CharSequence i) {
        return script(i, SUPER, SUPERSCRIPTS);
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
                    builder.append(superscript(b));
                }
            }
        }
        return builder.toString();
    }

    private static String script(long i, char minusChar, char[] digits) {
        StringBuilder bul = new StringBuilder();
        boolean minus = script(bul, i, digits);
        if (minus) bul.insert(0, minusChar);
        return bul.toString();
    }

    private static @PolyNull String script(@PolyNull CharSequence i, Map<Character, Character> chars, char[] digits) {
        if (i == null) {
            return null;
        }
        StringBuilder bul = new StringBuilder();

        i.chars().forEach(c -> {
            if (Character.isDigit(c)) {
                bul.append(digits[c - '0']);
            } else {
                Character replaced = chars.get((char) c);
                if (replaced != null) {
                    bul.append(replaced);
                } else {
                    bul.append((char) c);
                }
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
        0x2070, // 0
        0x00B9,
        0x00B2,
        0x00B3,
        0x2074,
        0x2075,
        0x2076,
        0x2077,
        0x2078,
        0x2079 // 9
    };
    private static final char SUPER_MINUS = '⁻';
    private static final char SUPER_PLUS = '⁺';

    private static final Map<Character, Character> SUPER = Collections.unmodifiableMap(new HashMap<Character, Character>() {{
        put('-', SUPER_MINUS);
        put('+', SUPER_PLUS);
        put('i', 'ⁱ');
        put('n', 'ⁿ');
        put('(', '⁽');
        put(')', '⁾');
        put('=', '⁼');
        put('P', 'ᴾ');
    }});

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
    private static final char SUB_MINUS = '₋';



    private static final Map<Character, Character> SUB = Collections.unmodifiableMap(new HashMap<Character, Character>() {{
        put('-', SUB_MINUS);
        put('P', 'ₚ');
        put('p', 'ₚ');
        put('r', 'ᵣ');
        put('u', 'ᵤ');
        put('i', 'ᵢ');
        put('m', 'ₘ');
        put('t', 'ₜ');
        put('e', 'ₑ');
        put('v', 'ᵥ');
        put('=', '₌');

    }});

    public static String format(Instant instant, ChronoUnit order) {
         return format(ZoneId.systemDefault(), instant, order);
    }

    public static String format(ZoneId zoneId, Instant instant, ChronoUnit order) {
         Instant toFormat = TimeUtils.round(instant, order);
         if (order.ordinal() < ChronoUnit.DAYS.ordinal()) {
             return DateTimeFormatter.ISO_DATE_TIME.format(toFormat.atZone(zoneId).toLocalDateTime());
         } else {
             return DateTimeFormatter.ISO_DATE.format(toFormat.atZone(zoneId).toLocalDate());
         }
    }

    /**
     * @param s a charsequence to underline
     * @return a representation of the string which is completely 'underlined' (using unicode control characters)
     */
    public static @PolyNull String underLine(@PolyNull CharSequence s) {
        return controlEach(s, '\u0332');
    }

    /**
     * @param s a charsequence to underline double
     * @return a representation of the string which is completely 'double underlined' (using unicode control characters)
     */
    public static @PolyNull String underLineDouble(@PolyNull CharSequence s) {
        return controlEach(s, '\u0333');
    }

    /**
     * @param s a charsequence to overline
     * @return a representation of the string which is completely 'overlined' (using unicode control characters)
     */
    public static @PolyNull String overLine(@PolyNull CharSequence s) {
        return controlEach(s, '\u0305');
    }


    /**
     * @param s a charsequence to overline double
     * @return a representation of the string which is completely 'double overlined' (using unicode control characters)
     */
    public static @PolyNull String overLineDouble(@PolyNull CharSequence s) {
        return controlEach(s, '\u033f');
    }

    public static @PolyNull String controlEach(@PolyNull CharSequence s, Character control) {
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
