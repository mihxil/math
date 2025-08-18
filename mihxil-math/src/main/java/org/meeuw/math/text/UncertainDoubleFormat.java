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
import java.util.Locale;
import java.util.Optional;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.DoubleUtils;
import org.meeuw.math.text.configuration.NumberConfiguration;
import org.meeuw.math.text.configuration.UncertaintyConfiguration;
import org.meeuw.math.abstractalgebra.reals.DoubleElement;

import static org.meeuw.math.DoubleUtils.uncertaintyForDouble;
import static org.meeuw.math.text.UncertainNumberFormat.valueAndError;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class UncertainDoubleFormat extends Format {

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
    private int minimumExponent = 4;

    @Getter
    @Setter
    private UncertaintyConfiguration.Notation uncertaintyNotation = UncertaintyConfiguration.Notation.PLUS_MINUS;

    @Getter
    @Setter
    private NumberFormat numberFormat = NumberConfiguration.getDefaultNumberFormat();

    @Getter
    @Setter
    private double considerRoundingErrorFactor = 1000d;


    private boolean roundingErrorsOnly(double value, double uncertainty) {
        return uncertainty < uncertaintyForDouble(value) * considerRoundingErrorFactor;
    }

    @Override
    public StringBuffer format(Object number, @NonNull StringBuffer toAppendTo, @NonNull FieldPosition pos) {
        if (number instanceof org.meeuw.math.uncertainnumbers.UncertainDouble<?> uncertainNumber) {
            if (uncertainNumber.isExact() || roundingErrorsOnly(uncertainNumber.doubleValue(), uncertainNumber.doubleUncertainty())) {
                toAppendTo.append(scientificNotation(uncertainNumber.doubleValue(), minimumExponent));
            } else {
                toAppendTo.append(scientificNotationWithUncertainty(uncertainNumber.doubleValue(), uncertainNumber.doubleUncertainty()));
            }
            return toAppendTo;
        } else {
            throw new IllegalArgumentException("Cannot format given Object " + number.getClass() + " as a Number");
        }
    }

    @Override
    public Object parseObject(String source, @NonNull ParsePosition  pos) {
        return parse(source, pos);
    }

    public DoubleElement parse(String source, ParsePosition pos) {
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
                double value = Double.parseDouble(valueStr);
                pos.setIndex(i);
                return DoubleElement.of(value);
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
                double value = Double.parseDouble(valueStr);
                double uncertainty = Double.parseDouble(uncertaintyStr);
                pos.setIndex(j);
                return DoubleElement.of(value, uncertainty);
            }
        } catch (NumberFormatException e) {
            pos.setErrorIndex(pos.getIndex());
            return null;
            //throw new NotParsable(pos, e);
        }
    }

    /**
     * Represents the mean value in a scientific notation (using Unicode characters), if sensible.
     * The value of the standard deviation is used to determine how many digits can sensibly be shown.
     * <p>
     * If the string will not be less concise without using scientific notation, it may do that. E.g. if the value is very precise, you can just as wel just state
     * all known digits.
     */
    public String scientificNotationWithUncertainty(
        double meanDouble,
        double stdDouble) {
        return formatInfinity(meanDouble).orElseGet(() -> {
            SplitNumber mean = SplitNumber.split(meanDouble);
            SplitNumber std = SplitNumber.split(stdDouble);

            boolean largeError = Math.abs(stdDouble) > Math.abs(meanDouble);

            // use difference of order of magnitude of std to determine how mean digits of the mean are
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
            std.coefficient /= DoubleUtils.pow10(magnitudeDifference);


            // For numbers close to 1, we don't use scientific notation.
            if (Math.abs(mean.exponent) < minimumExponent ||
                // neither do we do that if the precision is so high, that we'd show the complete
                // number anyway
                (mean.exponent > 0 && meanDigits > mean.exponent)) {

                double pow = Math.abs(DoubleUtils.pow10(mean.exponent));
                mean.exponent = 0;
                mean.coefficient *= pow;
                std.coefficient *= pow;

            }

            boolean useE = mean.exponent != 0;

            int fd = meanDigits - DoubleUtils.log10(largeError ? std.coefficient : mean.coefficient) - 1;
            //System.out.println(meanDigits + " -> " + mean + " -> " + fd);
            NumberFormat format = (NumberFormat) numberFormat.clone();
            format.setMaximumFractionDigits(fd);
            format.setMinimumFractionDigits(fd);

            final boolean useBrackets = useE && uncertaintyNotation == UncertaintyConfiguration.Notation.PLUS_MINUS;
            return
                (useBrackets ? "(" : "") + valueAndError(format.format(mean.coefficient), format.format(std.coefficient), uncertaintyNotation)
                    +
                    (useBrackets ? ")" : "") + (useE ? (TIMES_10 + TextUtils.superscript(mean.exponent)) : "");
        });
    }



    public static String scientificNotation(double meanDouble, int minimumExponent) {
        return formatInfinity(meanDouble).orElseGet(() -> {
            SplitNumber mean = SplitNumber.split(meanDouble);

            // For numbers close to 1, we don't use scientific notation.
            if (Math.abs(mean.exponent) < minimumExponent) {
                double pow = DoubleUtils.pow10(mean.exponent);
                mean.exponent = 0;
                mean.coefficient *= pow;
            }
            NumberFormat nf = NumberFormat.getInstance(Locale.US);
            nf.setMaximumFractionDigits(14);
            nf.setGroupingUsed(false);
            boolean useE = mean.exponent != 0;

            return
                nf.format(mean.coefficient)
                    +
                    (useE ? TIMES_10 + TextUtils.superscript(mean.exponent) : "");
        });
    }

    public static Optional<String> formatInfinity(double meanDouble) {
        if (Double.isInfinite(meanDouble)) {
            return Optional.of((meanDouble < 0 ? "-" : "") + TextUtils.INFINITY);
        } else {
            return Optional.empty();
        }
    }


}
