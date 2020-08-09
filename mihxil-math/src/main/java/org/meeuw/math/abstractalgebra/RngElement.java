package org.meeuw.math.abstractalgebra;

/**
 * Like {@link RingElement}, but the structure is not a complete {@link Ring}, but merely a {@link Rng}
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface RngElement<E extends RngElement<E>> extends AdditiveGroupElement<E>, MultiplicativeSemiGroupElement<E> {

    @Override
    Rng<E> getStructure();
}
