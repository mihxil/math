package org.meeuw.math.abstractalgebra;

import java.util.NavigableSet;

import static org.meeuw.math.Utils.navigableSet;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface CompleteField<E extends CompleteFieldElement<E>> extends ScalarField<E> {

    NavigableSet<Operator> OPERATORS = navigableSet(Operator.values());

    NavigableSet<UnaryOperator> UNARY_OPERATORS = navigableSet(UnaryOperator.values());


    @Override
    default NavigableSet<Operator> getSupportedOperators() {
        return OPERATORS;
    }

    @Override
    default NavigableSet<UnaryOperator> getSupportedUnaryOperators() {
        return UNARY_OPERATORS;
    }

    @Override
    default NavigableSet<ComparisonOperator> getSupportedComparisonOperators() {
        return ComparisonOperator.ALL;
    }

}
