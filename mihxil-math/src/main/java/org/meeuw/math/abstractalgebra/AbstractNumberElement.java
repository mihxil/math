package org.meeuw.math.abstractalgebra;

import java.math.BigDecimal;

/**
 * Abstract implementation of {@link NumberElement}. Extends from {@link Number}, and implements some of its methods.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public abstract  class AbstractNumberElement<E extends AbstractNumberElement<E>> extends Number
        implements NumberElement<E> {

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
