package org.meeuw.math.abstractalgebra;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AlgebraicElement<F extends AlgebraicElement<F, A>, A extends AlgebraicStructure<F, A>> {

    /**
     * Returns the {@link AlgebraicStructure} associated with the object.
     */
    A structure();

    /**
     * Returns the object itself. This exists to have 'this' available in default methods.
     */
    F self();
}
