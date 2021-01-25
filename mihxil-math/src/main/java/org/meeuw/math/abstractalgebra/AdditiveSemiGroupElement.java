package org.meeuw.math.abstractalgebra;

/**
 * Elements of a {@link AdditiveSemiGroup} can be added to each other (via {@link #plus(AdditiveSemiGroupElement)}.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AdditiveSemiGroupElement<E extends AdditiveSemiGroupElement<E>>
    extends AlgebraicElement<E> {

    @Override
    AdditiveSemiGroup<E> getStructure();

    /**
     * @param summand the element to add to this one
     * @return this + summand
     */
    E plus(E summand);

    /**
     * less verbose version of {@link #plus(AdditiveSemiGroupElement)}
     * @param summand the element to add to this one
     */
    default E p(E summand) {
        return plus(summand);
    }

}
