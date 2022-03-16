package org.meeuw.math.operators;

import java.util.Objects;

import org.meeuw.math.abstractalgebra.AlgebraicElement;

/**
 * Like {@link java.util.function.BinaryOperator}, but the difference is that this is not itself generic, but only its {@link #apply(AlgebraicElement, AlgebraicElement)} method.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AlgebraicBinaryOperator  extends OperatorInterface {

    <E extends AlgebraicElement<E>> E apply(E arg1, E arg2);

      /**
     * Returns a composed function that first applies this function to
     * its input, and then applies the {@code after} function to the result.
     * If evaluation of either function throws an exception, it is relayed to
     * the caller of the composed function.
     *
     * @param after the function to apply after this function is applied
     * @return a composed function that first applies this function and then
     * applies the {@code after} function
     * @throws NullPointerException if after is null
     */
    default AlgebraicBinaryOperator andThen(final AlgebraicUnaryOperator after) {
        Objects.requireNonNull(after);
        return new AlgebraicBinaryOperator() {
            @Override
            public <E extends AlgebraicElement<E>> E apply(E arg1, E arg2) {
                return after.apply(AlgebraicBinaryOperator.this.apply(arg1, arg2));
            }

            @Override
            public String stringify(Object element1, Object element2) {
                return after.stringify(AlgebraicBinaryOperator.this.stringify(element1, element2));
            }

            @Override
            public String name() {
                return AlgebraicBinaryOperator.this.name() + " and then " + after.name();
            }

        };
    }

    String stringify(Object element1, Object element2);

    default String getSymbol() {
        return stringify("·", "·");
    }

}
