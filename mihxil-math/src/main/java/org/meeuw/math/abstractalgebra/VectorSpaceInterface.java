package org.meeuw.math.abstractalgebra;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface VectorSpaceInterface<E extends ScalarFieldElement<E>, V extends VectorInterface<V, E>> {

    int getDimension();

    V zero();

    ScalarField<E> getField();

}
