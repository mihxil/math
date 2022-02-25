package org.meeuw.math.abstractalgebra.integers;

import java.math.BigInteger;

import javax.validation.constraints.Min;

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
    Scalar<OddInteger>  {

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
    public OddInteger pow(@Min(1) int n) {
        return super.pow(n);
    }

    @Override
    public OddInteger sqr() {
        return with(value.multiply(value));
    }

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

}
