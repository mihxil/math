package org.meeuw.math.abstractalgebra.integers;

import java.math.BigInteger;

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
    AbstractIntegerElement<NaturalNumber, NaturalNumber, NaturalNumbers>
    implements
    MultiplicativeMonoidElement<NaturalNumber>,
    AdditiveMonoidElement<NaturalNumber>,
    Scalar<NaturalNumber>,
    Ordered<NaturalNumber>
{
    public static final NaturalNumber ZERO = of(0);
    public static final NaturalNumber ONE = of(1);

    public static NaturalNumber of(@Min(0) long value) {
        return new NaturalNumber(BigInteger.valueOf(value));
    }

    public NaturalNumber(@Min(0) BigInteger value) {
        super(NaturalNumbers.INSTANCE, value);
        if (value.compareTo(BigInteger.ZERO) < 0) {
            throw new InvalidElementCreationException("Natural numbers must be non-negative");
        }
    }

    @Override
    public NaturalNumber plus(NaturalNumber summand) {
        return of(value.add(summand.value));
    }

    @Override
    public NaturalNumber times(NaturalNumber summand) {
        return of(value.multiply(summand.value));
    }

    @Override
    public NaturalNumber abs() {
        return this;
    }

}
