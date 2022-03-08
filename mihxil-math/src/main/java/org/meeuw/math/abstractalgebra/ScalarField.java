package org.meeuw.math.abstractalgebra;

import java.util.NavigableSet;

import static org.meeuw.math.Utils.navigableSet;

/**
 * A field with {@link ScalarFieldElement}s
 * @author Michiel Meeuwissen
 * @since 0.4
 * @param <E> The type of the elements of this field
 */
public interface ScalarField<E extends ScalarFieldElement<E>> extends Field<E> {


    NavigableSet<UnaryOperator> UNARY_OPERATORS = navigableSet(Field.UNARY_OPERATORS, UnaryOperator.ABS);


    @Override
    default boolean multiplicationIsCommutative() {
        return true;
    }

}
