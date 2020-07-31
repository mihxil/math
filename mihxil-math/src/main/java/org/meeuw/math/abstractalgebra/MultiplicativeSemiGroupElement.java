package org.meeuw.math.abstractalgebra;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MultiplicativeSemiGroupElement<F extends MultiplicativeSemiGroupElement<F>> extends AlgebraicElement<F> {

    MultiplicativeSemiGroup<F> structure();

    F times(F summand);
}
