package org.meeuw.math.abstractalgebra.integers;

import javax.validation.constraints.Min;

import org.meeuw.math.Utils;
import org.meeuw.math.abstractalgebra.Ordered;
import org.meeuw.math.abstractalgebra.RngElement;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.exceptions.ReciprocalException;
import org.meeuw.math.numbers.Scalar;
import org.meeuw.math.numbers.SignedNumber;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class NDivisibleInteger
    extends AbstractIntegerElement<NDivisibleInteger, NDivisibleInteger>
    implements
    SignedNumber,
    RngElement<NDivisibleInteger>,
    Scalar<NDivisibleInteger>,
    Ordered<NDivisibleInteger> {

    private final NDivisibleIntegers structure;

    public static NDivisibleInteger of(int divisor, long value){
        if (value % divisor != 0) {
            throw new InvalidElementCreationException("The argument must be even (" + value + " isn't)");
        }
        return new NDivisibleInteger(NDivisibleIntegers.of(divisor), value);
    }


    NDivisibleInteger(NDivisibleIntegers structure, long value) {
        super(value);
        this.structure = structure;
    }

    @Override
    public NDivisibleInteger plus(NDivisibleInteger summand) {
        return new NDivisibleInteger(structure, value + summand.value);
    }


    @Override
    public NDivisibleInteger negation() {
        return new NDivisibleInteger(structure,-1 * value);
    }

    @Override
    public NDivisibleInteger minus(NDivisibleInteger subtrahend) {
        return new NDivisibleInteger(structure, value - subtrahend.value);
    }

    @Override
    public NDivisibleIntegers getStructure() {
        return structure;
    }

    @Override
    public NDivisibleInteger times(NDivisibleInteger multiplier) {
        return new NDivisibleInteger(structure, value * multiplier.value);
    }

    @Override
    public NDivisibleInteger pow(@Min(1) int n) {
        if (n == 0) {
            throw new ReciprocalException("" + this + "^0");
        }
        return new NDivisibleInteger(structure, Utils.positivePow(value, n));
    }

    @Override
    public NDivisibleInteger sqr() {
        return new NDivisibleInteger(structure, value * value);
    }

    @Override
    public NDivisibleInteger abs() {
        return new NDivisibleInteger(structure, Math.abs(value));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NDivisibleInteger that = (NDivisibleInteger) o;

        return value == that.value;
    }
}
