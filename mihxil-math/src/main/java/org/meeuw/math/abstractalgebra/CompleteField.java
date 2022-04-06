package org.meeuw.math.abstractalgebra;

import java.util.NavigableSet;

import org.meeuw.math.operators.*;

import static org.meeuw.math.CollectionUtils.navigableSet;
import static org.meeuw.math.operators.BasicAlgebraicBinaryOperator.POWER;
import static org.meeuw.math.operators.BasicAlgebraicUnaryOperator.*;

/**
 *  A <a href="https://en.wikipedia.org/wiki/Complete_field">complete field</a> element has no 'gaps', which means e.g. that operations like
 *  * {@link CompleteFieldElement#sqrt()} and trigonometric operations like {@link CompleteFieldElement#sin()} are possible.
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface CompleteField<E extends CompleteFieldElement<E>> extends Field<E> {

    NavigableSet<AlgebraicBinaryOperator> OPERATORS = navigableSet(ScalarField.OPERATORS, POWER);

    NavigableSet<AlgebraicUnaryOperator> UNARY_OPERATORS = navigableSet(ScalarField.UNARY_OPERATORS, SQRT, SIN, COS, EXP, LN, SINH, COSH);

    E pi();

    E e();


    @Override
    default NavigableSet<AlgebraicBinaryOperator> getSupportedOperators() {
        return OPERATORS;
    }

    @Override
    default NavigableSet<AlgebraicUnaryOperator> getSupportedUnaryOperators() {
        return UNARY_OPERATORS;
    }

    @Override
    default E determinant(E[][] source) {
        // we have comparison and abs, we could use Gaussion elimination with partial pivoting
        return Field.super.determinant(source);
    }

}
