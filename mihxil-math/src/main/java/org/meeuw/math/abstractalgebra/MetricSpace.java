package org.meeuw.math.abstractalgebra;

import org.meeuw.math.numbers.Scalar;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 * @param <E> self reference
 * @param <S> the type of the distance
 */
public interface MetricSpace<E extends MetricSpaceElement<E, S>, S extends Scalar<S>> {

    E zero();

    default S metric(E e1, E e2) {
        return e1.distanceTo(e2);
    }
}
