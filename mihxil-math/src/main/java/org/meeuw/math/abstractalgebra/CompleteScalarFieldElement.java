package org.meeuw.math.abstractalgebra;

/**
 * Elements of a {@link CompleteField}.
 *
 * @see CompleteField
 * @author Michiel Meeuwissen
 * @since 0.4
 * @param <E> Self reference
 */
public interface CompleteScalarFieldElement<E extends CompleteScalarFieldElement<E>>
    extends
    CompleteFieldElement<E>,
    ScalarFieldElement<E> {

    @Override
    CompleteScalarField<E> getStructure();

}
