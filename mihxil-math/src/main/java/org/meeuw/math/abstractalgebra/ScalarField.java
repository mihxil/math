package org.meeuw.math.abstractalgebra;

import java.util.NavigableSet;

import org.meeuw.math.operators.*;

import static org.meeuw.math.Utils.navigableSet;

/**
 * A field with {@link ScalarFieldElement}s
 * @author Michiel Meeuwissen
 * @since 0.4
 * @param <E> The type of the elements of this field
 */
public interface ScalarField<E extends ScalarFieldElement<E>> extends Field<E> {

    NavigableSet<GenericFunction> FUNCTIONS = navigableSet(Field.FUNCTIONS, BasicFunction.ABS, BasicFunction.DECIMAL, BasicFunction.INTEGER);

    @Override
    default NavigableSet<GenericFunction> getSupportedFunctions() {
        return FUNCTIONS;
    }

    @Override
    default boolean multiplicationIsCommutative() {
        return true;
    }

}
