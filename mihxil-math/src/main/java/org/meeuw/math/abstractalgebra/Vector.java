package org.meeuw.math.abstractalgebra;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface Vector<E extends FieldElement<E>, V extends Vector<E, V>> extends Iterable<E> {

    V times(E multiplier);

    V plus(V summand);

    V inverse();

    E get(int i);

    VectorSpace<E, V> getSpace();

}
