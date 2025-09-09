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


import lombok.*;

import java.text.*;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.exceptions.NotParsable;
import org.meeuw.math.numbers.Factor;
import org.meeuw.math.text.configuration.NumberConfiguration;
import org.meeuw.math.text.configuration.UncertaintyConfiguration.Notation;

import static java.lang.Character.isDigit;
import static java.lang.Character.isWhitespace;
import static org.meeuw.math.text.TextUtils.unsuperscript;
import static org.meeuw.math.text.configuration.UncertaintyConfiguration.Notation.PLUS_MINUS;
import static org.meeuw.math.text.configuration.UncertaintyConfiguration.Notation.ROUND_VALUE;

/**
 * @author Michiel Meeuwissen
 * @since 0.19
 */
public abstract class AbstractUncertainFormat<F> extends Format {


    public static final int VALUE_FIELD = 0;
    public static final int UNCERTAINTY_FIELD = 1;
    public static final int E_FIELD = 2;

    public static final String TIMES_10 = TextUtils.TIMES + "10";  /* "·10' */

    /**
     * The minimum exponent defined how close a number must be to 1, to not use scientific notation
     * for it. Defaults to 4, which means that numbers between 0.0001 and 10000 (and -0.0001 and
     * -10000) are presented without useage of scientific notation
     * <p>
     * This is used in {@link #toString()}
     */
    @Getter
    @Setter
    protected int minimumExponent = 4;

    @Getter
    @Setter
    protected Notation uncertaintyNotation = PLUS_MINUS;


    @Getter
    @Setter
    protected double considerRoundingErrorFactor = 1000d;

    @Getter
    @Setter
    protected int maximalPrecision = Integer.MAX_VALUE;


    static ThreadLocal<DecimalFormat> EXACT_DOUBLE_FORMAT = ThreadLocal.withInitial(() -> {
        DecimalFormat numberFormat = NumberConfiguration.getDefaultNumberFormat();
        numberFormat.setMaximumFractionDigits(14);
        return numberFormat;
    });


    abstract F of(String v, Factor factor);

    abstract F of(String v, String uncertainty, Factor factor);


    @SneakyThrows
    @SuppressWarnings("unchecked")
    public F parseObject(String value) {
        return (F) super.parseObject(value);
    }

