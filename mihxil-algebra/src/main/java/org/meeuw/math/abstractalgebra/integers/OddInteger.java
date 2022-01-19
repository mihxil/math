package org.meeuw.math.abstractalgebra.integers;

import javax.validation.constraints.Min;

import org.meeuw.math.Utils;
import org.meeuw.math.abstractalgebra.MultiplicativeMonoidElement;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.numbers.Scalar;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class OddInteger
    extends AbstractIntegerElement<OddInteger, OddInteger>
    implements
    MultiplicativeMonoidElement<OddInteger>,
    Scalar<OddInteger>  {

    public static final OddInteger ONE = OddInteger.of(1);


    public static OddInteger of(long value){
        if (value % 2 == 0) {
               throw new InvalidElementCreationException("The argument mus be odd (" + value + " isn't)");
        }
        return new OddInteger(value);
    }

    OddInteger(long value) {
        super(value);
    }


    @Override
    public OddIntegers getStructure() {
        return OddIntegers.INSTANCE;
    }

    @Override
    public OddInteger times(OddInteger multiplier) {
        return new OddInteger(value * multiplier.value);
    }

    @Override
    public OddInteger pow(@Min(1) int n) {
        return new OddInteger(Utils.positivePow(value, n));
    }

    @Override
    public OddInteger sqr() {
        return new OddInteger(value * value);
    }

    public OddInteger negation() {
        return new OddInteger(-1 * value);
    }

    public OddInteger plus(EvenInteger summand)  {
        return new OddInteger(value + summand.getValue());
    }

    @Override
    public int compareTo(OddInteger f) {
        return Long.compare(value, f.value);
    }

    @Override
    public OddInteger abs() {
        return new OddInteger(Math.abs(value));
    }

    @Override
    public boolean isZero() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OddInteger that = (OddInteger) o;

        return value == that.value;
    }

}
