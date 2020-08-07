package org.meeuw.math.abstractalgebra;

import java.util.*;

/**
 * A <a href="https://en.wikipedia.org/wiki/Group_(mathematics)">Group</a> with the binary operation 'multiplication'.
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MultiplicativeGroup<E extends MultiplicativeGroupElement<E>> extends MultiplicativeMonoid<E> {

    Set<Operator> OPERATORS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(Operator.MULTIPLICATION, Operator.DIVISION)));

    @Override
    default Set<Operator> supportedOperators() {
        return OPERATORS;
    }

}
