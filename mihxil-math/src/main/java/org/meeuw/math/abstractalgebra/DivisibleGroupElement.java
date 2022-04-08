package org.meeuw.math.abstractalgebra;

import jakarta.validation.constraints.Positive;

/**
 *
 * @see DivisibleGroup
 * @author Michiel Meeuwissen
 * @since 0.8
 */
public interface DivisibleGroupElement<E extends DivisibleGroupElement<E>>
    extends MultiplicativeGroupElement<E> {

    @Override
    DivisibleGroup<E> getStructure();

    /**
     * Returns the
     */
    E dividedBy(@Positive long divisor);

    E times(@Positive long multiplier);


}
