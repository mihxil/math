package org.meeuw.math.abstractalgebra;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface VectorInterface<V extends VectorInterface<V, S>, S extends ScalarFieldElement<S>>
    extends Iterable<S>,
    WithScalarOperations<V, S> {

    @Override
    V times(S multiplier);

    V plus(V summand);

    S dot(V multiplier);

    V negation();

    S get(int i);

    VectorSpaceInterface<S, V> getSpace();

}
