package org.meeuw.math.abstractalgebra;

import java.util.NavigableSet;

import static org.meeuw.math.Utils.navigableSet;

import org.meeuw.math.operators.AlgebraicBinaryOperator;

import static org.meeuw.math.operators.BasicAlgebraicBinaryOperator.ADDITION;

/**
 * The algebraic structure that only defines addition. There might be no additive identity {@link AdditiveMonoid#zero()}
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AdditiveSemiGroup<E extends AdditiveSemiGroupElement<E>> extends Magma<E> {

    NavigableSet<AlgebraicBinaryOperator> OPERATORS = navigableSet(Magma.OPERATORS, ADDITION);

    @Override
    default NavigableSet<AlgebraicBinaryOperator> getSupportedOperators() {
        return OPERATORS;
    }

    default boolean additionIsCommutative() {
        return false;
    }

}
