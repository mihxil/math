package org.meeuw.math.abstractalgebra;

import org.meeuw.math.NonAlgebraic;
import org.meeuw.math.exceptions.DivisionByZeroException;
import org.meeuw.math.exceptions.ReciprocalException;

/**
 * An element of an algebraic Field. Next to multiplication, also addition is defined.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface FieldElement<E extends FieldElement<E>> extends
    DivisionRingElement<E>,
    AbelianRingElement<E>,
    DivisibleGroupElement<E>,
    GroupElement<E> {

    @Override
    Field<E> getStructure();

    @Override
    @NonAlgebraic("Cannot divide be zero")
    default E dividedBy(E divisor) throws DivisionByZeroException {
        return DivisionRingElement.super.dividedBy(divisor);
    }

    @Override
    @NonAlgebraic("Zero has no reciprocal")
    E reciprocal() throws ReciprocalException;



}

