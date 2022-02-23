package org.meeuw.math.abstractalgebra.integers;

import java.math.BigInteger;

import javax.validation.constraints.Min;

import org.meeuw.math.abstractalgebra.MultiplicativeMonoidElement;
import org.meeuw.math.exceptions.InvalidElementCreationException;
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


    public static OddInteger of(long value){
        if (value % 2 == 0) {
               throw new InvalidElementCreationException("The argument must be odd (" + value + " isn't)");
        }
        return new OddInteger(value);
    }

    OddInteger(long value) {
        this(BigInteger.valueOf(value));
    }

    OddInteger(BigInteger value) {
        super(OddIntegers.INSTANCE, value);
    }


    @Override
    public OddIntegers getStructure() {
        return OddIntegers.INSTANCE;
    }

    @Override
    public OddInteger times(OddInteger multiplier) {
        return of(value.multiply(multiplier.value));
    }

    @Override
    public OddInteger pow(@Min(1) int n) {
        return of(value.pow(n));
    }

    @Override
    public OddInteger sqr() {
        return of(value.multiply(value));
    }

    public OddInteger negation() {
        return of(value.negate());
    }

    public OddInteger plus(EvenInteger summand)  {
        return of(value.add(summand.value));
    }

    @Override
    public OddInteger abs() {
        return of(value.abs());
    }

    @Override
    public boolean isZero() {
        return false;
    }

}
