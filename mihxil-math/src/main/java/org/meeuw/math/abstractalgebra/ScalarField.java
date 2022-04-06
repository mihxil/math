package org.meeuw.math.abstractalgebra;

import java.util.NavigableSet;

import org.meeuw.math.numbers.Scalar;
import org.meeuw.math.operators.*;

import static org.meeuw.math.CollectionUtils.navigableSet;

/**
 * A field with {@link ScalarFieldElement}s.
 *
 * Elements of such a fields can be converted to {@link Number}. Two basic operators are in place.
 *
 * <ul>
 * <li>{@link Scalar#bigDecimalValue()}: for which the symbol x₌ ('equal value',  {@link BasicFunction#DECIMAL}) is used, and
 * <li>{@link Scalar#bigIntegerValue()}: for which we use the symbol ⌊x⌉ ('rounding', {@link BasicFunction#INTEGER}
 * </ul>
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
