package org.meeuw.math.abstractalgebra;

import java.util.NavigableSet;

import org.meeuw.math.operators.*;

import static org.meeuw.math.CollectionUtils.navigableSet;
import static org.meeuw.math.operators.BasicAlgebraicBinaryOperator.DIVISION;

/**
 * A <a href="https://en.wikipedia.org/wiki/Group_(mathematics)">Group</a> with the binary operation 'multiplication'.
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MultiplicativeGroup<E extends MultiplicativeGroupElement<E>>
    extends MultiplicativeMonoid<E>, Group<E> {

    NavigableSet<AlgebraicBinaryOperator> OPERATORS = navigableSet(MultiplicativeMonoid.OPERATORS, DIVISION);

    NavigableSet<AlgebraicUnaryOperator> UNARY_OPERATORS = navigableSet(
        MultiplicativeMonoid.UNARY_OPERATORS,
        navigableSet(BasicAlgebraicUnaryOperator.RECIPROCAL),
        Group.UNARY_OPERATORS
    );


    @Override
    default NavigableSet<AlgebraicBinaryOperator> getSupportedOperators() {
        return OPERATORS;
    }

    @Override
    default NavigableSet<AlgebraicUnaryOperator> getSupportedUnaryOperators() {
        return UNARY_OPERATORS;
    }

    @Override
    default E unity() {
        return one();
    }

}
