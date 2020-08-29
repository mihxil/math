package org.meeuw.math.abstractalgebra;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * A division ring or 'skewed field', is non-commutative field.
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface DivisionRing<E extends DivisionRingElement<E>> extends
    MultiplicativeGroup<E>,
    Ring<E> {

    Set<Operator> OPERATORS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(Operator.values())));

    @Override
    default Set<Operator> getSupportedOperators() {
        return OPERATORS;

    }
}
