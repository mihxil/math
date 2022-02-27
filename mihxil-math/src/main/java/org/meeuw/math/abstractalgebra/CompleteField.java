package org.meeuw.math.abstractalgebra;

import java.util.NavigableSet;

import static org.meeuw.math.Utils.navigableSet;
import static org.meeuw.math.abstractalgebra.Operator.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface CompleteField<E extends CompleteFieldElement<E>> extends ScalarField<E> {

    NavigableSet<Operator> OPERATORS = navigableSet(
        OPERATION,

        ADDITION,
        SUBTRACTION,

        MULTIPLICATION,
        DIVISION,

        POWER
    );

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

    @Override
    default E determinant(E[][] source) {
        // we have comparison and abs, we could use Gaussion elimination with partial pivoting
        return ScalarField.super.determinant(source);
    }


}
