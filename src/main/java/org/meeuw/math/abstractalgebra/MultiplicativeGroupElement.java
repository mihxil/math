package org.meeuw.math.abstractalgebra;

/**
 * An element for the algebraic 'group' (where the operation is multiplication)
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MultiplicativeGroupElement<F extends MultiplicativeGroupElement<F, A>, A extends AlgebraicStructure<F, A>> extends AlgebraicElement<F, A> {

    /**
     * Multiplies
     */

    F times(F multiplier);

    F pow(int exponent);

    /**
     * The multiplicative inverse
     */
    default F reciprocal() {
        return pow(-1);
    }
    default F dividedBy(F divisor) {
        return times(divisor.reciprocal());
    }

}
