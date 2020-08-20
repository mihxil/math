package org.meeuw.math.abstractalgebra;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface VectorSpaceInterface<E extends FieldElement<E>, V extends VectorInterface<E, V>> {

    int getDimension();

    V zero();

    Field<E> getField();


}
