package org.meeuw.math.abstractalgebra;

import java.util.NavigableSet;

import static org.meeuw.math.Utils.navigableSet;
import static org.meeuw.math.abstractalgebra.Operator.DIVISION;
import static org.meeuw.math.abstractalgebra.Operator.MULTIPLICATION;

/**
 * A <a href="https://en.wikipedia.org/wiki/Group_(mathematics)">Group</a> with the binary operation 'multiplication'.
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MultiplicativeGroup<E extends MultiplicativeGroupElement<E>>
    extends MultiplicativeMonoid<E> {

    NavigableSet<Operator> OPERATORS = navigableSet(MULTIPLICATION, DIVISION);

    @Override
    default NavigableSet<Operator> getSupportedOperators() {
        return OPERATORS;
    }

}
