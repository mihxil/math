package org.meeuw.math.abstractalgebra;

/**
 * A commutative ring
 *
 * @author Michiel Meeuwissen
 * @since 0.7
 */
public interface AbelianRing<E extends AbelianRingElement<E>> extends Ring<E> {

    @Override
    E one();

    @Override
    default boolean multiplicationIsCommutative() {
        return true;
    }

}
