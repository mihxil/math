package org.meeuw.math.abstractalgebra;

import java.util.NavigableSet;

import static org.meeuw.math.CollectionUtils.navigableSet;

import org.meeuw.math.operators.AlgebraicBinaryOperator;
import org.meeuw.math.operators.AlgebraicUnaryOperator;

/**
 * Like a {@link Ring} but without multiplicative identity.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface Rng<E extends RngElement<E>> extends AdditiveAbelianGroup<E>, MultiplicativeSemiGroup<E> {

    NavigableSet<AlgebraicBinaryOperator> OPERATORS = navigableSet(AdditiveAbelianGroup.OPERATORS, MultiplicativeSemiGroup.OPERATORS);

    NavigableSet<AlgebraicUnaryOperator> UNARY_OPERATORS = navigableSet(AdditiveAbelianGroup.UNARY_OPERATORS, MultiplicativeSemiGroup.UNARY_OPERATORS, Group.UNARY_OPERATORS);


    @Override
    default NavigableSet<AlgebraicBinaryOperator> getSupportedOperators() {
        return OPERATORS;
    }

    @Override
    default NavigableSet<AlgebraicUnaryOperator> getSupportedUnaryOperators() {
        return UNARY_OPERATORS;
    }

}
