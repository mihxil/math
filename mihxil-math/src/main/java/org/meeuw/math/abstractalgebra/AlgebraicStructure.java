package org.meeuw.math.abstractalgebra;

import java.util.Collections;
import java.util.Set;

/**
 * The base interface of all algebraic structures.
 *
 * If defines what arithmetic {@link Operator}s are possible its elements
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 * @param <E> The type of the elements of this structure
 */
public interface AlgebraicStructure<E extends AlgebraicElement<E>> {

    /**
     * Returns the {@link Operator}s that elements of this structure support.
     */
    default Set<Operator> getSupportedOperators() {
        return Collections.emptySet();
    }

    /**
     * Returns the cardinality of the complete set of this structure.
     */
    Cardinality getCardinality();

     /**
     * Returns the class of the elements of this algebraic structure
     */
    Class<E> getElementClass();

}
