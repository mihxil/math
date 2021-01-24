package org.meeuw.math.abstractalgebra;

import org.meeuw.math.numbers.SignedNumber;

/**
 * A <a href="https://en.wikipedia.org/wiki/Complete_field">complete field</a> element has no 'gaps', which means e.g. that operations like
 * {@link #sqrt()} and trigonometric operations like {@link #sin()} are possible.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 * @param <E> Self reference
 */
public interface CompleteFieldElement<E extends CompleteFieldElement<E>>
    extends ScalarFieldElement<E>,
    SignedNumber {

    @Override
    CompleteField<E> getStructure();

    E sqrt();

    E sin();

    E cos();

    E pow(E exponent);

}
