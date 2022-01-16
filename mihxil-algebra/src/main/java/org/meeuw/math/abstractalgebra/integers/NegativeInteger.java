package org.meeuw.math.abstractalgebra.integers;

import javax.validation.constraints.Max;

import org.meeuw.math.abstractalgebra.AdditiveSemiGroupElement;
import org.meeuw.math.abstractalgebra.Ordered;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.numbers.SizeableScalar;

/**
 * The natural numbers ℕ+
 * @author Michiel Meeuwissen
 * @since 0.8
 */
public class NegativeInteger
    extends  AbstractIntegerElement<NegativeInteger, PositiveInteger>
    implements
    AdditiveSemiGroupElement<NegativeInteger>,
    SizeableScalar<NegativeInteger, PositiveInteger>,
    Ordered<NegativeInteger>
{
    public static final NegativeInteger MINUS_ONE = of(-1);

    public static NegativeInteger of(@Max(-1) long value) {
        return new NegativeInteger(value);
    }

    public NegativeInteger(@Max(-1) long value) {
        super(value);
        if (value >= 0) {
            throw new InvalidElementCreationException("Negative numbers cannot be 0 or positive");
        }
    }

    @Override
    public NegativeInteger plus(NegativeInteger summand) {
        return of(value + summand.value);
    }

    @Override
    public NegativeIntegers getStructure() {
        return NegativeIntegers.INSTANCE;
    }

    @Override
    public int compareTo(NegativeInteger naturalNumber) {
        return Long.compare(value, naturalNumber.value);
    }

    @Override
    public PositiveInteger abs() {
        return new PositiveInteger(Math.abs(value));
    }

    @Override
    public int signum() {
        return -1;
    }

    @Override
    public boolean isZero() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NegativeInteger that = (NegativeInteger) o;

        return value == that.value;
    }
}
