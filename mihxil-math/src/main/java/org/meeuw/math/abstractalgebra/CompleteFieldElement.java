package org.meeuw.math.abstractalgebra;

import org.meeuw.math.exceptions.ReciprocalException;
import org.meeuw.math.numbers.SignedNumber;

/**
 * Elements of a {@link CompleteField}.
 *
 * @see CompleteField
 * @author Michiel Meeuwissen
 * @since 0.4
 * @param <E> Self reference
 */
public interface CompleteFieldElement<E extends CompleteFieldElement<E>>
    extends
    ScalarFieldElement<E>,
    SignedNumber<E> {

    @Override
    CompleteField<E> getStructure();

    E sqrt();

    E sin();

    E cos();

    E pow(E exponent) throws ReciprocalException;

}
