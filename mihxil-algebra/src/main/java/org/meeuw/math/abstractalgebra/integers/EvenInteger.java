package org.meeuw.math.abstractalgebra.integers;

import java.math.BigInteger;

import javax.validation.constraints.Min;

import org.meeuw.math.abstractalgebra.RngElement;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.exceptions.ReciprocalException;
import org.meeuw.math.numbers.Scalar;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class EvenInteger
    extends AbstractIntegerElement<EvenInteger, EvenInteger, EvenIntegers>
    implements
    RngElement<EvenInteger>,
    Scalar<EvenInteger> {

    public static final EvenInteger ZERO = EvenInteger.of(0);

    public static EvenInteger of(long value){
        if (value % 2 == 1) {
            throw new InvalidElementCreationException("The argument mus be even (" + value + " isn't)");
        }
        return new EvenInteger(value);
    }

    private EvenInteger(long value) {
        super(EvenIntegers.INSTANCE, value);
    }

    EvenInteger(BigInteger value) {
        super(EvenIntegers.INSTANCE, value);
    }


    public OddInteger plus(OddInteger summand) {
        return new OddInteger(value.add(summand.getValue()));
    }

    @Override
    public EvenInteger negation() {
        return new EvenInteger( value.negate());
    }

    @Override
    public EvenInteger minus(EvenInteger subtrahend) {
        return of(value.subtract(subtrahend.value));
    }


    @Override
    public EvenInteger times(EvenInteger multiplier) {
        return of(value.multiply(multiplier.value));
    }

    @Override
    public EvenInteger pow(@Min(1) int n) {
        if (n == 0) {
            throw new ReciprocalException("" + this + "^0");
        }
        return of(value.pow(n));
    }

    @Override
    public EvenInteger sqr() {
        return of(value.multiply(value));
    }

    @Override
    public EvenInteger abs() {
        return of(value.abs());
    }



    @Override
    public EvenInteger plus(EvenInteger summand) {
        return of(value.add(summand.value));
    }
}
