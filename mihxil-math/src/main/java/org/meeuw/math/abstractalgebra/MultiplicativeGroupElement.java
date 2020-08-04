package org.meeuw.math.abstractalgebra;

/**
 * An element for the algebraic 'group' (where the operation is multiplication)
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MultiplicativeGroupElement<E extends MultiplicativeGroupElement<E>> extends MultiplicativeMonoidElement<E> {

    @Override
    MultiplicativeGroup<E> structure();

    /**
     * The multiplicative inverse
     */
    default E reciprocal() {
        return pow(-1);
    }

    /**
     * if multiplication and division is definied, then so is exponentation
     */
    default E pow(int exponent) {
        if (exponent < 0) {
            throw new UnsupportedOperationException();
        }
        E result = structure().one();
        while (exponent > 0) {
            result = result.times(self());
            exponent--;
        }
        return result;
    }


    default E dividedBy(E divisor) {
        return times(divisor.reciprocal());
    }

    /**
     * Returns this elemented multiplied by itself.
     */
    default E sqr() {
        return times(self());
    }


}
