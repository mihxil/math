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

    default Set<Operator> getSupportedOperators() {
        return Collections.emptySet();
    }

    default boolean supports(Operator operator) {
        return getSupportedOperators().contains(operator);
    }

    Cardinality getCardinality();

    Class<E> getElementClass();

}
