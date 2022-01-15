package org.meeuw.math.abstractalgebra.integers;

import java.math.BigDecimal;

import javax.validation.constraints.Min;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.numbers.Scalar;

/**
 * The natural numbers ℕ+
 * @author Michiel Meeuwissen
 * @since 0.8
 */
public class PositiveNumber implements
    MultiplicativeMonoidElement<PositiveNumber>,
    AdditiveSemiGroupElement<PositiveNumber>,
    Scalar<PositiveNumber>,
    Ordered<PositiveNumber>
{
    public static final PositiveNumber ONE = of(1);

    private final @Min(1) long value;

    public static PositiveNumber of(@Min(1) long value) {
        return new PositiveNumber(value);
    }

    public PositiveNumber(@Min(1) long value) {
        if (value <= 0) {
            throw new InvalidElementCreationException("Positive numbers cannot be 0 or negative");
        }
        this.value = value;
    }

    @Override
    public PositiveNumber plus(PositiveNumber summand) {
        return of(value + summand.value);
    }

    @Override
    public PositiveNumbers getStructure() {
        return PositiveNumbers.INSTANCE;
    }

    @Override
    public PositiveNumber times(PositiveNumber summand) {
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
    public int compareTo(PositiveNumber naturalNumber) {
        return Long.compare(value, naturalNumber.value);
    }

    @Override
    public PositiveNumber abs() {
        return this;
    }

    @Override
    public int signum() {
        return Long.signum(value);
    }

    @Override
    public boolean isZero() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PositiveNumber that = (PositiveNumber) o;

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
