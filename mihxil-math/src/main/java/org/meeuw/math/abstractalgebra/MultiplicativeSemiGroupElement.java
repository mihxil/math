package org.meeuw.math.abstractalgebra;

/**
 * Elements of a {@link MultiplicativeSemiGroup} can be multiplied by each other (via {@link #times(MultiplicativeSemiGroupElement)}.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MultiplicativeSemiGroupElement<F extends MultiplicativeSemiGroupElement<F>> extends AlgebraicElement<F> {

    MultiplicativeSemiGroup<F> structure();

    F times(F summand);
}
