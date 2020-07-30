package org.meeuw.math.abstractalgebra;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MultiplicableElement<F extends MultiplicableElement<F>> {

    /**
     * Multiplies
     */

    F times(F multiplier);
}
