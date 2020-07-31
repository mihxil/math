package org.meeuw.math.abstractalgebra.rationalnumbers;

import org.meeuw.math.abstractalgebra.NumberFieldElement;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class RationalNumber implements NumberFieldElement<RationalNumber> {

    private final BigInteger numerator;
    private @NotNull final BigInteger denominator;

    public static RationalNumber of(long numerator, @NotNull long denominator) {
        return new RationalNumber(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
    }

    public static RationalNumber of(long longValue) {
        return of(longValue, 1);
    }

    public RationalNumber(BigInteger numerator, BigInteger denominator) {
        BigInteger gcd = numerator.abs().gcd(denominator.abs());
        boolean nn = numerator.abs().equals(numerator);
        boolean dn = denominator.abs().equals(denominator);
        boolean positive = (nn && dn) || (!nn && !dn);
        this.numerator = positive ? numerator.abs().divide(gcd) : numerator.abs().divide(gcd).negate();
        this.denominator = denominator.abs().divide(gcd);
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
            return structure().one();
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
        return numerator  + (denominator.equals(BigInteger.ONE) ? "" : ("/" + denominator));
    }

}