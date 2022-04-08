package org.meeuw.math.abstractalgebra.integers;

import jakarta.validation.constraints.Positive;

import java.math.BigInteger;

import org.meeuw.math.NonAlgebraic;
import org.meeuw.math.abstractalgebra.MultiplicativeMonoidElement;
import org.meeuw.math.numbers.Scalar;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class OddInteger
    extends AbstractIntegerElement<OddInteger, OddInteger, OddIntegers>
    implements
    MultiplicativeMonoidElement<OddInteger>,
    Scalar<OddInteger>,
    Factoriable<IntegerElement> {


    public static final OddInteger ONE = OddInteger.of(1);

    public static OddInteger of(BigInteger value){
        return OddIntegers.INSTANCE.newElement(value);
    }
    public static OddInteger of(long value){
        return of(BigInteger.valueOf(value));
    }


    OddInteger(BigInteger value) {
        super(OddIntegers.INSTANCE, value);
    }

    @Override
    public OddInteger times(OddInteger multiplier) {
        return with(value.multiply(multiplier.value));
    }

    @Override
    public OddInteger pow(@Positive int n) {
        return super.pow(n);
    }

    @Override
    public OddInteger sqr() {
        return with(value.multiply(value));
    }

    /**
     * Negation can be done, but this addition can't be!
     */
    public OddInteger negation() {
        return with(value.negate());
    }

    public OddInteger plus(EvenInteger summand)  {
        return with(value.add(summand.value));
    }

    @Override
    public OddInteger abs() {
        return with(value.abs());
    }

    @Override
    public boolean isZero() {
        return false;
    }

    @Override
    @NonAlgebraic
    public IntegerElement factorial() {
        return new IntegerElement(bigIntegerFactorial());
    }


}
