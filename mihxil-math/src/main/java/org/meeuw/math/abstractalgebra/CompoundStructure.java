package org.meeuw.math.abstractalgebra;

/**
 * An algebraic structure of which the elements are built from elements of another structure. E.g. complex numbers and quaternions consist of real numbers
 * (or some other {@link ScalarField}), matrices and vectors of their entries, polynomials of their coefficients.
 *
 * @param <ES> the type of the structure of the building blocks
 * @since 0.24
 */
public interface CompoundStructure<ES extends AlgebraicStructure<?>> {

    /**
     * @return the structure of which the elements are the building blocks of the elements of this structure
     */
    ES getElementStructure();

    /**
     * Whether every element consists of a fixed number of elements of the {@link #getElementStructure() element structure} (e.g. 2 for complex numbers, 4 for quaternions).
     * This is not the case for e.g. polynomials, which can have any number of coefficients.
     */
    default boolean isFiniteDimensional() {
        return true;
    }
}
