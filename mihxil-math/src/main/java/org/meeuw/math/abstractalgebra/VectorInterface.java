package org.meeuw.math.abstractalgebra;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface VectorInterface<S extends ScalarFieldElement<S>, V extends VectorInterface<S, V>>
    extends Iterable<S>,
    WithScalarOperations<V, S> {

    @Override
    V times(S multiplier);

    V plus(V summand);

    V inverse();

    S get(int i);

    VectorSpaceInterface<S, V> getSpace();

}
