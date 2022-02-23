package org.meeuw.math.abstractalgebra;

import java.io.Serializable;

/**
 * The base interface for elements of algebraic structures.
 *
 * Every element in a algebraic structure has at least a reference to the {@link AlgebraicStructure} of wich it is an element
 * of. See {@link #getStructure()}.
 *
 * An algebraic element should basicly be unmodifiable, at least in the sense that it from the start on  should represent the same <em>value</em>.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 * @param <E> The self type
 */
public interface AlgebraicElement<E extends AlgebraicElement<E>> extends Serializable {

    /**
     * @return the {@link AlgebraicStructure} associated with the object.
     */
    AlgebraicStructure<E> getStructure();

    default E self() {
        return (E) this;
    }

    /**
     * The operator
     */
    default boolean eq(E other) {
        return equals(other);
    }

}
