package org.meeuw.math.abstractalgebra.integers;

import java.math.BigInteger;

import javax.validation.constraints.Min;

import org.meeuw.math.abstractalgebra.*;
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
        return PositiveIntegers.INSTANCE.newElement(BigInteger.valueOf(value));
    }

    protected PositiveInteger(@Min(1) BigInteger value) {
        super(INSTANCE, value);
    }

    @Override
    public PositiveInteger plus(PositiveInteger summand) {
        return with(value.add(summand.value));
    }

    @Override
    public PositiveInteger times(PositiveInteger summand) {
        return with(value.multiply(summand.value));
    }

    @Override
    public PositiveInteger operate(PositiveInteger operand) {
        return times(operand);
    }

    @Override
    public PositiveInteger abs() {
        return this;
    }

}
