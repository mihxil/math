/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
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

import org.meeuw.math.NonAlgebraic;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.complex.GaussianRational;
import org.meeuw.math.abstractalgebra.reals.BigDecimalElement;
import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.math.exceptions.DivisionByZeroException;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.numbers.MathContextConfiguration;
import org.meeuw.math.numbers.SignedNumber;
import org.meeuw.math.text.TextUtils;

/**
 * A rational number is implemented using two {@link BigInteger}s, one for the numferator, one for denominator
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class RationalNumber extends Number
    implements
    ScalarFieldElement<RationalNumber>,
    SignedNumber<RationalNumber>,
    Ordered<RationalNumber> {

    public static final RationalNumber ONE = new RationalNumber(BigInteger.ONE, BigInteger.ONE);
    public static final RationalNumber ZERO = new RationalNumber(BigInteger.ZERO, BigInteger.ONE);

    @Getter
    private final @NotNull BigInteger numerator;
    @Getter
    private final @NotNull @Positive BigInteger denominator;

    public static RationalNumber of(long numerator, long denominator) {
        return new RationalNumber(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
    }

    public static RationalNumber of(@NotNull BigInteger numerator, @NotNull
        @Positive @Negative BigInteger denominator) {
        return new RationalNumber(numerator, denominator);
    }

    public static RationalNumber of(long longValue) {
        return of(longValue, 1);
    }

    RationalNumber(@NonNull BigInteger numerator, @NonNull BigInteger denominator) throws InvalidElementCreationException {
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
    @NonAlgebraic
    public RationalNumber dividedBy(long divisor) throws DivisionByZeroException {
        return new RationalNumber(
            numerator,
            denominator.multiply(BigInteger.valueOf(divisor)));
    }

    @Override
    public RationalNumber reciprocal() {
        if (numerator.equals(BigInteger.ZERO)) {
            throw new DivisionByZeroException("Denominator cannot be zero");
        }
        return new RationalNumber(denominator, numerator);
    }

    @Override
    public RationalNumber pow(int exponent) {
        int e = exponent;
        try {
            if (e < 0) {
                e = -1 * e;
                return new RationalNumber(
                    denominator.pow(e),
                    numerator.pow(e));
            } else if (e == 0) {
                return ONE;
            } else {
                return new RationalNumber(
                    numerator.pow(e),
                    denominator.pow(e));
            }
        } catch (InvalidElementCreationException invalidElementCreationException) {
            throw new DivisionByZeroException(invalidElementCreationException);
        }
    }

    @Override
    public boolean isZero() {
        return numerator.equals(BigInteger.ZERO);
    }

    @Override
    public RationalNumber sqr() {
        return new RationalNumber(numerator.multiply(numerator), denominator.multiply(denominator));
    }

    @Override
    public RationalNumber dividedBy(RationalNumber divisor) {
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
                numerator.multiply(BigInteger.valueOf(-1L)), denominator);
    }

    @Override
    public RationalNumber minus(RationalNumber subtrahend) {
        return plus(subtrahend.times(-1));
    }

    @Override
    public int compareTo(RationalNumber compare) {
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
            return (isNegative() ? "-" : "") + TextUtils.superscript(numerator.abs().toString()) + "\u2044" + TextUtils.subscript(denominator.toString());
        }
    }

}
