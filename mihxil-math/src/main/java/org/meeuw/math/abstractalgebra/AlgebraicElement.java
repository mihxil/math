package org.meeuw.math.abstractalgebra;

/**
 * The base interface for elements of algebraic structures.
 *
 * Every element in a algebraic structure has at least a reference to the {@link AlgebraicStructure} where it is an element
 * of. See {@link #structure()}.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AlgebraicElement<F extends AlgebraicElement<F>> {

    /**
     * Returns the {@link AlgebraicStructure} associated with the object.
     */
    AlgebraicStructure<F> structure();

    /**
     * Returns the object itself. This exists to have 'this' available in default methods.
     */
    F self();
}
