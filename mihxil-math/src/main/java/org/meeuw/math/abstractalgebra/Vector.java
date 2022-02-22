package org.meeuw.math.abstractalgebra;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface Vector<V extends Vector<V, S>, S extends FieldElement<S>>
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

    VectorSpace<S, V> getSpace();

}
