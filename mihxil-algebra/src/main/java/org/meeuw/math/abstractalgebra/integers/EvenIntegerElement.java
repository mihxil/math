package org.meeuw.math.abstractalgebra.integers;

import org.meeuw.math.abstractalgebra.RngElement;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class EvenIntegerElement implements RngElement<EvenIntegerElement> {
    private final int value;

    public static EvenIntegerElement of(int value){
        return new EvenIntegerElement(value);
    }

    public EvenIntegerElement(int value) {
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
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
