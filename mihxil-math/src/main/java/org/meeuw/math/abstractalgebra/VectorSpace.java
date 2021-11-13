package org.meeuw.math.abstractalgebra;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface VectorSpace<E extends ScalarFieldElement<E>, V extends Vector<V, E>> extends AbelianRing<V> {

    int getDimension();

    @Override
    V zero();

    ScalarField<E> getField();

}
