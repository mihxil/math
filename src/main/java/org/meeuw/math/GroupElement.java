package org.meeuw.math;

/**
 * An element for the algebraic 'group' (where the operation is multiplication)
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface GroupElement<F extends GroupElement<F>>  {
    /**
     * Multiplies
     */

    F times(F multiplicand);

    F pow(int exponent);

    default F inverse() {
        return pow(-1);
    }
    default F dividedBy(F divisor) {
        return times(divisor.inverse());
    }

}
