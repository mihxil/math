package org.meeuw.math.abstractalgebra;

import java.util.NavigableSet;

import org.meeuw.math.Utils;

import static org.meeuw.math.abstractalgebra.Operator.ADDITION;
import static org.meeuw.math.abstractalgebra.Operator.SUBTRACTION;

/**
 *  A <a href="https://en.wikipedia.org/wiki/Group_(mathematics)">Group</a> with the binary operation 'addition'.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AdditiveGroup<E extends AdditiveGroupElement<E>> extends AdditiveMonoid<E>  {

    NavigableSet<Operator> OPERATORS = Utils.navigableSet(ADDITION, SUBTRACTION);

    @Override
    default NavigableSet<Operator> getSupportedOperators() {
        return OPERATORS;
    }

}
