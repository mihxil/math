package org.meeuw.math.abstractalgebra;

import java.util.Objects;
import java.util.function.Function;

/**
 * Like a {@link java.util.function.UnaryOperator} but not generic itself.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@FunctionalInterface
public interface AlgebraicUnaryOperator {

    <E extends AlgebraicElement<E>> E apply(E e);

      /**
     * Returns a composed function that first applies the {@code before}
     * function to its input, and then applies this function to the result.
     * If evaluation of either function throws an exception, it is relayed to
     * the caller of the composed function.
     *
     * @param before the function to apply before this function is applied
     * @return a composed function that first applies the {@code before}
     * function and then applies this function
     * @throws NullPointerException if before is null
     *
     * @see Function#andThen(Function)
     */
    default AlgebraicUnaryOperator compose(AlgebraicUnaryOperator before) {
        Objects.requireNonNull(before);
        return new AlgebraicUnaryOperator() {
            @Override
            public <E extends AlgebraicElement<E>> E apply(E e) {
                return AlgebraicUnaryOperator.this.apply(before.apply(e));
            }
        };
    }

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
     *
     * @see Function#compose(Function)
     */
    default AlgebraicUnaryOperator andThen(final AlgebraicUnaryOperator after) {
        Objects.requireNonNull(after);
        return new AlgebraicUnaryOperator() {
            @Override
            public <E extends AlgebraicElement<E>> E apply(E e) {
                return after.apply(AlgebraicUnaryOperator.this.apply(e));
            }
        };
    }

    /**
     * Returns a function that always returns its input argument.
     *
     * @return a function that always returns its input argument
     */
    static AlgebraicUnaryOperator identity() {
        return new AlgebraicUnaryOperator() {
            @Override
            public <E extends AlgebraicElement<E>> E apply(E e) {
                return e;
            }
        };
    }
}
