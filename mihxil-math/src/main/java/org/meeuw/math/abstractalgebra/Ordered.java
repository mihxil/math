package org.meeuw.math.abstractalgebra;

/**
 *
 * @author Michiel Meeuwissen
 * @since 0.7
 */
public interface Ordered<E extends Ordered<E> & AlgebraicElement<E>>  extends AlgebraicElement<E>, Comparable<E> {

    default boolean lt(E compare) {
        return compareTo(compare) < 0;
    }

    default boolean lte(E compare) {
        return lt(compare) || eq(compare);
    }

    default boolean gt(E compare) {
        return compareTo(compare) > 0;
    }

    default boolean gte(E compare) {
        return gt(compare) || eq(compare);
    }
}
