package org.meeuw.math.abstractalgebra.integers;

import javax.validation.constraints.Min;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.numbers.Scalar;

/**
 * The natural numbers ℕ
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class NaturalNumber extends
    AbstractIntegerElement<NaturalNumber, NaturalNumber>
    implements
    MultiplicativeMonoidElement<NaturalNumber>,
    AdditiveMonoidElement<NaturalNumber>,
    Scalar<NaturalNumber>,
    Ordered<NaturalNumber>
{
    public static final NaturalNumber ZERO = of(0);
    public static final NaturalNumber ONE = of(1);

    public static NaturalNumber of(@Min(0) long value) {
        return new NaturalNumber(value);
    }

    public NaturalNumber(@Min(0) long value) {
        super(value);
        if (value < 0) {
            throw new InvalidElementCreationException("Natural numbers must be non-negative");
        }
    }

    @Override
    public NaturalNumber plus(NaturalNumber summand) {
        return of(value + summand.value);
    }

    @Override
    public NaturalNumbers getStructure() {
        return NaturalNumbers.INSTANCE;
    }

    @Override
    public NaturalNumber times(NaturalNumber summand) {
        return of(value * summand.value);
    }

    @Override
    public int compareTo(NaturalNumber naturalNumber) {
        return Long.compare(value, naturalNumber.value);
    }

    @Override
    public NaturalNumber abs() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NaturalNumber that = (NaturalNumber) o;

        return value == that.value;
    }
}
