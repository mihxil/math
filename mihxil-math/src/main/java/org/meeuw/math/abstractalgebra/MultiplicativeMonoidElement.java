package org.meeuw.math.abstractalgebra;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MultiplicativeMonoidElement<E extends MultiplicativeMonoidElement<E>> extends MultiplicativeSemiGroupElement<E> {

    MultiplicativeMonoid<E> structure();

    E times(E multiplier);
}
