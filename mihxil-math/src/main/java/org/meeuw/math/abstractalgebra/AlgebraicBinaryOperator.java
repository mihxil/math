package org.meeuw.math.abstractalgebra;

import java.util.Objects;

/**
 * Like {@link java.util.function.BinaryOperator}, but the difference is that this is not itself generic, but only its {@link #apply(AlgebraicElement, AlgebraicElement)} method.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@FunctionalInterface
public interface AlgebraicBinaryOperator {

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
    default AlgebraicBinaryOperator andThen(AlgebraicUnaryOperator after) {
        Objects.requireNonNull(after);
        return new AlgebraicBinaryOperator() {
            @Override
            public <E extends AlgebraicElement<E>> E apply(E arg1, E arg2) {
                return after.apply(AlgebraicBinaryOperator.this.apply(arg1, arg2));
            }
        };
    }
}
