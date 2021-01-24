package org.meeuw.math.abstractalgebra;

import java.util.*;

import static java.util.Collections.unmodifiableSet;
import static org.meeuw.math.abstractalgebra.Operator.ADDITION;
import static org.meeuw.math.abstractalgebra.Operator.SUBTRACTION;

/**
 *  A <a href="https://en.wikipedia.org/wiki/Group_(mathematics)">Group</a> with the binary operation 'addition'.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AdditiveGroup<E extends AdditiveGroupElement<E>> extends AdditiveMonoid<E>  {

    Set<Operator> OPERATORS = unmodifiableSet(new HashSet<>(Arrays.asList(ADDITION, SUBTRACTION)));

    @Override
    default Set<Operator> getSupportedOperators() {
        return OPERATORS;
    }

}
