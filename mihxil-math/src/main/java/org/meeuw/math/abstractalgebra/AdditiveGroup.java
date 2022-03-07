package org.meeuw.math.abstractalgebra;

import java.util.NavigableSet;

import static org.meeuw.math.Utils.navigableSet;
import static org.meeuw.math.abstractalgebra.Operator.*;
import static org.meeuw.math.abstractalgebra.UnaryOperator.NEGATION;

/**
 *  A <a href="https://en.wikipedia.org/wiki/Group_(mathematics)">Group</a> with the binary operation 'addition'.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AdditiveGroup<E extends AdditiveGroupElement<E>> extends AdditiveMonoid<E>, Group<E>  {

    NavigableSet<Operator> OPERATORS = navigableSet(AdditiveMonoid.OPERATORS, SUBTRACTION);

    NavigableSet<UnaryOperator> UNARY_OPERATORS = navigableSet(AdditiveMonoid.UNARY_OPERATORS, NEGATION);


    @Override
    default NavigableSet<Operator> getSupportedOperators() {
        return OPERATORS;
    }

    @Override
    default NavigableSet<UnaryOperator> getSupportedUnaryOperators() {
        return UNARY_OPERATORS;
    }

    @Override
    default E unity() {
        return zero();
    }

}
