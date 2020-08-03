package org.meeuw.math.abstractalgebra;

/**
 * An element for the algebraic 'group' (where the operation is multiplication)
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MultiplicativeGroupElement<F extends MultiplicativeGroupElement<F>> extends MultiplicativeMonoidElement<F> {

    @Override
    MultiplicativeGroup<F> structure();

    /**
     * The multiplicative inverse
     */
    default F reciprocal() {
        return pow(-1);
    }

    /**
     * if multiplication and division is definied, then so is exponentation
     */
    default F pow(int exponent) {
        if (exponent < 0) {
            throw new UnsupportedOperationException();
        }
        F result = structure().one();
        while (exponent > 0) {
            result = result.times(self());
            exponent--;
        }
        return result;
    }


    default F dividedBy(F divisor) {
        return times(divisor.reciprocal());
    }

    /**
     * Returns this elemented multiplied by itself.
     */
    default F sqr() {
        return times(self());
    }


}
