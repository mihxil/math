package org.meeuw.math.abstractalgebra;

import javax.validation.constraints.Min;

/**
 * Elements of a {@link MultiplicativeSemiGroup} can be multiplied by each other (via {@link #times(MultiplicativeSemiGroupElement)}.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MultiplicativeSemiGroupElement<E extends MultiplicativeSemiGroupElement<E>> extends AlgebraicElement<E> {

    MultiplicativeSemiGroup<E> structure();

    E times(E multiplier);

    /**
     * if multiplication is defined, then so is exponentation, as long as the exponent is positive
     */
    default E pow(@Min(0) int exponent) {
        E result = self();
        while (exponent > 1) {
            result = result.times(self());
            exponent--;
        }
        if(exponent < 1) {
            throw new IllegalArgumentException();
        }
        return result;
    }
}
