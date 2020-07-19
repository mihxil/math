package org.meeuw.math.abstractalgebra;

import java.util.Set;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AlgebraicStructure<F extends AlgebraicElement<F, A>, A extends AlgebraicStructure<F, A>> {

    Set<Operator> supportedOperators();

    default boolean supports(Operator operator) {
        return supportedOperators().contains(operator);
    }
}
