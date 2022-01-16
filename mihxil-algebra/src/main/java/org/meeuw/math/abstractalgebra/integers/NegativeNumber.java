package org.meeuw.math.abstractalgebra.integers;

import java.math.BigDecimal;

import javax.validation.constraints.Max;

import org.meeuw.math.abstractalgebra.AdditiveSemiGroupElement;
import org.meeuw.math.abstractalgebra.Ordered;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.numbers.SizeableScalar;

/**
 * The natural numbers ℕ+
 * @author Michiel Meeuwissen
 * @since 0.8
 */
public class NegativeNumber implements
    AdditiveSemiGroupElement<NegativeNumber>,
    SizeableScalar<NegativeNumber, PositiveNumber>,
    Ordered<NegativeNumber>
{
    public static final NegativeNumber  MINUS_ONE = of(-1);


    private final @Max(-1) long value;

    public static NegativeNumber of(@Max(-1) long value) {
        return new NegativeNumber(value);
    }

    public NegativeNumber(@Max(-1) long value) {
        if (value >= 0) {
            throw new InvalidElementCreationException("Negative numbers cannot be 0 or positive");
        }
        this.value = value;
    }

    @Override
    public NegativeNumber plus(NegativeNumber summand) {
        return of(value + summand.value);
    }

    @Override
    public NegativeNumbers getStructure() {
        return NegativeNumbers.INSTANCE;
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
    public int compareTo(NegativeNumber naturalNumber) {
        return Long.compare(value, naturalNumber.value);
    }

    @Override
    public PositiveNumber abs() {
        return new PositiveNumber(Math.abs(value));
    }

    @Override
    public int signum() {
        return -1;
    }

    @Override
    public boolean isZero() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NegativeNumber that = (NegativeNumber) o;

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
