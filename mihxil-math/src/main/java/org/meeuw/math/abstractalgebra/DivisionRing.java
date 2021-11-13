package org.meeuw.math.abstractalgebra;

import java.util.NavigableSet;

import static org.meeuw.math.Utils.navigableSet;
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

    NavigableSet<Operator> OPERATORS = navigableSet(ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION);

    @Override
    E one();

    @Override
    default NavigableSet<Operator> getSupportedOperators() {
        return OPERATORS;

    }
}
