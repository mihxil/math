package org.meeuw.math.abstractalgebra;

import java.util.NavigableSet;

import static org.meeuw.math.Utils.navigableSet;

import org.meeuw.math.operators.AlgebraicUnaryOperator;

/**
 *  A <a href="https://en.wikipedia.org/wiki/Complete_field">complete field</a> element has no 'gaps', which means e.g. that operations like
 *  * {@link CompleteFieldElement#sqrt()} and trigonometric operations like {@link CompleteFieldElement#sin()} are possible.
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface CompleteScalarField<E extends CompleteScalarFieldElement<E>> extends CompleteField<E>, ScalarField<E> {

    NavigableSet<AlgebraicUnaryOperator> UNARY_OPERATORS = navigableSet(CompleteField.UNARY_OPERATORS, ScalarField.UNARY_OPERATORS);


    @Override
    default NavigableSet<AlgebraicUnaryOperator> getSupportedUnaryOperators() {
        return UNARY_OPERATORS;
    }


}
