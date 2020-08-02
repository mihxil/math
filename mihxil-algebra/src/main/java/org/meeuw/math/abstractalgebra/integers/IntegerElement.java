package org.meeuw.math.abstractalgebra.integers;

import org.meeuw.math.abstractalgebra.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class IntegerElement extends AbstractNumberElement<IntegerElement> implements RingElement<IntegerElement> {
    private final long value;

    public static IntegerElement of(long value){
        return new IntegerElement(value);
    }

    public IntegerElement(long value) {
        this.value = value;
    }

    @Override
    public IntegerElement plus(IntegerElement summand) {
        return new IntegerElement(value + summand.value);

    }

    @Override
    public IntegerElement negation() {
        return new IntegerElement(-1 * value);
    }

    @Override
    public Integers structure() {
        return Integers.INSTANCE;
    }

    @Override
    public IntegerElement times(IntegerElement multiplier) {
        return new IntegerElement(value * multiplier.value);
    }

    @Override
    public IntegerElement self() {
        return this;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IntegerElement that = (IntegerElement) o;

        return value == that.value;
    }

    @Override
    public int hashCode() {
        return (int) value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }


    @Override
    public int compareTo(IntegerElement o) {
        return Long.compare(value, o.value);
    }

    @Override
    public long longValue() {
        return value;
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public int compareTo(Number o) {
        return Long.compare(value, o.longValue());

    }
}
