package org.meeuw.math.abstractalgebra.rationalnumbers;

import lombok.Getter;

import java.math.*;

import javax.validation.constraints.NotNull;

import org.meeuw.math.Utils;
import org.meeuw.math.abstractalgebra.NumberFieldElement;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class RationalNumber implements NumberFieldElement<RationalNumber> {

    public static final RationalNumber ONE = new RationalNumber(BigInteger.ONE, BigInteger.ONE);
    public static final RationalNumber ZERO = new RationalNumber(BigInteger.ZERO, BigInteger.ONE);

    @Getter
    private final BigInteger numerator;
    @Getter
    private @NotNull final BigInteger denominator;

    public static RationalNumber of(long numerator, @NotNull long denominator) {
        return new RationalNumber(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
    }

    public static RationalNumber of(long longValue) {
        return of(longValue, 1);
    }

    public RationalNumber(BigInteger numerator, BigInteger denominator) {
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
    public RationalNumbers structure() {
        return new RationalNumbers();
    }

    @Override
    public RationalNumber self() {
        return this;
    }

    @Override
    public RationalNumber pow(int exponent) {
        int e = exponent;
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
    }

    @Override
    public RationalNumber plus(RationalNumber summand) {
        return new RationalNumber(
                numerator.multiply(summand.denominator)
                        .add(summand.numerator.multiply(denominator)),
                denominator.multiply(summand.denominator)
        );

    }

    @Override
    public RationalNumber negation() {
        return new RationalNumber(
                numerator.multiply(BigInteger.valueOf(-1L)), denominator);
    }

    @Override
    public int compareTo(RationalNumber compare) {
        return numerator.multiply(compare.denominator).compareTo(compare.numerator.multiply(denominator));
    }

    @Override
    public int compareTo(Number o) {
        return Double.compare(doubleValue(), o.doubleValue());
    }

    @Override
    public RationalNumber times(RationalNumber multiplier) {
        return new RationalNumber(
                numerator.multiply(multiplier.numerator),
                denominator.multiply(multiplier.denominator)
        );
    }

    @Override
    public long longValue() {
        return (long) doubleValue();
    }

    @Override
    public double doubleValue() {
        return numerator.doubleValue() / denominator.doubleValue();
    }

    private static final  MathContext MATH_CONTEXT = new MathContext(40);
    public BigDecimal bigDecimalValue() {
        return new BigDecimal(numerator).divide(new BigDecimal(denominator), MATH_CONTEXT);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RationalNumber that = (RationalNumber) o;

        if (numerator != null ? !numerator.equals(that.numerator) : that.numerator != null) return false;
        return denominator != null ? denominator.equals(that.denominator) : that.denominator == null;
    }

    @Override
    public int hashCode() {
        int result = numerator != null ? numerator.hashCode() : 0;
        result = 31 * result + (denominator != null ? denominator.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        if (denominator.equals(BigInteger.ONE)) {
            return numerator.toString();
        } else {
            return (isNegative() ? "-" : "") + Utils.superscript(numerator.abs().toString()) + "\u2044" + Utils.subscript(denominator.toString());
        }
    }

}
