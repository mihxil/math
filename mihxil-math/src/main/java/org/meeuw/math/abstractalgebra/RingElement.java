package org.meeuw.math.abstractalgebra;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
public interface RingElement<F extends RingElement<F>> extends RngElement<F> {

    @Override
    Ring<F> structure();
}
