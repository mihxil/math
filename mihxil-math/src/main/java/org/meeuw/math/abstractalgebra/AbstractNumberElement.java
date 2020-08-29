package org.meeuw.math.abstractalgebra;

import java.math.BigDecimal;

import org.meeuw.math.numbers.Numerical;

/**
 * Abstract implementation of {@link Numerical}. Extends from {@link Number}, and implements some of its methods.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public abstract  class AbstractNumberElement<E extends AbstractNumberElement<E>> extends Number
        implements Numerical<E> {

    @Override
    public int intValue() {
        return (int) longValue();
    }

    @Override
    public float floatValue() {
        return (float) doubleValue();
    }

    @Override
    public int compareTo(Number o) {
        return bigDecimalValue().compareTo(new BigDecimal(o.toString()));
    }
}
