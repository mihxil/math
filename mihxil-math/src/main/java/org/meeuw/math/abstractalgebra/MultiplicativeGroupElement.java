package org.meeuw.math.abstractalgebra;

/**
 * An element for the algebraic 'group' (where the operation is multiplication)
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MultiplicativeGroupElement<F extends MultiplicativeGroupElement<F>> extends AlgebraicElement<F>, MultiplicableElement<F> {

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
    F pow(int exponent);


    default F dividedBy(F divisor) {
        return times(divisor.reciprocal());
    }

}
