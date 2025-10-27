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
import org.meeuw.math.numbers.NumberOperations;
import org.meeuw.math.text.configuration.NumberConfiguration;
import org.meeuw.math.text.configuration.UncertaintyConfiguration;
import org.meeuw.math.text.configuration.UncertaintyConfiguration.Notation;
import org.meeuw.math.uncertainnumbers.UncertainNumber;

import static java.lang.Character.isDigit;
import static java.lang.Character.isWhitespace;
import static org.meeuw.math.text.configuration.UncertaintyConfiguration.Notation.PLUS_MINUS;
import static org.meeuw.math.text.configuration.UncertaintyConfiguration.Notation.ROUND_VALUE;

/**
 * @author Michiel Meeuwissen
 * @since 0.19
 * @param <F> formattable
 * @param <P> parsing to
 * @param <N> number type
 */
public abstract class AbstractUncertainFormat<
    F extends UncertainNumber<?>,
    P extends UncertainNumber<?>,
    N extends Number
    > extends Format {

    public static final int VALUE_FIELD       = 0;
    public static final int UNCERTAINTY_FIELD = 1;
    public static final int E_FIELD           = 2;


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

    @Getter
    @Setter
    private NumberFormat numberFormat = NumberConfiguration.getDefaultNumberFormat();

    @Getter
    final ScientificNotation<N> scientific;

    private final Class<? extends F> clazz;

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected AbstractUncertainFormat(Class clazz, NumberOperations<N> operations) {
        this.clazz = clazz;
        this.scientific = new ScientificNotation<>(this, operations);
        ;
    }

    @Override
    public final StringBuffer format(Object number, @lombok.NonNull StringBuffer toAppendTo, @lombok.NonNull FieldPosition pos) {
        if (clazz.isInstance(number)) {
            valueAndError(toAppendTo,
                pos,
                (F) number,
                getUncertaintyNotation());
            return toAppendTo;
        } else {
            throw new IllegalArgumentException("Cannot format given Object " + number.getClass() + " as a Number");
        }
    }


    /**
     * Parses a value of the {@code P}
     * @param factor
     */
    abstract P exactly(String v, Factor factor);

    abstract P of(String v, String uncertainty, Factor factor);


    @SneakyThrows
    @SuppressWarnings("unchecked")
    public P parseObject(String value) {
        return (P) super.parseObject(value);
    }

    @Override
    public P parseObject(String source, @NonNull ParsePosition pos) {
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
            if (! TextUtils.isNumberChar(c)) {
                break;
            }
            i++;
        }

        try {
            if (plusminIndex == -1 && errorBracket == -1) {
                // Only value, parse until non-numeric
                String valueStr = source.substring(start, i).trim();
                pos.setIndex(i);
                Factor factor = TextUtils.parsePower(valueStr, pos);
                if (uncertaintyNotation == ROUND_VALUE) {
                    StringBuilder uncertaintyStr = new StringBuilder(valueStr.trim());
                    while(uncertaintyStr.charAt(0) == '-') {
                        uncertaintyStr.deleteCharAt(0);
                    }
                    int lastHit = 0;
                    for (int j = 0; j < uncertaintyStr.length(); j++) {
                        char c = uncertaintyStr.charAt(j);
                        if (Character.isDigit(c)) {
                            uncertaintyStr.setCharAt(j, '0');
                            lastHit = j;
                        } else if (c == '.') {
                            continue;
                        } else {
                            break;
                        }
                    }
                    uncertaintyStr.setCharAt(lastHit, '5');
                    return of(valueStr, uncertaintyStr.toString(), factor);
                } else {
                    // it must have been exact/not a result of format
                    return exactly(valueStr, factor);
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
                Factor factor = TextUtils.parsePower(source, pos);
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
                Factor factor = TextUtils.parsePower(source, pos);
                return of(valueStr, uncertaintyStr, factor);
            }
        } catch (NumberFormatException e) {
            pos.setErrorIndex(pos.getIndex());
            throw new NotParsable( pos, e);
        }
    }


    /**
     * Performs the switch on notation.
     */
    protected void valueAndError(StringBuffer appendable, FieldPosition position, F value, UncertaintyConfiguration.Notation uncertaintyNotation) {
        switch (uncertaintyNotation) {
            case PARENTHESES -> valueParenthesesError(appendable, position, value);
            case PLUS_MINUS -> valuePlusMinError(appendable,  position, value);
            case ROUND_VALUE -> valueRound(appendable, position, value, false);
            case ROUND_VALUE_AND_TRIM -> {
                valueRound(appendable, position, value, true);
            }
        }
    }
    protected abstract void valueParenthesesError(StringBuffer appendable, FieldPosition position, F value);
    protected abstract void valuePlusMinError(StringBuffer appendable, FieldPosition position, F value);
    protected abstract void valueRound(StringBuffer appendable, FieldPosition position, F value, boolean trim);


}
