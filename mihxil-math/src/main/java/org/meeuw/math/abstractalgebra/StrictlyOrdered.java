package org.meeuw.math.abstractalgebra;

import org.meeuw.math.exceptions.NotComparableException;

/**
 *
 * Provides operator {@link #lt} and {@link #gt} and guarantees that these are transitive.
 *
 * {@link #lte} and {@link #gte} are provided too, but not necessarily transitively.
 *
 * @author Michiel Meeuwissen
 * @since 0.7
 */
public interface StrictlyOrdered<E extends StrictlyOrdered<E>>  extends Comparable<E> {

    default boolean lt(E compare) {
        return compareTo(compare) < 0 && ! this.equals(compare);
    }

    default boolean gt(E compare) {
        return compareTo(compare) > 0 && ! this.equals(compare);
    }

    default boolean lte(E compare) {
        return lt(compare) || equals(compare);
    }

    default boolean gte(E compare) {
        return gt(compare) || equals(compare);
    }

    @Override
    int compareTo(E o) throws NotComparableException;
}
