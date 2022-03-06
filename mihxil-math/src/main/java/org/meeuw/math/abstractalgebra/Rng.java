package org.meeuw.math.abstractalgebra;

import java.util.NavigableSet;

import static org.meeuw.math.Utils.navigableSet;

/**
 * Like a {@link Ring} but without multiplicative identity.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface Rng<E extends RngElement<E>> extends AdditiveAbelianGroup<E>, MultiplicativeSemiGroup<E> {

    NavigableSet<Operator> OPERATORS = navigableSet(AdditiveAbelianGroup.OPERATORS, MultiplicativeSemiGroup.OPERATORS);

    NavigableSet<UnaryOperator> UNARY_OPERATORS = navigableSet(AdditiveAbelianGroup.UNARY_OPERATORS, MultiplicativeSemiGroup.UNARY_OPERATORS);


    @Override
    default NavigableSet<Operator> getSupportedOperators() {
        return OPERATORS;
    }

    @Override
    default NavigableSet<UnaryOperator> getSupportedUnaryOperators() {
        return UNARY_OPERATORS;
    }

}
