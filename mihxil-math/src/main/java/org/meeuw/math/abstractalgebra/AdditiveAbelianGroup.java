package org.meeuw.math.abstractalgebra;

import java.util.NavigableSet;

import static org.meeuw.math.Utils.navigableSet;
import static org.meeuw.math.abstractalgebra.Operator.SUBTRACTION;
import static org.meeuw.math.abstractalgebra.UnaryOperator.NEGATION;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AdditiveAbelianGroup<E extends AdditiveGroupElement<E>>
    extends AdditiveGroup<E>, AdditiveAbelianSemiGroup<E> {

    NavigableSet<Operator> OPERATORS = navigableSet(AdditiveMonoid.OPERATORS, SUBTRACTION);

    NavigableSet<UnaryOperator> UNARY_OPERATORS = navigableSet(AdditiveMonoid.UNARY_OPERATORS, NEGATION);


    @Override
    default boolean additionIsCommutative() {
        return true;
    }
}
