package org.meeuw.math.abstractalgebra;

import java.util.NavigableSet;

import static org.meeuw.math.Utils.navigableSet;
import static org.meeuw.math.abstractalgebra.Operator.*;
import static org.meeuw.math.abstractalgebra.UnaryOperator.*;

/**
 * <a href="https://en.wikipedia.org/wiki/Field_(mathematics)">Field</a>
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface Field<E extends FieldElement<E>> extends
    DivisionRing<E>,
    AbelianRing<E>,
    DivisibleGroup<E>,
    Group<E> {

    NavigableSet<Operator> OPERATORS = navigableSet(OPERATION, ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION);

    NavigableSet<UnaryOperator> UNARY_OPERATORS = navigableSet(IDENTIFY, NEGATION, RECIPROCAL);



    @Override
    default NavigableSet<Operator> getSupportedOperators() {
        return OPERATORS;
    }

    @Override
    default NavigableSet<UnaryOperator> getSupportedUnaryOperators() {
        return UNARY_OPERATORS;
    }

    @Override
    default boolean multiplicationIsCommutative() {
        return true;
    }

    @Override
    E one();

    default Operator groupOperator() {
        return Operator.MULTIPLICATION;
    }

    @Override
    default E unity() {
        return groupOperator().unity(this);
    }

}
