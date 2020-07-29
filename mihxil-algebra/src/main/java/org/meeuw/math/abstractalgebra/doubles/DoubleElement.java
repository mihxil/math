package org.meeuw.math.abstractalgebra.doubles;

import java.util.Arrays;

import org.meeuw.math.abstractalgebra.NumberFieldElement;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class DoubleElement extends Number implements  NumberFieldElement<DoubleElement, DoubleField> {

    public static final DoubleElement ONE = new DoubleElement(1d);
    public static final DoubleElement ZERO = new DoubleElement(0d);

    public final Double value;

    public static DoubleElement of(Double value) {
        return new DoubleElement(value);
    }

    public static DoubleElement[] of(double... values) {
        return Arrays.stream(values)
            .mapToObj(v -> DoubleElement.of(Double.valueOf(v))).toArray(DoubleElement[]::new);
    }

    public DoubleElement(Double value) {
        this.value = value;
    }

    @Override
    public DoubleElement plus(DoubleElement summand) {
        return new DoubleElement(value + summand.value);
    }

    @Override
    public DoubleElement negation() {
        return new DoubleElement(-1 * value);
    }

    @Override
    public DoubleElement times(DoubleElement multiplier) {
        return new DoubleElement(value * multiplier.value);
    }

    @Override
    public DoubleElement pow(int exponent) {
        return new DoubleElement(Math.pow(value, exponent));
    }

    @Override
    public DoubleField structure() {
        return DoubleField.INSTANCE;
    }

    @Override
    public DoubleElement self() {
        return this;
    }

    @Override
    public int intValue() {
        return value.intValue();
    }

    @Override
    public long longValue() {
        return value.longValue();
    }

    @Override
    public float floatValue() {
        return value.floatValue();
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public DoubleElement times(double multiplier) {
        return new DoubleElement(value * multiplier);
    }

    @Override
    public int compareTo(Number o) {
        return value.compareTo(o.doubleValue());
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DoubleElement that = (DoubleElement) o;

        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
