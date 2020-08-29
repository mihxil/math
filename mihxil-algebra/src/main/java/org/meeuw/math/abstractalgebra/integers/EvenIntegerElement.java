package org.meeuw.math.abstractalgebra.integers;

import java.math.BigDecimal;

import org.meeuw.math.abstractalgebra.AbstractNumberElement;
import org.meeuw.math.abstractalgebra.RngElement;
import org.meeuw.math.numbers.SignedNumber;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class EvenIntegerElement extends AbstractNumberElement<EvenIntegerElement>
    implements SignedNumber<EvenIntegerElement>, RngElement<EvenIntegerElement> {

    public static final EvenIntegerElement ZERO = EvenIntegerElement.of(0);

    private final long value;

    public static EvenIntegerElement of(long value){
        return new EvenIntegerElement(value);
    }

    public EvenIntegerElement(long value) {
        if (value % 2 == 1) {
            throw new IllegalArgumentException();
        }
        this.value = value;
    }

    @Override
    public EvenIntegerElement plus(EvenIntegerElement summand) {
        return new EvenIntegerElement(value + summand.value);

    }

    @Override
    public EvenIntegerElement negation() {
        return new EvenIntegerElement(-1 * value);
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
    public int signum() {
        return Long.signum(value);
    }

    @Override
    public int compareTo(EvenIntegerElement f) {
        return Long.compare(value, f.value);
    }
}
