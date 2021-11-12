package org.meeuw.math.abstractalgebra;

import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableSet;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface CompleteField<E extends CompleteFieldElement<E>> extends ScalarField<E> {

    Set<Operator> OPERATORS = unmodifiableSet(new HashSet<>(asList(Operator.values())));

    Set<UnaryOperator> UNARY_OPERATORS = unmodifiableSet(new HashSet<>(asList(UnaryOperator.values())));


    @Override
    default Set<Operator> getSupportedOperators() {
        return OPERATORS;
    }

    @Override
    default Set<UnaryOperator> getSupportedUnaryOperators() {
        return UNARY_OPERATORS;
    }

    @Override
    default Set<ComparisonOperator> getSupportedComparisonOperators() {
        return ComparisonOperator.ALL;
    }

}
