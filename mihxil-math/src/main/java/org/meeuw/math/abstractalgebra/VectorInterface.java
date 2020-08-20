package org.meeuw.math.abstractalgebra;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface VectorInterface<E extends FieldElement<E>, V extends VectorInterface<E, V>> extends Iterable<E> {

    V times(E multiplier);

    V plus(V summand);

    V inverse();

    E get(int i);

    VectorSpaceInterface<E, V> getSpace();

}
