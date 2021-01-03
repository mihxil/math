package org.meeuw.math.abstractalgebra;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.meeuw.math.abstractalgebra.Operator.*;

/**
 * A division ring is a ring, where also the multiplicative inverse is defined, but where multiplication is not necessarily commutative.
 *
 * In other words it is  'skewed field', or  non-commutative field.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface DivisionRing<E extends DivisionRingElement<E>> extends
    MultiplicativeGroup<E>,
    Ring<E> {

    Set<Operator> OPERATORS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION)));

    @Override
    default Set<Operator> getSupportedOperators() {
        return OPERATORS;

    }
}
