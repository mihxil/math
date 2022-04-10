package org.meeuw.math.abstractalgebra;

import org.meeuw.math.NonAlgebraic;

/**
 * An element of an algebraic Field. Next to multiplication, also addition is defined.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface DivisionRingElement<E extends DivisionRingElement<E>> extends
    MultiplicativeGroupElement<E>,
    RingElement<E> {

    @Override
    DivisionRing<E> getStructure();

    @Override
    default E operate(E operand) {
        return getStructure().groupOperator().apply(self(), operand);
    }
    @Override
    @NonAlgebraic
    default E inverse() {
        return getStructure().groupOperator().inverse(self());
    }

    @Override
    @NonAlgebraic
    default E dividedBy(E divisor) {
        return times(divisor.reciprocal());
    }


}
