package org.meeuw.math.abstractalgebra;

import java.util.NavigableSet;

import static org.meeuw.math.CollectionUtils.navigableSet;

import org.meeuw.math.operators.AlgebraicBinaryOperator;
import org.meeuw.math.operators.AlgebraicUnaryOperator;

import static org.meeuw.math.operators.BasicAlgebraicBinaryOperator.SUBTRACTION;
import static org.meeuw.math.operators.BasicAlgebraicUnaryOperator.NEGATION;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AdditiveAbelianGroup<E extends AdditiveGroupElement<E>>
    extends AdditiveGroup<E>, AdditiveAbelianSemiGroup<E> {

    NavigableSet<AlgebraicBinaryOperator> OPERATORS = navigableSet(AdditiveMonoid.OPERATORS, SUBTRACTION);

    NavigableSet<AlgebraicUnaryOperator> UNARY_OPERATORS = navigableSet(AdditiveMonoid.UNARY_OPERATORS, NEGATION);


    @Override
    default boolean additionIsCommutative() {
        return true;
    }
}
