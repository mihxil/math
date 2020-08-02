package org.meeuw.math.abstractalgebra;

/**
 * @author Michiel Meeuwissen
 * @since .0.4
 */
public abstract  class AbstractNumberElement<F extends AbstractNumberElement<F>> extends Number
        implements NumberElement<F> {

    @Override
    public int intValue() {
        return (int) longValue();
    }

    @Override
    public float floatValue() {
        return (float) doubleValue();

    }
}
