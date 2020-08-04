package org.meeuw.math.abstractalgebra;

/**
 * Elements of a {@link MultiplicativeSemiGroup} can be multiplied by each other (via {@link #times(MultiplicativeSemiGroupElement)}.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MultiplicativeSemiGroupElement<E extends MultiplicativeSemiGroupElement<E>> extends AlgebraicElement<E> {

    MultiplicativeSemiGroup<E> structure();

    E times(E summand);
}
