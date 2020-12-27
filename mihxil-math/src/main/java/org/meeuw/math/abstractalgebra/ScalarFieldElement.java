package org.meeuw.math.abstractalgebra;

import org.meeuw.math.numbers.Scalar;

/**
 * A field element that is also a scalar, e.g. it is very much like a number.
 **
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface ScalarFieldElement<E extends ScalarFieldElement<E>> extends
    FieldElement<E>,
    Scalar<E> {

    @Override
    ScalarField<E> getStructure();

    @Override
    default boolean isZero() {
        return FieldElement.super.isZero();
    }

}
