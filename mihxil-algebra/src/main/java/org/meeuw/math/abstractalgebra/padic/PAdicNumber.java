package org.meeuw.math.abstractalgebra.padic;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigInteger;
import java.util.Optional;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.abstractalgebra.FieldElement;
import org.meeuw.math.abstractalgebra.padic.impl.AdicDigitUtils;
import org.meeuw.math.abstractalgebra.padic.impl.AdicDigits;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.math.exceptions.*;
import org.meeuw.math.text.FormatService;

/**
 * A p-adic number with (eventually) repeating digits.
 * <p>
 * Such a number is exactly a rational number (see {@link AdicDigitUtils}), so that is how it is stored, and how the arithmetic is done. The p-adic digits are
 * only calculated to {@link #toString() present} it. If the denominator contains factors {@code p}, then there are digits after the point.
 *
 * @see PAdicNumbers
 */
@EqualsAndHashCode
public class PAdicNumber implements FieldElement<PAdicNumber> {

    final PAdicNumbers structure;

    /**
     * The value of this p-adic number
     */
    @Getter
    final RationalNumber value;

    PAdicNumber(PAdicNumbers structure, RationalNumber value) {
        this.structure = structure;
        this.value = value;
    }

    @Override
    public @NonNull PAdicNumbers getStructure() {
        return structure;
    }

    /**
     * @return the value as a big integer, if this p-adic number is an integer (like {@code ...0 12} or {@code ...4} (which is {@code -1}))
     * @throws NotFiniteException if this is not an integer (it has infinitely many non-zero digits, or digits after the point)
     */
    public BigInteger bigIntegerValue() {
        if (!value.getDenominator().equals(BigInteger.ONE)) {
            throw new NotFiniteException(this + " is not an integer");
        }
        return value.getNumerator();
    }

    /**
     * The p-adic valuation: the number of times {@code p} divides this number. It is negative if the number has digits after the point.
     * @return the valuation, or {@link Integer#MAX_VALUE} for zero.
     */
    public int valuation() {
        if (value.isZero()) {
            return Integer.MAX_VALUE;
        }
        return multiplicity(value.getNumerator()) - multiplicity(value.getDenominator());
    }

    /**
     * Whether this is a p-adic integer, i.e. it has no digits after the point.
     */
    public boolean isInteger() {
        return multiplicity(value.getDenominator()) == 0;
    }

    private int multiplicity(BigInteger i) {
        int result = 0;
        while (i.signum() != 0 && i.mod(structure.bbase).signum() == 0) {
            i = i.divide(structure.bbase);
            result++;
        }
        return result;
    }

    @Override
    public PAdicNumber plus(PAdicNumber summand) {
        return with(value.plus(summand.value));
    }

    @Override
    public PAdicNumber negation() {
        return with(value.negation());
    }

    @Override
    public PAdicNumber times(@NonNull PAdicNumber multiplier) {
        return with(value.times(multiplier.value));
    }

    @Override
    public PAdicNumber times(long multiplier) {
        return with(value.times(BigInteger.valueOf(multiplier)));
    }

    @Override
    public PAdicNumber times(BigInteger multiplier) {
        return with(value.times(multiplier));
    }

    @Override
    public PAdicNumber dividedBy(BigInteger divisor) {
        if (divisor.signum() == 0) {
            throw new DivisionByZeroException(this, divisor);
        }
        return with(value.dividedBy(divisor));
    }

    @Override
    public PAdicNumber reciprocal() throws ReciprocalException {
        if (value.isZero()) {
            throw new ReciprocalException("Zero has no reciprocal", "1/" + this);
        }
        return with(value.reciprocal());
    }

    /**
     * Shifts the digits to the left, which is multiplying by {@code pⁱ}
     */
    public PAdicNumber leftShift(int i) {
        if (i < 0) {
            return rightShift(-i);
        }
        return with(value.times(structure.bbase.pow(i)));
    }

    /**
     * Shifts the digits to the right, which is dividing by {@code pⁱ}. Digits may end up after the point.
     */
    public PAdicNumber rightShift(int i) {
        if (i < 0) {
            return leftShift(-i);
        }
        return with(value.dividedBy(structure.bbase.pow(i)));
    }

    /**
     * @param repetend The digits that are to be repeated. The first one is the most significant.
     * @return a new p-adic number with the same (finite number of) digits as this one, and the given repetend before that
     * @throws IllegalArgumentException if this number has a repetend already, or has digits after the point
     */
    public PAdicNumber withRepetend(int... repetend) {
        if (!isInteger()) {
            throw new IllegalArgumentException(this + " has digits after the point");
        }
        final AdicDigits digits = AdicDigitUtils.toAdicDigits(structure.base, value, 100_000)
            .orElseThrow(() -> new IllegalArgumentException(this + " has a repetend already"));
        return structure.of(digits.withRepetend(repetend));
    }

    /**
     * The p-adic digits of this number, if the repetend is not longer than the given number of digits.
     * @return the digits of {@code this · pⁿ}, where {@code n} is the number of digits after the point (so {@code -1 · } {@link #valuation()} if that is negative)
     */
    public Optional<AdicDigits> adicDigits(int maximalNumberOfDigits) {
        return AdicDigitUtils.toAdicDigits(structure.base, value.times(structure.bbase.pow(getNumberOfFractionDigits())), maximalNumberOfDigits);
    }

    /**
     * @return the number of digits after the point (which is {@code -1 · }{@link #valuation()} if that is negative, and otherwise {@code 0})
     */
    public int getNumberOfFractionDigits() {
        return multiplicity(value.getDenominator());
    }

    private PAdicNumber with(RationalNumber value) {
        return new PAdicNumber(structure, value);
    }

    /**
     * Presents the p-adic digits, as configured by {@link org.meeuw.math.abstractalgebra.padic.text.PAdicConfiguration}. By default first the repetend, then a space,
     * then the other digits, and possibly digits after the point. E.g. {@code ...4 ₅} is {@code -1}, {@code ...0 2.4₅} is {@code 14/5}, and {@code ...04 1₅} is
     * {@code 1/6}.
     */
    @Override
    public String toString() {
        return FormatService.toString(this);
    }
}
