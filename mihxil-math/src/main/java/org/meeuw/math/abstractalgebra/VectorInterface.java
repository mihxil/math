package org.meeuw.math.abstractalgebra;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface VectorInterface<V extends VectorInterface<V, S>, S extends ScalarFieldElement<S>>
    extends Iterable<S>,
    AbelianRingElement<V>,
    WithScalarOperations<V, S> {

    @Override
    V times(S multiplier);

    @Override
    V plus(V summand);

    S dot(V multiplier);

    //V cross(V multiplier);

    @Override
    V negation();

    S get(int i) throws ArrayIndexOutOfBoundsException;

    VectorSpaceInterface<S, V> getSpace();

}
