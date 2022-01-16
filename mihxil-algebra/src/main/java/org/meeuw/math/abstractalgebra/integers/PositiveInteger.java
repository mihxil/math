package org.meeuw.math.abstractalgebra.integers;

import javax.validation.constraints.Min;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.numbers.Scalar;

/**
 * The natural numbers ℕ+
 * @author Michiel Meeuwissen
 * @since 0.8
 */
public class PositiveInteger
    extends
    AbstractIntegerElement<PositiveInteger, PositiveInteger>
    implements
    MultiplicativeMonoidElement<PositiveInteger>,
    AdditiveSemiGroupElement<PositiveInteger>,
    Scalar<PositiveInteger>,
    Ordered<PositiveInteger>
{
    public static final PositiveInteger ONE = of(1);


    public static PositiveInteger of(@Min(1) long value) {
        return new PositiveInteger(value);
    }

    public PositiveInteger(@Min(1) long value) {
        super(value);
        if (value <= 0) {
            throw new InvalidElementCreationException("Positive numbers cannot be 0 or negative");
        }
    }

    @Override
    public PositiveInteger plus(PositiveInteger summand) {
        return of(value + summand.value);
    }

    @Override
    public PositiveIntegers getStructure() {
        return PositiveIntegers.INSTANCE;
    }

    @Override
    public PositiveInteger times(PositiveInteger summand) {
        return of(value * summand.value);
    }

    @Override
    public int compareTo(PositiveInteger naturalNumber) {
        return Long.compare(value, naturalNumber.value);
    }

    @Override
    public PositiveInteger abs() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PositiveInteger that = (PositiveInteger) o;

        return value == that.value;
    }

}
