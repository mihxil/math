package org.meeuw.math.abstractalgebra;

import javax.validation.constraints.Min;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MultiplicativeMonoidElement<E extends MultiplicativeMonoidElement<E>> extends MultiplicativeSemiGroupElement<E> {

    MultiplicativeMonoid<E> getStructure();

    /**
     * if multiplication is defined, then so is exponentation, as long as the exponent is non negative
     */
    default E pow(@Min(0) int exponent) {
        E result = getStructure().one();
        while (exponent > 0) {
            result = result.times(self());
            exponent--;
        }
        if(exponent < 0) {
            throw new IllegalArgumentException();
        }
        return result;
    }
}
