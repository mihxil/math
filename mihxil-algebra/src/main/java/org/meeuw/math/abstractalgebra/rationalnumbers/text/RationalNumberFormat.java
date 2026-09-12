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
package org.meeuw.math.abstractalgebra.rationalnumbers.text;


import java.math.BigInteger;
import java.text.*;
import java.util.HashMap;
import java.util.Map;

import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.math.text.TextUtils;

import static org.meeuw.math.abstractalgebra.rationalnumbers.text.RationalNumberConfiguration.Mode;

/**
 * An abstract extension of {@link Format}, targeted at formatting (and parsing) 'uncertain' numbers.
 * <p>
 * Parsing is fully implemented here, formatting is done by extensions.
 *
 * @author Michiel Meeuwissen
 * @since 0.20

 */
public class RationalNumberFormat extends Format {

    private final Mode mode;

    public RationalNumberFormat(Mode mode) {
        this.mode = mode;
    }

    @Override
    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
        if (obj instanceof RationalNumber rationalNumber) {
            switch(mode) {
                case FRACTION:
                    if (rationalNumber.getDenominator().equals(BigInteger.ONE)) {
                        toAppendTo.append(rationalNumber.getNumerator().toString());
                    } else {
                        if (rationalNumber.isNegative()) {
                            toAppendTo.append("-");
                        }
                        toAppendTo.append(TextUtils.superscript(rationalNumber.getNumerator().abs().toString()))
                            .append(TextUtils.FRACTION_SLASH)
                            .append(rationalNumber.getDenominator().toString());
                    }
                    break;
                case INTEGER_AND_FRACTION:
                    appendIntegerAndFraction(rationalNumber, toAppendTo);
                    break;
                case DECIMAL_WITH_REPEAT:
                    appendRepeatingDecimal(rationalNumber, toAppendTo);
                    break;
                case DECIMAL_ROUNDED:
                    toAppendTo.append(rationalNumber.bigDecimalValue().toPlainString());
                    break;
            }
        } else {
            throw new IllegalArgumentException("Cannot format given Object " + obj.getClass() + " as a rational number");
        }

        return toAppendTo;
    }

    private static void appendIntegerAndFraction(RationalNumber rationalNumber, StringBuffer toAppendTo) {
        if (rationalNumber.isNegative()) {
            toAppendTo.append('-');
        }
        BigInteger[] quotientAndRemainder =
            rationalNumber.getNumerator().abs().divideAndRemainder(rationalNumber.getDenominator());
        BigInteger quotient = quotientAndRemainder[0];
        BigInteger remainder = quotientAndRemainder[1];


        if (!quotient.equals(BigInteger.ZERO) || remainder.equals(BigInteger.ZERO)) {
            toAppendTo.append(quotient);
        }
        if (!remainder.equals(BigInteger.ZERO)) {
            toAppendTo
                .append(TextUtils.superscript(remainder.abs().toString()))
                .append(TextUtils.FRACTION_SLASH)
                .append(TextUtils.subscript(rationalNumber.getDenominator().toString()));

        }
    }

    private static void appendRepeatingDecimal(RationalNumber rationalNumber, StringBuffer toAppendTo) {
        BigInteger numerator = rationalNumber.getNumerator();
        BigInteger denominator = rationalNumber.getDenominator();
        if (numerator.signum() < 0) {
            toAppendTo.append('-');
            numerator = numerator.negate();
        }

        BigInteger[] quotientAndRemainder = numerator.divideAndRemainder(denominator);
        toAppendTo.append(quotientAndRemainder[0]);
        BigInteger remainder = quotientAndRemainder[1];
        if (remainder.equals(BigInteger.ZERO)) {
            return;
        }

        toAppendTo.append('.');
        StringBuilder digits = new StringBuilder();
        Map<BigInteger, Integer> remainderPositions = new HashMap<>();
        while (!remainder.equals(BigInteger.ZERO)) {
            Integer repeatStart = remainderPositions.putIfAbsent(remainder, digits.length());
            if (repeatStart != null) {
                toAppendTo.append(digits, 0, repeatStart);
                toAppendTo.append(TextUtils.overLine(digits.subSequence(repeatStart, digits.length())));
                return;
            }

            BigInteger[] digitAndRemainder = remainder.multiply(BigInteger.TEN).divideAndRemainder(denominator);
            digits.append(digitAndRemainder[0]);
            remainder = digitAndRemainder[1];
        }
        toAppendTo.append(digits);
    }

    @Override
    public RationalNumber parseObject(String source, ParsePosition pos) {
        return null;
    }
}
