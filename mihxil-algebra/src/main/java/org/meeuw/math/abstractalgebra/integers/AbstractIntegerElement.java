package org.meeuw.math.abstractalgebra.integers;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.numbers.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.9
 */
public abstract class AbstractIntegerElement<
    E extends AbstractIntegerElement<E, SIZE, S>,
    SIZE extends Scalar<SIZE>,
    S extends AbstractIntegers<E, SIZE, S>
    >
    extends AbstractAlgebraicElement<E, S> implements
    SignedNumber<E>,
    AlgebraicElement<E>,
    SizeableScalar<E, SIZE>,
    Ordered<E> {

    @Getter
    protected final BigInteger value;



    protected AbstractIntegerElement(S structure, long value) {
        this(structure, BigInteger.valueOf(value));
    }

    protected AbstractIntegerElement(S structure, BigInteger value) {
        super(structure);
        this.value = value;
    }

    protected E of(BigInteger value) {
        return structure.of(value);
    }

    @Override
    public long longValue() {
        return value.longValue();
    }

    @Override
    public double doubleValue() {
        return value.doubleValue();
    }

    @Override
    public BigDecimal bigDecimalValue() {
        return new BigDecimal(value.toString());
    }

    @Override
    public int signum() {
        return value.signum();
    }

    @Override
    public int compareTo(E f) {
        return value.compareTo(f.value);
    }

    @Override
    public boolean isZero() {
        return value.longValue() == 0;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        E that = (E) o;

        return value.equals(that.value);
    }
    @Override
    public String toString() {
        return String.valueOf(value);
    }

}
