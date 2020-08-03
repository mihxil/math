package org.meeuw.math.abstractalgebra;

/**
 * A ring is a {@link AdditiveGroup}, but also defines multiplication, though an inverse {@link MultiplicativeGroupElement#reciprocal()} is not defined (That would make it a {@link Field})
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface Ring<F extends RingElement<F>> extends Rng<F> {

    F one();
}
