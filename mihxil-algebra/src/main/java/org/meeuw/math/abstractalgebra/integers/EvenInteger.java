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
public class EvenInteger
    extends AbstractIntegerElement<EvenInteger, EvenInteger>
    implements
    SignedNumber,
    RngElement<EvenInteger>,
    Scalar<EvenInteger>,
    Ordered<EvenInteger> {

    public static final EvenInteger ZERO = EvenInteger.of(0);

    public static EvenInteger of(long value){
        if (value % 2 == 1) {
            throw new InvalidElementCreationException("The argument mus be even (" + value + " isn't)");
        }
        return new EvenInteger(value);
    }

    private EvenInteger(long value) {
        super(value);
    }

    @Override
    public EvenInteger plus(EvenInteger summand) {
        return new EvenInteger(value + summand.value);
    }

    public OddInteger plus(OddInteger summand) {
        return new OddInteger(value + summand.getValue());
    }

    @Override
    public EvenInteger negation() {
        return new EvenInteger(-1 * value);
    }

    @Override
    public EvenInteger minus(EvenInteger subtrahend) {
        return new EvenInteger(value - subtrahend.value);
    }

    @Override
    public EvenIntegers getStructure() {
        return EvenIntegers.INSTANCE;
    }

    @Override
    public EvenInteger times(EvenInteger multiplier) {
        return new EvenInteger(value * multiplier.value);
    }

    @Override
    public EvenInteger pow(@Min(1) int n) {
        if (n == 0) {
            throw new ReciprocalException("" + this + "^0");
        }
        return new EvenInteger(Utils.positivePow(value, n));
    }

    @Override
    public EvenInteger sqr() {
        return new EvenInteger(value * value);
    }

    @Override
    public EvenInteger abs() {
        return new EvenInteger(Math.abs(value));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EvenInteger that = (EvenInteger) o;

        return value == that.value;
    }
}
