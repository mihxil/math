package org.meeuw.math.abstractalgebra;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class IntegerElement implements AdditiveGroupElement<IntegerElement, IntegersGroup> {
    private final int value;

    public IntegerElement(int value) {
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
    public IntegersGroup structure() {
        return IntegersGroup.INSTANCE;

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
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
