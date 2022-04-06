package org.meeuw.math.abstractalgebra;

import org.meeuw.math.exceptions.ReciprocalException;

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
    FieldElement<E> {

    @Override
    CompleteField<E> getStructure();

    E sqrt();

    E sin();

    E cos();

    default E pow(E exponent) throws ReciprocalException {
        return (ln().times(exponent)).exp();
    }

    E exp();

    E ln();

    default E sinh() {
        return exp().minus(negation().exp()).dividedBy(2);
    }
    default E cosh() {
        return exp().plus(negation().exp()).dividedBy(2);
    }

}
