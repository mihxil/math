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
public class NDivisibleInteger
    extends AbstractIntegerElement<NDivisibleInteger, NDivisibleInteger, NDivisibleIntegers>
    implements
    RngElement<NDivisibleInteger>,
    Scalar<NDivisibleInteger> {


    public static NDivisibleInteger of(int divisor, long value){
        if (value % divisor != 0) {
            throw new InvalidElementCreationException("The argument must be even (" + value + " isn't)");
        }
        return new NDivisibleInteger(NDivisibleIntegers.of(divisor), value);
    }


    NDivisibleInteger(NDivisibleIntegers structure, long value) {
        super(structure, value);
    }

    NDivisibleInteger(NDivisibleIntegers structure, BigInteger value) {
        super(structure, value);
    }

    @Override
    public NDivisibleInteger plus(NDivisibleInteger summand) {
        return new NDivisibleInteger(structure, value.add(summand.value));
    }


    @Override
    public NDivisibleInteger negation() {
        return of(value.negate());
    }

    @Override
    public NDivisibleInteger minus(NDivisibleInteger subtrahend) {
        return of(value.min(subtrahend.value));
    }

    @Override
    public NDivisibleInteger times(NDivisibleInteger multiplier) {
        return of(value.multiply(multiplier.value));
    }

    @Override
    public NDivisibleInteger pow(@Min(1) int n) {
        if (n == 0) {
            throw new ReciprocalException("" + this + "^0");
        }
        return of(value.pow(n));
    }

    @Override
    public NDivisibleInteger sqr() {
        return of(value.multiply(value));
    }

    @Override
    public NDivisibleInteger abs() {
        return of(value.abs());
    }

}
