package org.meeuw.math.abstractalgebra;

import java.util.NavigableSet;

import org.meeuw.math.operators.AlgebraicBinaryOperator;
import org.meeuw.math.operators.AlgebraicUnaryOperator;

import static org.meeuw.math.CollectionUtils.navigableSet;

/**
 * <a href="https://en.wikipedia.org/wiki/Field_(mathematics)">Field</a>
 *
 * For simplicity it is both a {@link AdditiveGroup} and a {@link MultiplicativeGroup}, which is not absolutely correct, because it contains one element {@link #zero()} that has no multiplicative {@link MultiplicativeGroupElement#reciprocal()}.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */

public interface Field<E extends FieldElement<E>> extends
    DivisionRing<E>,
    AbelianRing<E>,
    DivisibleGroup<E> {

    NavigableSet<AlgebraicBinaryOperator> OPERATORS = navigableSet(DivisionRing.OPERATORS, AbelianRing.OPERATORS);

    NavigableSet<AlgebraicUnaryOperator> UNARY_OPERATORS = navigableSet(DivisionRing.UNARY_OPERATORS, AbelianRing.UNARY_OPERATORS);


    @Override
    default NavigableSet<AlgebraicBinaryOperator> getSupportedOperators() {
        return OPERATORS;
    }

    @Override
    default NavigableSet<AlgebraicUnaryOperator> getSupportedUnaryOperators() {
        return UNARY_OPERATORS;
    }

    // explicit to make proxying possible (DocumentationTest)
    @Override
    E one();


    @Override
    default boolean multiplicationIsCommutative() {
        return true;
    }


}
