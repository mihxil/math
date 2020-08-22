package org.meeuw.math.abstractalgebra;

/**
 * Elements of a {@link MultiplicativeSemiGroup} can be multiplied by each other (via {@link #times(AdditiveSemiGroupElement)}.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AdditiveSemiGroupElement<E extends AdditiveSemiGroupElement<E>> extends AlgebraicElement<E> {

    AdditiveSemiGroup<E> getStructure();

    E plus(E summand);

    /**
     * less verbose version of {@link #plus(AdditiveSemiGroupElement)}
     */
    default E p(E multiplier) {
        return plus(multiplier);
    }

}
