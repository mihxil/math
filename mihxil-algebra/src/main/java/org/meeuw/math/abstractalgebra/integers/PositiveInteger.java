package org.meeuw.math.abstractalgebra.integers;

import java.math.BigInteger;

import javax.validation.constraints.Min;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.numbers.Scalar;

import static org.meeuw.math.abstractalgebra.integers.PositiveIntegers.INSTANCE;

/**
 * The natural numbers ℕ+
 * @author Michiel Meeuwissen
 * @since 0.8
 */
public class PositiveInteger
    extends
    AbstractIntegerElement<PositiveInteger, PositiveInteger, PositiveIntegers>
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

    public PositiveInteger(@Min(1) BigInteger value) {
        super(INSTANCE, value);
        if (value.compareTo(BigInteger.ZERO) <= 0) {
            throw new InvalidElementCreationException("Positive numbers cannot be 0 or negative");
        }
    }
    public PositiveInteger(@Min(1) long value) {
        this(BigInteger.valueOf(value));
    }

    @Override
    public PositiveInteger plus(PositiveInteger summand) {
        return of(value.add(summand.value));
    }

    @Override
    public PositiveInteger times(PositiveInteger summand) {
        return of(value.multiply(summand.value));
    }

    @Override
    public PositiveInteger abs() {
        return this;
    }

}
