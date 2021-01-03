package org.meeuw.math.abstractalgebra;

import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableSet;
import static org.meeuw.math.abstractalgebra.Operator.DIVISION;
import static org.meeuw.math.abstractalgebra.Operator.MULTIPLICATION;

/**
 * A <a href="https://en.wikipedia.org/wiki/Group_(mathematics)">Group</a> with the binary operation 'multiplication'.
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MultiplicativeGroup<E extends MultiplicativeGroupElement<E>>
    extends MultiplicativeMonoid<E> {

    Set<Operator> OPERATORS = unmodifiableSet(new HashSet<>(asList(MULTIPLICATION, DIVISION)));

    @Override
    default Set<Operator> getSupportedOperators() {
        return OPERATORS;
    }

}
