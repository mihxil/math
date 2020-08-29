package org.meeuw.math.abstractalgebra;

/**
 * Marker interface for groups which are abelian, i.e. commutative.
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MultiplicativeAbelianGroup<E extends MultiplicativeGroupElement<E>>
    extends MultiplicativeGroup<E> {
}
