package org.meeuw.math.abstractalgebra;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
public interface MultiplicableElement<F extends MultiplicableElement<F>> {

    /**
     * Multiplies
     */

    F times(F multiplier);
}
