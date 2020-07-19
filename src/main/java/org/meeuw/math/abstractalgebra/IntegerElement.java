package org.meeuw.math.abstractalgebra;

/**
 * @author Michiel Meeuwissen
 * @since ...
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
}
