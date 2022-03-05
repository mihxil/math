package org.meeuw.math.abstractalgebra;

import java.util.NavigableSet;

import static org.meeuw.math.Utils.navigableSet;
import static org.meeuw.math.abstractalgebra.Operator.ADDITION;
import static org.meeuw.math.abstractalgebra.Operator.OPERATION;

/**
 * The algebraic structure that only defines addition. There might be no additive identity {@link AdditiveMonoid#zero()}
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AdditiveSemiGroup<E extends AdditiveSemiGroupElement<E>> extends Magma<E> {

    NavigableSet<Operator> OPERATORS = navigableSet(OPERATION, ADDITION);

    @Override
    default NavigableSet<Operator> getSupportedOperators() {
        return OPERATORS;
    }

    default boolean additionIsCommutative() {
        return false;
    }

}
