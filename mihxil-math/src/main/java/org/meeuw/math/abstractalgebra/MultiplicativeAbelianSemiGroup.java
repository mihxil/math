package org.meeuw.math.abstractalgebra;

/**
 * A <a href="https://en.wikipedia.org/wiki/Group_(mathematics)">Group</a> with the binary operation 'multiplication'.
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MultiplicativeAbelianSemiGroup<E extends MultiplicativeSemiGroupElement<E>>
    extends MultiplicativeSemiGroup<E> {


    @Override
    default boolean multiplicationIsCommutative() {
        return true;
    }
}
