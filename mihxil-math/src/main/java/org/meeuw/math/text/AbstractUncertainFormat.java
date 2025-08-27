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


import lombok.Getter;
import lombok.Setter;

import java.text.*;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.text.configuration.NumberConfiguration;
import org.meeuw.math.text.configuration.UncertaintyConfiguration;

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
    protected UncertaintyConfiguration.Notation uncertaintyNotation = UncertaintyConfiguration.Notation.PLUS_MINUS;


    @Getter
    @Setter
    private double considerRoundingErrorFactor = 1000d;



    static ThreadLocal<DecimalFormat> EXACT_DOUBLE_FORMAT = ThreadLocal.withInitial(() -> {
        DecimalFormat numberFormat = NumberConfiguration.getDefaultNumberFormat();
        numberFormat.setMaximumFractionDigits(14);
        return numberFormat;
    });


    abstract F of(String v);

    abstract F of(String v, String uncertainty);



    @Override
    public F parseObject(String source, @NonNull ParsePosition pos) {
        int start = pos.getIndex();
        int length = source.length();
        int i = start;
        int plusminIndex = -1;

        // Find the plus-minus character, if present
        while (i < length) {
            char c = source.charAt(i);
            if (c == TextUtils.PLUSMIN) {
                plusminIndex = i;
                break;
            }
            if (!Character.isDigit(c) && c != '.' && !Character.isWhitespace(c) && c != '-') {
                break;
            }
            i++;
        }

        try {
            if (plusminIndex == -1) {
                // Only value, parse until non-numeric
                String valueStr = source.substring(start, i).trim();

                pos.setIndex(i);
                return of(valueStr);
            } else {
                // Value and uncertainty
                String valueStr = source.substring(start, plusminIndex).trim();
                int j = plusminIndex + 1;
                while (j < length && Character.isWhitespace(source.charAt(j))) {
                    j++;
                }
                final String uncertaintyStr;
                if (source.startsWith("NaN")) {
                    uncertaintyStr = "NaN";
                    j +=3;
                } else {
                    // Parse uncertainty until non-numeric
                    while (j < length) {
                        char c = source.charAt(j);
                        if (!Character.isDigit(c) && c != '.' && !Character.isWhitespace(c) && c != '-') {
                            break;
                        }
                        j++;
                    }
                    uncertaintyStr = source.substring(plusminIndex + 1, j).trim();
                }
                pos.setIndex(j);
                return of(valueStr, uncertaintyStr);
            }
        } catch (NumberFormatException e) {
            pos.setErrorIndex(pos.getIndex());
            return null;
            //throw new NotParsable(pos, e);
        }
    }



}
