package org.meeuw.math.abstractalgebra;

import java.util.NavigableSet;

import static org.meeuw.math.Utils.navigableSet;
import static org.meeuw.math.abstractalgebra.Operator.MULTIPLICATION;
import static org.meeuw.math.abstractalgebra.UnaryOperator.SQR;

/**
 * The algebraic structure that only defines multiplication. There might be no multiplicatie identity {@link MultiplicativeMonoid#one()}
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MultiplicativeSemiGroup<E extends MultiplicativeSemiGroupElement<E>>
    extends Magma<E> {

    NavigableSet<Operator> OPERATORS = navigableSet(Magma.OPERATORS, MULTIPLICATION);

    NavigableSet<UnaryOperator> UNARY_OPERATORS = navigableSet(Magma.UNARY_OPERATORS, SQR);


    @Override
    default NavigableSet<Operator> getSupportedOperators() {
        return OPERATORS;
    }

    @Override
    default NavigableSet<UnaryOperator> getSupportedUnaryOperators() {
        return UNARY_OPERATORS;
    }

    default boolean multiplicationIsCommutative() {
        return false;
    }

}
