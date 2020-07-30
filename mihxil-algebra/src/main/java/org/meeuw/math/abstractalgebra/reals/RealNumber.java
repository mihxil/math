package org.meeuw.math.abstractalgebra.reals;

import org.meeuw.math.abstractalgebra.AlgebraicNumber;
import org.meeuw.math.abstractalgebra.NumberFieldElement;

import java.util.Arrays;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class RealNumber extends AlgebraicNumber<RealNumber> implements  NumberFieldElement<RealNumber> {

    public static final RealNumber ONE = new RealNumber(1d);
    public static final RealNumber ZERO = new RealNumber(0d);

    public final double value;

    public static RealNumber of(Double value) {
        return new RealNumber(value);
    }

    public static RealNumber[] of(double... values) {
        return Arrays.stream(values)
            .mapToObj(v -> RealNumber.of(Double.valueOf(v))).toArray(RealNumber[]::new);
    }

    public RealNumber(Double value) {
        this.value = value;
    }

    @Override
    public RealNumber plus(RealNumber summand) {
        return new RealNumber(value + summand.value);
    }

    @Override
    public RealNumber negation() {
        return new RealNumber(-1 * value);
    }

    @Override
    public RealNumber times(RealNumber multiplier) {
        return new RealNumber(value * multiplier.value);
    }

    @Override
    public RealNumber pow(int exponent) {
        return new RealNumber(Math.pow(value, exponent));
    }

    @Override
    public RealField structure() {
        return RealField.INSTANCE;
    }

    @Override
    public RealNumber self() {
        return this;
    }


    @Override
    public long longValue() {
        return Math.round(value);
    }

    @Override
    public double doubleValue() {
        return value;
    }

    public RealNumber times(double multiplier) {
        return new RealNumber(value * multiplier);
    }

    @Override
    public int compareTo(Number o) {
        return Double.compare(value, o.doubleValue());
    }

    @Override
    public int compareTo(RealNumber o) {
        return Double.compare(value, o.value);

    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RealNumber that = (RealNumber) o;

        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }

}
