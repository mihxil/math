package org.meeuw.math.abstractalgebra;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface VectorSpace<E extends FieldElement<E>, V extends Vector<E, V>> {

    int getDimension();

    V zero();

    Field<E> getField();


}
