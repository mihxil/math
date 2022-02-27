package org.meeuw.math.abstractalgebra;

import java.util.NavigableSet;

import static org.meeuw.math.Utils.navigableSet;

/**
 * The algebraic structure that only defines addition. There might be no additive identity {@link AdditiveMonoid#zero()}
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AdditiveSemiGroup<E extends AdditiveSemiGroupElement<E>> extends Magma<E> {

    NavigableSet<Operator> OPERATORS = navigableSet(Operator.ADDITION);

    NavigableSet<UnaryOperator> UNARY_OPERATORS = navigableSet(UnaryOperator.NEGATION);

    @Override
    default NavigableSet<Operator> getSupportedOperators() {
        return OPERATORS;
    }

    @Override
    default NavigableSet<UnaryOperator> getSupportedUnaryOperators() {
        return UNARY_OPERATORS;
    }

    default boolean additionIsCommutative() {
        return false;
    }

}