    @Override
    public F parseObject(String source, @NonNull ParsePosition pos) {
        int start = pos.getIndex();
        int length = source.length();

        int plusminIndex = -1;
        int errorBracket = -1;

        while (start < length && isWhitespace(source.charAt(start))) {
            start++;
        }

        boolean bracketFound = false;
        if (start < length && source.charAt(start) == '(') {
            start++;
            bracketFound = true;
        }


        int i = start;
        // Find the plus-minus character, if present
        while (i < length) {
            char c = source.charAt(i);
            if (c == TextUtils.PLUSMIN) {
                plusminIndex = i;
                break;
            }
            if (c == '(') {
                errorBracket = i;
                break;
            }
            if (!isDigit(c) && c != '.' && !Character.isWhitespace(c) && c != '-') {
                break;
            }
            i++;
        }

        try {
            if (plusminIndex == -1 && errorBracket == -1) {
                // Only value, parse until non-numeric
                String valueStr = source.substring(start, i).trim();
                pos.setIndex(i);
                Factor factor = parsePower(valueStr, pos);
                if (uncertaintyNotation == ROUND_VALUE) {
                    StringBuilder uncertaintyStr = new StringBuilder(valueStr);
                    int lastMatch = -1;
                    for (int j = 0; j < uncertaintyStr.length(); j++) {
                        if (Character.isDigit(uncertaintyStr.charAt(j))) {
                            lastMatch = j;
                            uncertaintyStr.setCharAt(j, '0');
                        }
                    }
                    if (lastMatch >= 0) {
                        uncertaintyStr.setCharAt(lastMatch, '5');
                    }
                    while(uncertaintyStr.charAt(0) == '-') {
                        uncertaintyStr.deleteCharAt(0);
                    }
                    return of(valueStr, uncertaintyStr.toString(), factor);
                } else {
                    // it must have been exact/not a result of format
                    return of(valueStr, factor);
                }
            } else if (errorBracket >= 0)  {
                // Value and uncertainty
                final String valueStr = source.substring(start, errorBracket).trim();
                int j = errorBracket + 1;
                while (j < length && Character.isWhitespace(source.charAt(j))) {
                    j++;
                }
                // Parse uncertainty until close bracket
                while (j < length) {
                    char c = source.charAt(j);

                    if (!isDigit(c)) {
                        break;
                    }
                    j++;
                }


                String errorDigits = source.substring(errorBracket + 1, j).trim();

                while(j < length && isDigit(source.charAt(j))) {
                    j++;
                }
                if (bracketFound) {
                    if (source.charAt(j) != ')') {
                        pos.setErrorIndex(j);
                        throw new NotParsable("No closing bracket");
                    }
                    j++;
                }
                pos.setIndex(j + 1);

                // count how many things like space and - might lead the valueStr
                int firstDigit = 0;
                while (! isDigit(valueStr.charAt(firstDigit))) {
                    firstDigit++;
                }

                String uncertaintyStr = valueStr.substring(firstDigit, valueStr.length() - errorDigits.length()).replaceAll("\\d", "0") + errorDigits;
                Factor factor = parsePower(source, pos);
                return of(valueStr, uncertaintyStr,  factor);


            } else {
                // Value and uncertainty
                final String valueStr = source.substring(start, plusminIndex).trim();
                int j = plusminIndex + 1;
                while (j < length && Character.isWhitespace(source.charAt(j))) {
                    j++;
                }
                final String uncertaintyStr;
                if (source.length() >= j + 3 && source.substring(j, j+3).equalsIgnoreCase("NaN")) {
                    uncertaintyStr = "NaN";
                    j +=3;
                } else {
                    // Parse uncertainty until non-numeric
                    while (j < length) {
                        char c = source.charAt(j);
                        if (!isDigit(c) && c != '.' && !Character.isWhitespace(c) && c != '-') {
                            break;
                        }
                        j++;
                    }
                    uncertaintyStr = source.substring(plusminIndex + 1, j).trim();
                }
                while(j < length && isDigit(source.charAt(j))) {
                    j++;
                }
                if (bracketFound) {
                    if (source.charAt(j) != ')') {
                        pos.setErrorIndex(j);
                        throw new NotParsable("No closing bracket found in " + source + " at postion " + pos.getIndex());
                    }
                    j++;
                }
                pos.setIndex(j);
                Factor factor = parsePower(source, pos);
                return of(valueStr, uncertaintyStr, factor);
            }
        } catch (NumberFormatException e) {
            pos.setErrorIndex(pos.getIndex());
            throw new NotParsable( pos, e);
        }
    }

    Factor parsePower(String value, ParsePosition parsePosition) {
        int pos = parsePosition.getIndex();
        //skip leading space
        while (pos < value.length() && isWhitespace(value.charAt(pos))) {
            pos++;
        }
        // if nothing, just return 1;
        if (pos >= value.length()) {
            return Factor.ONE;
        }
        if (value.charAt(pos) == 'e' || value.charAt(pos) == 'E') {
            parsePosition.setIndex(pos + 1);
            int power = parseInt(value, parsePosition);
            return Factor.ofPow10(power);
        } else if (value.charAt(pos) == TextUtils.TIMES) {
            parsePosition.setIndex(pos + 1);
            int base = parseInt(value, parsePosition);
            int power = parseSuperscript(value, parsePosition);
            return Factor.ofPow(base, power);
        } else {
            return Factor.ONE;
        }
    }

    int parseInt(String value, ParsePosition parsePosition) {
        int pos = parsePosition.getIndex();
        while(isWhitespace(value.charAt(pos))) {
            pos++;
        }
        int i = pos;
        while(i < value.length() && isDigit(value.charAt(i))) {
            i++;
        }
        parsePosition.setIndex(i);
        return Integer.parseInt(value.substring(pos, i));
    }

    int parseSuperscript(String value, ParsePosition parsePosition) {
        int pos = parsePosition.getIndex();
        while(isWhitespace(value.charAt(pos))) {
            pos++;
        }
        int i = pos;
        char c = unsuperscript(value.charAt(i));
        int result = 0;

        boolean negative = false;
        if (c == '+' || c == '-') {
            i++;
            if (c == '-') {
                negative = true;
            }
            c = unsuperscript(value.charAt(i));
        }
        while(isDigit(c)) {
            i++;
            result = result * 10 + c - '0';
            if (i >= value.length()) {
                break;
            }
            c = unsuperscript(value.charAt(i));
        }
        parsePosition.setIndex(i);
        if (i == pos) {
            return 1;
        }
        return negative ? -1 * result : result;
    }



}
