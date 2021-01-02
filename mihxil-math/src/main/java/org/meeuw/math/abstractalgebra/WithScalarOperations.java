package org.meeuw.math.abstractalgebra;

/**
 * Some structures can naturally be multiplied by a scalar.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 * @param <E> Self reference
 * @param <S> The type of the scalars
 */
public interface WithScalarOperations<E, S extends ScalarFieldElement<S>> {

    /**
     * Multiplies the current object with a scalar and returns a new instance
     */
    E times(S multiplier);

    /**
     * Divides the current object with a scalar and returns a new instance
     */
    E dividedBy(S divisor);

}
