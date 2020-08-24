package org.meeuw.math.abstractalgebra;

/**
 * The base interface for elements of algebraic structures.
 *
 * Every element in a algebraic structure has at least a reference to the {@link AlgebraicStructure} where it is an element
 * of. See {@link #getStructure()}.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 * @param <E> The self type
 */
public interface AlgebraicElement<E extends AlgebraicElement<E>> {

    /**
     * Returns the {@link AlgebraicStructure} associated with the object.
     */
    AlgebraicStructure<E> getStructure();



}
