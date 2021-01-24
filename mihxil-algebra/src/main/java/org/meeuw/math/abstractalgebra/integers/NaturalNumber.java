package org.meeuw.math.abstractalgebra.integers;

import java.math.BigDecimal;

import javax.validation.constraints.Min;

import org.meeuw.math.abstractalgebra.AdditiveMonoidElement;
import org.meeuw.math.abstractalgebra.MultiplicativeMonoidElement;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.numbers.Scalar;

/**
 * The natural numbers ℕ
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class NaturalNumber  implements
    MultiplicativeMonoidElement<NaturalNumber>,
    AdditiveMonoidElement<NaturalNumber>,
    Scalar<NaturalNumber>
{
    public static final NaturalNumber ZERO = of(0);
    public static final NaturalNumber ONE = of(1);

    private final @Min(0) long value;

    public static NaturalNumber of(@Min(0) long value) {
        return new NaturalNumber(value);
    }

    public NaturalNumber(@Min(0) long value) {
        if (value < 0) {
            throw new InvalidElementCreationException("Natural numbers must be non-negative");
        }
        this.value = value;
    }

    @Override
    public NaturalNumber plus(NaturalNumber summand) {
        return of(value + summand.value);
    }

    @Override
    public NaturalNumbers getStructure() {
        return NaturalNumbers.INSTANCE;
    }

    @Override
    public NaturalNumber times(NaturalNumber summand) {
        return of(value * summand.value);
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
        return new BigDecimal(value);
    }

    @Override
    public int compareTo(NaturalNumber naturalNumber) {
        return Long.compare(value, naturalNumber.value);
    }

    @Override
    public NaturalNumber abs() {
        return this;
    }

    @Override
    public int signum() {
        return Long.signum(value);
    }

    @Override
    public boolean isZero() {
        return value == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NaturalNumber that = (NaturalNumber) o;

        return value == that.value;
    }

    @Override
    public int hashCode() {
        return (int) (value ^ (value >>> 32));
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
