package org.meeuw.math.abstractalgebra;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface RngElement<F extends RngElement<F>> extends AdditiveGroupElement<F>, MultiplicableElement<F> {

    @Override
    Rng<F> structure();
}
