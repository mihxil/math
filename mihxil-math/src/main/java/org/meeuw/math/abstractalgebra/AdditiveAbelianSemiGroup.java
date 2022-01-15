package org.meeuw.math.abstractalgebra;

/**
 * A <a href="https://en.wikipedia.org/wiki/Group_(mathematics)">Group</a> with the binary operation 'addition'.
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AdditiveAbelianSemiGroup<E extends AdditiveSemiGroupElement<E>>
    extends AdditiveSemiGroup<E> {


    @Override
    default boolean additionIsCommutative() {
        return true;
    }
}
