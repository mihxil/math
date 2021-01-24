package org.meeuw.math.abstractalgebra.integers;

import lombok.Getter;

import java.math.BigDecimal;

import javax.validation.constraints.Min;

import org.meeuw.math.Utils;
import org.meeuw.math.abstractalgebra.MultiplicativeMonoidElement;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.numbers.Scalar;
import org.meeuw.math.numbers.SignedNumber;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class OddIntegerElement
    implements
    SignedNumber,
    MultiplicativeMonoidElement<OddIntegerElement>,
    Scalar<OddIntegerElement> {

    public static final OddIntegerElement ONE = OddIntegerElement.of(1);

    @Getter
    private final long value;

    public static OddIntegerElement of(long value){
        if (value % 2 == 0) {
               throw new InvalidElementCreationException("The argument mus be odd (" + value + " isn't)");
        }
        return new OddIntegerElement(value);
    }

    OddIntegerElement(long value) {
        this.value = value;
    }


    @Override
    public OddIntegers getStructure() {
        return OddIntegers.INSTANCE;
    }

    @Override
    public OddIntegerElement times(OddIntegerElement multiplier) {
        return new OddIntegerElement(value * multiplier.value);
    }

    @Override
    public OddIntegerElement pow(@Min(1) int n) {
        return new OddIntegerElement(Utils.positivePow(value, n));
    }

    @Override
    public OddIntegerElement sqr() {
        return new OddIntegerElement(value * value);
    }


    public OddIntegerElement negation() {
        return new OddIntegerElement(-1 * value);
    }

    public OddIntegerElement plus(EvenIntegerElement summand)  {
        return new OddIntegerElement(value + summand.getValue());
    }

    @Override
    public long longValue() {
        return value;
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public BigDecimal bigDecimalValue() {
        return BigDecimal.valueOf(value);
    }

    @Override
    public int signum() {
        return Long.signum(value);
    }

    @Override
    public int compareTo(OddIntegerElement f) {
        return Long.compare(value, f.value);
    }

    @Override
    public OddIntegerElement abs() {
        return new OddIntegerElement(Math.abs(value));
    }

    @Override
    public boolean isZero() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OddIntegerElement that = (OddIntegerElement) o;

        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
