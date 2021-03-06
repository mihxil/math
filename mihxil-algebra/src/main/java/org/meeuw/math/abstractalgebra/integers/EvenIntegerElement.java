package org.meeuw.math.abstractalgebra.integers;

import lombok.Getter;

import java.math.BigDecimal;

import javax.validation.constraints.Min;

import org.meeuw.math.Utils;
import org.meeuw.math.abstractalgebra.RngElement;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.exceptions.ReciprocalException;
import org.meeuw.math.numbers.Scalar;
import org.meeuw.math.numbers.SignedNumber;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class EvenIntegerElement
    implements
    SignedNumber,
    RngElement<EvenIntegerElement>,
    Scalar<EvenIntegerElement> {

    public static final EvenIntegerElement ZERO = EvenIntegerElement.of(0);

    @Getter
    private final long value;

    public static EvenIntegerElement of(long value){
        if (value % 2 == 1) {
            throw new InvalidElementCreationException("The argument mus be even (" + value + " isn't)");
        }
        return new EvenIntegerElement(value);
    }

    private EvenIntegerElement(long value) {
        this.value = value;
    }

    @Override
    public EvenIntegerElement plus(EvenIntegerElement summand) {
        return new EvenIntegerElement(value + summand.value);
    }

    public OddIntegerElement plus(OddIntegerElement summand) {
        return new OddIntegerElement(value + summand.getValue());
    }

    @Override
    public EvenIntegerElement negation() {
        return new EvenIntegerElement(-1 * value);
    }

    @Override
    public EvenIntegerElement minus(EvenIntegerElement subtrahend) {
        return new EvenIntegerElement(value - subtrahend.value);
    }

    @Override
    public EvenIntegers getStructure() {
        return EvenIntegers.INSTANCE;
    }

    @Override
    public EvenIntegerElement times(EvenIntegerElement multiplier) {
        return new EvenIntegerElement(value * multiplier.value);
    }

    @Override
    public EvenIntegerElement pow(@Min(1) int n) {
        if (n == 0) {
            throw new ReciprocalException("" + this + "^0");
        }
        return new EvenIntegerElement(Utils.positivePow(value, n));
    }

    @Override
    public EvenIntegerElement sqr() {
        return new EvenIntegerElement(value * value);
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
    public int compareTo(EvenIntegerElement f) {
        return Long.compare(value, f.value);
    }

    @Override
    public EvenIntegerElement abs() {
        return new EvenIntegerElement(Math.abs(value));
    }

    @Override
    public boolean isZero() {
        return value == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EvenIntegerElement that = (EvenIntegerElement) o;

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
