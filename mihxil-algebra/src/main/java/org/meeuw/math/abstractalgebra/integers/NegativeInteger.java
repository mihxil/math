package org.meeuw.math.abstractalgebra.integers;

import java.math.BigInteger;

import javax.validation.constraints.Max;

import org.meeuw.math.NonAlgebraic;
import org.meeuw.math.abstractalgebra.AdditiveSemiGroupElement;
import org.meeuw.math.abstractalgebra.Ordered;
import org.meeuw.math.numbers.SizeableScalar;

/**
 * The natural numbers ℕ+
 * @author Michiel Meeuwissen
 * @since 0.8
 */
public class NegativeInteger
    extends  AbstractIntegerElement<NegativeInteger, PositiveInteger, NegativeIntegers>
    implements
    AdditiveSemiGroupElement<NegativeInteger>,
    SizeableScalar<NegativeInteger, PositiveInteger>,
    Ordered<NegativeInteger>
{
    public static final NegativeInteger MINUS_ONE = of(-1);

    public static NegativeInteger of(@Max(-1) long value) {
        return NegativeIntegers.INSTANCE.newElement(BigInteger.valueOf(value));
    }

    protected NegativeInteger(@Max(-1) BigInteger value) {
        super(NegativeIntegers.INSTANCE, value);
    }

    @Override
    public NegativeInteger plus(NegativeInteger summand) {
        return with(value.add(summand.value));
    }

    @Override
    @NonAlgebraic
    public PositiveInteger abs() {
        return new PositiveInteger(value.abs());
    }

    @Override
    public int signum() {
        return -1;
    }

    @Override
    public boolean isZero() {
        return false;
    }

}
