package org.meeuw.math.numbers;

/**
 * Sizeable objects have {@link #abs()}
 *
 * TODO is the difference between {@link org.meeuw.math.abstractalgebra.MetricSpaceElement} and Sizeable Meaningful?
 * are there objects which are one, but not the other?
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 *
 */
public interface Sizeable<E extends Scalar<E>> {

    E abs();
}
