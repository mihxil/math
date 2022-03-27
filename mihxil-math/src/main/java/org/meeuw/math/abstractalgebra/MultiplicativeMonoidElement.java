package org.meeuw.math.abstractalgebra;

import javax.validation.constraints.Min;

import org.meeuw.math.exceptions.ReciprocalException;

/**
 * An element of the {@link MultiplicativeMonoid} structure.
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MultiplicativeMonoidElement<E extends MultiplicativeMonoidElement<E>>
    extends MultiplicativeSemiGroupElement<E> {

    @Override
    MultiplicativeMonoid<E> getStructure();

    /**
     * if multiplication is defined, then so is exponentiation, as long as the exponent is non-negative
     *
     * This default implementation is based on {@link MultiplicativeSemiGroupElement#pow(int)}
     * If the argument is {@code 0}, then return {@link #getStructure()}{@link MultiplicativeMonoid#one()}
     */
    @Override
    default E pow(@Min(0) int n) {
        if (n < 0) {
            throw new ReciprocalException("");
        }
        if (n == 0) {
            return getStructure().one();
        }
        return MultiplicativeSemiGroupElement.super.pow(n);
    }


    default boolean isOne() {
        return getStructure().one().equals(this);
    }


}
