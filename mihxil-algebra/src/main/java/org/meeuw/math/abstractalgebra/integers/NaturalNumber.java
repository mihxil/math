package org.meeuw.math.abstractalgebra.integers;

import java.math.BigInteger;

import javax.validation.constraints.Min;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.numbers.Scalar;

/**
 * The natural numbers ℕ
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class NaturalNumber extends
    AbstractIntegerElement<NaturalNumber, NaturalNumber, NaturalNumbers>
    implements
    MultiplicativeMonoidElement<NaturalNumber>,
    AdditiveMonoidElement<NaturalNumber>,
    Scalar<NaturalNumber>,
    Ordered<NaturalNumber>,
    Factoriable<NaturalNumber> {

    public static final NaturalNumber ZERO = of(0);
    public static final NaturalNumber ONE = of(1);

    public static NaturalNumber of(@Min(0) long value) {
        return NaturalNumbers.INSTANCE.newElement(BigInteger.valueOf(value));
    }

    protected NaturalNumber(@Min(0) BigInteger value) {
        super(NaturalNumbers.INSTANCE, value);
    }

    @Override
    public NaturalNumber plus(NaturalNumber summand) {
        return with(value.add(summand.value));
    }

    @Override
    public NaturalNumber times(NaturalNumber summand) {
        return with(value.multiply(summand.value));
    }

    @Override
    public NaturalNumber operate(NaturalNumber operand) {
        return MultiplicativeMonoidElement.super.operate(operand);
    }

    @Override
    public NaturalNumber abs() {
        return this;
    }

    @Override
    public NaturalNumber factorial() {
        return new NaturalNumber(bigIntegerFactorial());
    }
}
