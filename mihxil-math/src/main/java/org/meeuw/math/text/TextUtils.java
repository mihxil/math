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

import lombok.SneakyThrows;

import java.io.IOException;
import java.util.*;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.PolyNull;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public final class TextUtils {

    public static final char TIMES      = '·';  /* "·10' */

    public static final char PLUSMIN    = '±';
    public static final String INFINITY   = "∞";
    public static final String NOT_EQUALS = "≠";

    public static final String PLACEHOLDER = "x";
    public static final char FRACTION_SLASH = '\u2044';

    private TextUtils() {
    }

    /**
     * @param i an integer
     * @since 0.19
     */
    @SneakyThrows(IOException.class)
    public static void superscript(Appendable a, long i)  {
        script(a, i, SUPER_MINUS, SUPERSCRIPTS);
    }


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


    public static String unsuperscript(CharSequence s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            sb.append(unsuperscript(c));
        }
        return sb.toString();
    }
     public static char unsuperscript(char c) {
         return REVERSE_SUPERSCRIPTS.getOrDefault(c, c);
    }


    public static String unsubscript(CharSequence s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            sb.append(REVERSE_SUBSCRIPTS.getOrDefault(c, c));
        }
        return sb.toString();
    }

    public static String undo(CharSequence s) {
        String st = TextUtils.unsuperscript(TextUtils.unsubscript(s));
        return  st.replace(TextUtils.FRACTION_SLASH, '/');
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

    /**
     * @since 0.19
     * @param s
     */
    public static String stripParentheses(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        s = s.trim();
        if (s.startsWith("(") && s.endsWith(")")) {
            return s.substring(1, s.length() - 1);
        }
        return s;

    }

    private static String script(long i, char minusChar, char[] digits) {
        StringBuilder bul = new StringBuilder();
        try {
            script(bul, i, minusChar, digits);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bul.toString();
    }


    private static @PolyNull String script(@PolyNull CharSequence i, Map<Character, Character> chars, char[] digits) {
        if (i == null) {
            return null;
        }
        StringBuilder bul = new StringBuilder();
        script(bul, i, chars, digits);
        return bul.toString();
    }


    /**
     * @since 0.19
     */
    private static void script(@NonNull Appendable bul, CharSequence i, Map<Character, Character> chars, char[] digits) {
        if (i == null) {
            return;
        }
        i.chars().forEach(c -> {
            try {
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
            } catch (IOException ignored) {}
        });

    }

    /**
     * @since 0.19
     */
    private static void script(Appendable bul, long i, char minusChar, char[] digits) throws IOException {
        if (i < 0) {
            bul.append(minusChar);
            i = -1 * i;
        }
        if (i == 0) {
            bul.append(digits[0]);
        }
        char[] buf = new char[21];
        int pos = 0;
        while (i > 0) {
            int j = (int) (i % 10);
            i /= 10;
            buf[pos++] = digits[j];
        }
        while (pos > 0) {
            bul.append(buf[--pos]);
        }
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

    private static final Map<Character, Character> SUPER = Collections.unmodifiableMap(new HashMap<>() {{
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

    private static final Map<Character, Character> REVERSE_SUPERSCRIPTS = new HashMap<>();
    private static final Map<Character, Character> REVERSE_SUBSCRIPTS = new HashMap<>();
    static {
        for (int i = 0; i < SUPERSCRIPTS.length; i++) {
            REVERSE_SUPERSCRIPTS.put(SUPERSCRIPTS[i], (char)('0' + i));
        }
        REVERSE_SUPERSCRIPTS.put(SUPER_MINUS, '-');
        REVERSE_SUPERSCRIPTS.put(SUPER_PLUS, '+');

        for (int i = 0; i < SUBSCRIPTS.length; i++) {
            REVERSE_SUBSCRIPTS.put(SUBSCRIPTS[i], (char)('0' + i));
        }
        REVERSE_SUBSCRIPTS.put(SUB_MINUS, '-');
        REVERSE_SUBSCRIPTS.put(SUPER_PLUS, '+');
    }


    private static final Map<Character, Character> SUB = Collections.unmodifiableMap(new HashMap<>() {{
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

    public static void overLine(StringBuilder builder,  CharSequence s) {
        controlEach(builder, s, '\u0305');
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
        controlEach(result, s, control);
        return result.toString();
    }

    public static void controlEach(StringBuilder builder, CharSequence s, Character control) {
        if (s == null) {
            return;
        }
        for (int i = 0; i <  s.length(); i++) {
            builder.append(s.charAt(i));
            builder.append(control);
        }
     }

}
