package org.meeuw.math.abstractalgebra;

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
}
