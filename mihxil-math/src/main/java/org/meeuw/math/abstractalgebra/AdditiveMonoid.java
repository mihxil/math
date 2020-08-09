package org.meeuw.math.abstractalgebra;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AdditiveMonoid<E extends AdditiveMonoidElement<E>> extends AlgebraicStructure<E> {

    Set<Operator> OPERATORS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(Operator.ADDITION)));

    /**
     * The additive group by definition has an element that is 'zero',  the additive identity element.
     */
    E zero();

    @Override
    default Set<Operator> getSupportedOperators() {
        return OPERATORS;
    }

}
