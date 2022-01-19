package org.meeuw.math.abstractalgebra.integers;

import lombok.Getter;

import java.math.BigDecimal;

import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.abstractalgebra.Ordered;
import org.meeuw.math.numbers.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.9
 */
public abstract class AbstractIntegerElement<E extends AbstractIntegerElement<E, SIZE>, SIZE extends Scalar<SIZE>>
    implements
    SignedNumber<E>,
    AlgebraicElement<E>,
    SizeableScalar<E, SIZE>,
    Ordered<E> {

    @Getter
    protected final long value;

    protected AbstractIntegerElement(long value) {
        this.value = value;
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
    public int compareTo(AbstractIntegerElement f) {
        return Long.compare(value, f.value);
    }



    @Override
    public boolean isZero() {
        return value == 0;
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
