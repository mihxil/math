package org.meeuw.math.abstractalgebra;

/**
 * <a href="https://en.wikipedia.org/wiki/Field_(mathematics)">Field</a>
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface Field<E extends FieldElement<E>> extends
    DivisionRing<E>,
    AbelianRing<E>,
    DivisibleGroup<E> {

    @Override
    default boolean multiplicationIsCommutative() {
        return true;
    }

    @Override
    E one();


}
