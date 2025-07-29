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
package org.meeuw.math.abstractalgebra.rationalnumbers;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NonNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

import org.meeuw.math.*;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.complex.GaussianRational;
import org.meeuw.math.abstractalgebra.reals.BigDecimalElement;
import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.math.exceptions.*;
import org.meeuw.math.numbers.*;
import org.meeuw.math.operators.BasicAlgebraicIntOperator;
import org.meeuw.math.text.TextUtils;
import org.meeuw.math.validation.NotZero;

/**
 * A rational number is implemented using two {@link BigInteger big integers}, one for the numerator, one for denominator
 * @author Michiel Meeuwissen
 * @since 0.4
 * @see org.meeuw.math.abstractalgebra.rationalnumbers
 */
@Getter
public class RationalNumber extends Number
    implements
    ScalarFieldElement<RationalNumber>,
    SignedNumber<RationalNumber>,
    Ordered<RationalNumber> {

    public static final RationalNumber ONE = new RationalNumber(BigInteger.ONE, BigInteger.ONE);
    public static final RationalNumber ZERO = new RationalNumber(BigInteger.ZERO, BigInteger.ONE);

    private final @NotNull BigInteger numerator;
    private final @NotNull @Positive BigInteger denominator;

    public static RationalNumber of(long numerator, @NotZero long denominator) {
        return new RationalNumber(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
    }

    public static RationalNumber of(@NotNull BigInteger numerator, @NotNull
        @NotZero BigInteger denominator) {
        return new RationalNumber(numerator, denominator);
    }

    public static RationalNumber of(@NotNull BigInteger numerator) {
        return new RationalNumber(numerator, BigInteger.ONE);
    }

    public static RationalNumber of(long longValue) {
        return of(longValue, 1);
    }


    public static RationalNumber of(BigDecimal bigDecimal) {
        bigDecimal = bigDecimal.stripTrailingZeros();
        BigInteger denominator = BigInteger.TEN.pow(bigDecimal.scale());
        return of(bigDecimal.scaleByPowerOfTen(bigDecimal.scale()).toBigIntegerExact(),  denominator);
    }

    RationalNumber(@NonNull BigInteger numerator, @NonNull @NotZero BigInteger denominator) throws InvalidElementCreationException {
        if (denominator.equals(BigInteger.ZERO)) {
            throw new InvalidElementCreationException("Denominator cannot be zero");
        }
        BigInteger gcd = numerator.gcd(denominator);
        BigInteger anumerator = numerator.abs();
        BigInteger adenominator = denominator.abs();
        boolean nn = anumerator.equals(numerator);
        boolean dn = adenominator.equals(denominator);
        boolean positive = (nn && dn) || (!nn && !dn);
        this.numerator = positive ? anumerator.divide(gcd) : anumerator.divide(gcd).negate();
        this.denominator = adenominator.divide(gcd);
    }

    @Override
    public RationalNumbers getStructure() {
        return RationalNumbers.INSTANCE;
    }

    @Override
    public RationalNumber dividedBy(@NotZero long divisor) throws DivisionByZeroException {
        return new RationalNumber(
            numerator,
            denominator.multiply(BigInteger.valueOf(divisor)));
    }

    @Override
    @NonAlgebraic(reason = NonAlgebraic.Reason.SOME)
    public RationalNumber reciprocal() throws ReciprocalException {
        if (numerator.equals(BigInteger.ZERO)) {
            throw new DivisionByZeroException("Denominator cannot be zero", "reciprocal(" + this + ")");
        }
        return new RationalNumber(denominator, numerator);
    }

    @Override
    public RationalNumber pow(final int exponent) throws IllegalPowerException {
        if (exponent < 0) {
            final int positiveExponent = -1 * exponent;
            if (numerator.equals(BigInteger.ZERO)) {
                throw new IllegalPowerException("Cannot take negative exponent (" + exponent + ") of zero", BasicAlgebraicIntOperator.POWER.stringify(toString(), Integer.toString(exponent)));
            }
            return new RationalNumber(
                denominator.pow(positiveExponent),
                numerator.pow(positiveExponent));
        } else if (exponent == 0) {
            return ONE;
        } else {
            return new RationalNumber(
                numerator.pow(exponent),
                denominator.pow(exponent));
        }
    }

    @Override
    public boolean isZero() {
        return numerator.equals(BigInteger.ZERO);
    }

    @Override
    @NonExact
    @NonAlgebraic
    public RationalNumber sin() {
        return of(BigDecimalOperations.INSTANCE.sin(bigDecimalValue()).getValue());
    }

    @Override
    @NonExact
    @NonAlgebraic
    public RationalNumber asin() {
        return of(BigDecimalOperations.INSTANCE.asin(bigDecimalValue()).getValue());
    }

    @Override
    @NonExact
    @NonAlgebraic
    public RationalNumber cos() {
        return of(BigDecimalOperations.INSTANCE.cos(bigDecimalValue()).getValue());
    }

    @Override
    @NonAlgebraic
    public RationalNumber tan() {
        return of(BigDecimalOperations.INSTANCE.tan(bigDecimalValue()).getValue());
    }

    @Override
    @NonAlgebraic
    public RationalNumber sqrt() {
        return of(BigDecimalOperations.INSTANCE.sqrt(bigDecimalValue()).getValue());
    }

    @Override
    public RationalNumber sqr() {
        return new RationalNumber(numerator.multiply(numerator), denominator.multiply(denominator));
    }

    @Override
    @NonAlgebraic(reason = NonAlgebraic.Reason.SOME)
    public RationalNumber dividedBy(@NotZero RationalNumber divisor) throws DivisionByZeroException {
        if (divisor.isZero()) {
            throw new DivisionByZeroException(this, divisor);
        }
        return new RationalNumber(
            numerator.multiply(divisor.denominator),
            denominator.multiply(divisor.numerator)
        );
    }

    @Override
    public RationalNumber plus(RationalNumber summand) {
        return new RationalNumber(
            numerator.multiply(summand.denominator).add(summand.numerator.multiply(denominator)),
            denominator.multiply(summand.denominator)
        );
    }

    @Override
    public RationalNumber negation() {
        return new RationalNumber(
                numerator.multiply(IntegerUtils.MINUS_ONE), denominator);
    }

    @Override
    public RationalNumber minus(RationalNumber subtrahend) {
        return plus(subtrahend.times(-1));
    }

    public BigDecimalElement toBigDecimalElement() {
        return BigDecimalElement.of(bigDecimalValue());
    }

    @Override
    public int compareTo(@org.checkerframework.checker.nullness.qual.NonNull RationalNumber compare) {
        return numerator.multiply(compare.denominator)
            .compareTo(compare.numerator.multiply(denominator));
    }

    @Override
    public RationalNumber times(RationalNumber multiplier) {
        return new RationalNumber(
                numerator.multiply(multiplier.numerator),
                denominator.multiply(multiplier.denominator)
        );
    }

    @Override
    public RationalNumber times(long multiplier) {
        return new RationalNumber(
                numerator.multiply(BigInteger.valueOf(multiplier)),
                denominator
        );
    }

    @Override
    @NonExact("Small numbers (< 100_000) are multiplied by 100_000, and the result will be divided by 100_000, to achieve better precision")
    public RationalNumber times(double multiplier) {
        double signum = Math.signum(multiplier);
        double abs = Math.abs(multiplier);
        if (abs >= 100_000d) {
            return times(Math.round(multiplier));
        } else {
            multiplier = 100_000 * abs * signum;
            return new RationalNumber(
                numerator.multiply(BigInteger.valueOf(Math.round(multiplier))),
                denominator.multiply(BigInteger.valueOf(100_000))
            );

        }
    }




    @Override
    public int intValue() {
        return numerator.divide(denominator).intValue();
    }

    @Override
    public long longValue() {
        return numerator.divide(denominator).longValue();
    }

    @Override
    public float floatValue() {
        return (float) numerator.longValue() / denominator.longValue();
    }

    @Override
    public double doubleValue() {
        return bigDecimalValue().doubleValue();
    }
    @SuppressWarnings("unchecked")
    @Override
    public <F extends AlgebraicElement<F>> Optional<F> castDirectly(Class<F> clazz) {
        if (clazz.isAssignableFrom(BigDecimalElement.class)) {
            return Optional.of((F) BigDecimalElement.of(bigDecimalValue()));
        }
        if (clazz.isAssignableFrom(RealNumber.class)) {
            return Optional.of((F) RealNumber.of(doubleValue()));
        }
        if (clazz.isAssignableFrom(GaussianRational.class)) {
            return Optional.of((F) GaussianRational.of(this));
        }
        return Optional.empty();
    }

    @Override
    public BigDecimal bigDecimalValue() {
        return new BigDecimal(numerator).divide(new BigDecimal(denominator),
            MathContextConfiguration.get().getContext());
    }

    @Override
    public boolean isOne() {
        return this.equals(ONE);
    }

    @Override
    public int signum() {
        return numerator.signum();
    }

    @Override
    public RationalNumber abs() {
        return new RationalNumber(numerator.abs(), denominator);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RationalNumber that = (RationalNumber) o;

        return numerator.equals(that.numerator) && denominator.equals(that.denominator);
    }

    @Override
    public int hashCode() {
        int result = numerator.hashCode();
        result = 31 * result + denominator.hashCode();
        return result;
    }

    @Override
    public String toString() {
        if (denominator.equals(BigInteger.ONE)) {
            return numerator.toString();
        } else {
            return (isNegative() ? "-" : "") + TextUtils.superscript(numerator.abs().toString()) + TextUtils.FRACTION_SLASH + TextUtils.subscript(denominator.toString());
        }
    }


}
