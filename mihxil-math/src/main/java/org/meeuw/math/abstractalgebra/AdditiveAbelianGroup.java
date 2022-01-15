package org.meeuw.math.abstractalgebra;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AdditiveAbelianGroup<E extends AdditiveGroupElement<E>>
    extends AdditiveGroup<E>,
    AdditiveAbelianSemiGroup<E> {

    @Override
    default boolean additionIsCommutative() {
        return true;
    }
}
