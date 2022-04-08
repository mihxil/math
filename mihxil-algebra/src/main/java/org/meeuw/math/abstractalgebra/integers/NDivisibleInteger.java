package org.meeuw.math.abstractalgebra.integers;

import jakarta.validation.constraints.Positive;

import java.math.BigInteger;

import org.meeuw.math.abstractalgebra.GroupElement;
import org.meeuw.math.abstractalgebra.RngElement;
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
    Scalar<NDivisibleInteger>,
    GroupElement<NDivisibleInteger> {


    public static NDivisibleInteger of(int divisor, long value){
        return NDivisibleIntegers.of(divisor).newElement(BigInteger.valueOf(value));
    }

    NDivisibleInteger(NDivisibleIntegers structure, long value) {
        this(structure, BigInteger.valueOf(value));
    }

    NDivisibleInteger(NDivisibleIntegers structure, BigInteger value) {
        super(structure, value);
    }

    @Override
    public NDivisibleInteger plus(NDivisibleInteger summand) {
        return with(value.add(summand.value));
    }

    @Override
    public NDivisibleInteger negation() {
        return with(value.negate());
    }

    @Override
    public NDivisibleInteger minus(NDivisibleInteger subtrahend) {
        return with(value.subtract(subtrahend.value));
    }

    @Override
    public NDivisibleInteger times(NDivisibleInteger multiplier) {
        return with(value.multiply(multiplier.value));
    }

    @Override
    public NDivisibleInteger pow(@Positive int n) {
        if (n == 0 && structure.divisor != 1) {
            throw new ReciprocalException("" + this + "^0");
        }
        return super.pow(n);
    }

    @Override
    public NDivisibleInteger sqr() {
        return with(value.multiply(value));
    }

    @Override
    public NDivisibleInteger abs() {
        return with(value.abs());
    }

}
