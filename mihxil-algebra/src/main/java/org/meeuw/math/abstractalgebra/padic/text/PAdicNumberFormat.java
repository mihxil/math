package org.meeuw.math.abstractalgebra.padic.text;

import java.math.BigInteger;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.Optional;
import java.util.function.IntFunction;

import org.meeuw.math.DigitUtils;
import org.meeuw.math.abstractalgebra.padic.PAdicNumber;
import org.meeuw.math.abstractalgebra.padic.impl.AdicDigitUtils;
import org.meeuw.math.abstractalgebra.padic.impl.AdicDigits;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.math.text.TextUtils;

/**
 * Formats {@link PAdicNumber p-adic numbers}, see {@link PAdicConfiguration}. Parsing is done by {@link org.meeuw.math.abstractalgebra.padic.PAdicNumbers#fromString(String)},
 * which accepts all notations.
 * @since 0.24
 */
public class PAdicNumberFormat extends Format {

    /**
     * Precedes the digits if the repetend could not be determined
     */
    public static final String ELLIPSIS = "…";

    private final PAdicConfiguration.Notation notation;
    private final int maximalNumberOfDigits;

    public PAdicNumberFormat(PAdicConfiguration.Notation notation, int maximalNumberOfDigits) {
        this.notation = notation;
        this.maximalNumberOfDigits = maximalNumberOfDigits;
    }

    @Override
    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
        if (!(obj instanceof PAdicNumber number)) {
            throw new IllegalArgumentException("Cannot format given Object " + obj.getClass() + " as a p-adic number");
        }
        final int base = number.getStructure().getBase();
        final int fractionDigits = number.getNumberOfFractionDigits();
        final RationalNumber shifted = number.getValue().times(BigInteger.valueOf(base).pow(fractionDigits));
        final Optional<AdicDigits> digits = AdicDigitUtils.toAdicDigits(base, shifted, maximalNumberOfDigits);
        if (digits.isPresent()) {
            final AdicDigits integerPart = digits.get().rightShift(fractionDigits);
            switch (notation) {
                case ELLIPSIS -> toAppendTo.append(integerPart);
                case OVERLINE -> {
                    if (integerPart.isRepetitive()) {
                        toAppendTo.append(TextUtils.overLine(integerPart.repetendToString()));
                        toAppendTo.append(integerPart.digitsToString());
                    } else {
                        final String integerDigits = integerPart.digitsToString();
                        toAppendTo.append(integerDigits.isEmpty() ? "0" : integerDigits);
                    }
                }
            }
            appendFraction(toAppendTo, fractionDigits, i -> digits.get().get(i).value());
        } else {
            final byte[] leastSignificant = AdicDigitUtils.leastSignificantDigits(base, shifted, Math.max(maximalNumberOfDigits, fractionDigits));
            toAppendTo.append(ELLIPSIS);
            for (int i = leastSignificant.length - 1; i >= fractionDigits; i--) {
                toAppendTo.append(DigitUtils.digitToString(leastSignificant[i]));
            }
            appendFraction(toAppendTo, fractionDigits, i -> leastSignificant[i]);
        }
        toAppendTo.append(TextUtils.subscript(base));
        return toAppendTo;
    }

    private static void appendFraction(StringBuffer builder, int fractionDigits, IntFunction<Byte> digit) {
        if (fractionDigits > 0) {
            builder.append('.');
            for (int i = fractionDigits - 1; i >= 0; i--) {
                builder.append(DigitUtils.digitToString(digit.apply(i)));
            }
        }
    }

    /**
     * Not supported, since the structure (the base) is needed. Use {@link org.meeuw.math.abstractalgebra.padic.PAdicNumbers#fromString(String)}.
     */
    @Override
    public Object parseObject(String source, ParsePosition pos) {
        return null;
    }
}
