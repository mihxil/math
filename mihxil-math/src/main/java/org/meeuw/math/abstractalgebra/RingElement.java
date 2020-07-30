package org.meeuw.math.abstractalgebra;

/**
 * An element of a {@link Ring}, so something that can be added, subtracted and multiplied by each other.
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface RingElement<F extends RingElement<F>> extends RngElement<F> {

    @Override
    Ring<F> structure();
}
