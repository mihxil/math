package org.meeuw.math.abstractalgebra;

import java.util.NavigableSet;

import org.meeuw.math.Utils;

import static org.meeuw.math.abstractalgebra.Operator.MULTIPLICATION;

/**
 * The algebraic structure that only defines multiplication. There might be no multiplicatie identity {@link MultiplicativeMonoid#one()}
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MultiplicativeSemiGroup<E extends MultiplicativeSemiGroupElement<E>>
    extends Magma<E> {

    NavigableSet<Operator> OPERATORS = Utils.navigableSet(MULTIPLICATION);

    @Override
    default NavigableSet<Operator> getSupportedOperators() {
        return OPERATORS;
    }

    default boolean multiplicationIsCommutative() {
        return false;
    }

}
