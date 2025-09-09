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
import org.meeuw.math.DoubleUtils;
import org.meeuw.math.abstractalgebra.reals.DoubleElement;
import org.meeuw.math.numbers.Factor;
import org.meeuw.math.text.configuration.NumberConfiguration;
import org.meeuw.math.text.configuration.UncertaintyConfiguration;

import static org.meeuw.math.DoubleUtils.uncertaintyForDouble;
import static org.meeuw.math.text.UncertainNumberFormat.valueAndError;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class UncertainDoubleFormat extends AbstractUncertainFormat<DoubleElement> {


    @Getter
    @Setter
    private NumberFormat numberFormat = NumberConfiguration.getDefaultNumberFormat();



    private boolean roundingErrorsOnly(double value, double uncertainty) {
        return uncertainty < uncertaintyForDouble(value) * considerRoundingErrorFactor;
    }

    @Override
    public StringBuffer format(Object number, @NonNull StringBuffer toAppendTo, @NonNull FieldPosition pos) {
        if (number instanceof org.meeuw.math.uncertainnumbers.UncertainDouble<?> uncertainNumber) {
            if (uncertainNumber.isExact() || roundingErrorsOnly(uncertainNumber.doubleValue(), uncertainNumber.doubleUncertainty())) {
                scientificNotation(uncertainNumber.doubleValue(), minimumExponent, toAppendTo, pos);
            } else {
                scientificNotationWithUncertainty(uncertainNumber.doubleValue(), uncertainNumber.doubleUncertainty(), toAppendTo, pos);
            }
            return toAppendTo;
        } else {
            throw new IllegalArgumentException("Cannot format given Object " + number.getClass() + " as a Number");
        }
    }

    @Override
    DoubleElement of(String valueStr, Factor factor) {
        double value = Double.parseDouble(valueStr);
        return (DoubleElement) factor.apply(DoubleElement.of(value, considerRoundingErrorFactor * uncertaintyForDouble(value)));

    }

    @Override
    DoubleElement of(String valueStr, String uncertaintyStr, Factor factor) {
        double value = Double.parseDouble(valueStr);
        double uncertainty = Double.parseDouble(uncertaintyStr);

        return (DoubleElement) factor.apply(DoubleElement.of(value, uncertainty));

    }


    public String scientificNotationWithUncertainty(
        double meanDouble,
        double stdDouble) {
        StringBuffer result = new StringBuffer();
        scientificNotationWithUncertainty(meanDouble, stdDouble, result, new FieldPosition(VALUE_FIELD));
        return result.toString();
    }
    /**
     * Represents the mean value in a scientific notation (using Unicode characters), if sensible.
     * The value of the standard deviation is used to determine how many digits can sensibly be shown.
     * <p>
     * If the string will not be less concise without using scientific notation, it may do that. E.g. if the value is very precise, you can just as wel just state
     * all known digits.
     * @since 0.19
     */
    public void scientificNotationWithUncertainty(
        double meanDouble,
        double stdDouble,
        StringBuffer buffer,
        FieldPosition position) {
        if (Double.isInfinite(meanDouble)) {
            EXACT_DOUBLE_FORMAT.get().format(meanDouble, buffer, position);
        } else {

            SplitNumber mean = SplitNumber.split(meanDouble);
            SplitNumber std = SplitNumber.split(stdDouble);

            boolean largeError = Math.abs(stdDouble) > Math.abs(meanDouble);

            // use difference of order of magnitude of std to determine how mean digits of the mean are
            // relevant
            int magnitudeDifference = mean.exponent - std.exponent;
            //System.out.println("Md: " + mean + " " + std + magnitudeDifference);

            int meanDigits = magnitudeDifference; // at least one digit

            assert Double.isNaN(mean.coefficient) || Math.abs(mean.coefficient) < 10 : "unexpected coefficient " + mean.coefficient;

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
            if (mean.exponent != 0 // no useE anyways
                && (
                Math.abs(mean.exponent) < minimumExponent ||
                // neither do we do that if the precision is so high, that we'd show the complete
                // number anyway
                    (meanDigits >= Math.abs(mean.exponent))
            )
            ) {

                double pow = Math.abs(DoubleUtils.pow10(mean.exponent));
                meanDigits -= mean.exponent;
                mean.exponent = 0;
                mean.coefficient *= pow;
                std.coefficient *= pow;

            }

            boolean useE = mean.exponent != 0;



            meanDigits = Math.min(meanDigits, maximalPrecision);

            NumberFormat format = (NumberFormat) numberFormat.clone();
            format.setMaximumFractionDigits(meanDigits);
            format.setMinimumFractionDigits(meanDigits);

            final boolean useBrackets = useE && uncertaintyNotation == UncertaintyConfiguration.Notation.PLUS_MINUS;
            if (useBrackets) {
                buffer.append('(');
            }
            buffer.append(
                valueAndError(
                    format.format(mean.coefficient),
                    format.format(std.coefficient),
                    uncertaintyNotation
                )
            );

            if (useBrackets) {
                buffer.append(')');
            }
            if (useE) {
                buffer.append(TIMES_10);
                TextUtils.superscript(buffer, mean.exponent);
            }
        }
    }

    public  String notationWithUncertainty(double meanDouble,
                                           double stdDouble) {
        NumberFormat format = (NumberFormat) numberFormat.clone();
        int digits = DoubleUtils.log10(stdDouble);
        //format.setMaximumFractionDigits(fd);
        //format.setMinimumFractionDigits(fd);
        return "";
    }


    static ThreadLocal<DecimalFormat> EXACT_DOUBLE_FORMAT = ThreadLocal.withInitial(() -> {
        DecimalFormat numberFormat = NumberConfiguration.getDefaultNumberFormat();
        numberFormat.setMaximumFractionDigits(14);
        return numberFormat;
    });


    public static String scientificNotation(double meanDouble, int minimumExponent) {
        StringBuffer  result = new StringBuffer();
        scientificNotation(meanDouble, minimumExponent, result, new FieldPosition(VALUE_FIELD));
        return result.toString();
    }

    /**
     *
     * @param minimumExponent If the number is smaller than 10^mininumExponent no 10^ notation is used.
     * @since 0.19
     */
    public static void scientificNotation(double meanDouble, int minimumExponent, StringBuffer buffer, FieldPosition position) {
        if (Double.isInfinite(meanDouble)) {
            EXACT_DOUBLE_FORMAT.get().format(meanDouble, buffer, position);
        } else {
            SplitNumber mean = SplitNumber.split(meanDouble);
            // For numbers close to 1, we don't use scientific notation.
            if (Math.abs(mean.exponent) < minimumExponent) {
                double pow = DoubleUtils.pow10(mean.exponent);
                mean.exponent = 0;
                mean.coefficient *= pow;
            }

            boolean useE = mean.exponent != 0;

            EXACT_DOUBLE_FORMAT.get().format(mean.coefficient, buffer, position);
            if (useE) {
                buffer.append(TIMES_10);
                String superscript = TextUtils.superscript(mean.exponent);
                buffer.append(superscript);
                position.setEndIndex(position.getEndIndex() + TIMES_10.length() + superscript.length());
            }
        }
    }
}
