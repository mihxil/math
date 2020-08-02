package org.meeuw.math.abstractalgebra.integers;

import org.meeuw.math.abstractalgebra.AbstractNumberElement;
import org.meeuw.math.abstractalgebra.RngElement;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class EvenIntegerElement extends AbstractNumberElement<EvenIntegerElement> implements RngElement<EvenIntegerElement> {
    private final long value;

    public static EvenIntegerElement of(long value){
        return new EvenIntegerElement(value);
    }

    public EvenIntegerElement(long value) {
        if (value % 2 == 1) {
            throw new IllegalArgumentException();
        }
        this.value = value;
    }

    @Override
    public EvenIntegerElement plus(EvenIntegerElement summand) {
        return new EvenIntegerElement(value + summand.value);

    }

    @Override
    public EvenIntegerElement negation() {
        return new EvenIntegerElement(-1 * value);
    }

    @Override
    public int compareTo(EvenIntegerElement compare) {
        return Long.compare(value, compare.value);
    }

    @Override
    public EvenIntegers structure() {
        return EvenIntegers.INSTANCE;
    }

    @Override
    public EvenIntegerElement times(EvenIntegerElement multiplier) {
        return new EvenIntegerElement(value * multiplier.value);
    }

    @Override
    public EvenIntegerElement self() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EvenIntegerElement that = (EvenIntegerElement) o;

        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public int compareTo(Number o) {
        return Long.compare(value, o.longValue());
    }

    @Override
    public long longValue() {
        return value;
    }

    @Override
    public double doubleValue() {
        return 0;
    }
}
