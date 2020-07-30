package org.meeuw.math.abstractalgebra;

/**
 * Like a {@link Ring} but without multiplicative identify.
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface Rng<F extends RngElement<F>> extends AdditiveGroup<F> {

}
