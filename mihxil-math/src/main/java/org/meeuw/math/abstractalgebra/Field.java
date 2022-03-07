package org.meeuw.math.abstractalgebra;

import java.util.NavigableSet;

import static org.meeuw.math.Utils.navigableSet;

/**
 * <a href="https://en.wikipedia.org/wiki/Field_(mathematics)">Field</a>
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface Field<E extends FieldElement<E>> extends
    DivisionRing<E>,
    AbelianRing<E>,
    DivisibleGroup<E> {

    NavigableSet<Operator> OPERATORS = navigableSet(DivisionRing.OPERATORS, AbelianRing.OPERATORS);

    NavigableSet<UnaryOperator> UNARY_OPERATORS = navigableSet(DivisionRing.UNARY_OPERATORS, AbelianRing.UNARY_OPERATORS);


    @Override
    default NavigableSet<Operator> getSupportedOperators() {
        return OPERATORS;
    }

    @Override
    default NavigableSet<UnaryOperator> getSupportedUnaryOperators() {
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
