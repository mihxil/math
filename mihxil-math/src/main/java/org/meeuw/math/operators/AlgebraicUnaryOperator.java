package org.meeuw.math.operators;

import java.util.Objects;
import java.util.function.Function;

import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.text.TextUtils;

/**
 * Like a {@link java.util.function.UnaryOperator} but not generic itself.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AlgebraicUnaryOperator extends OperatorInterface {

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

            @Override
            public String stringify(String element) {
                return AlgebraicUnaryOperator.this.stringify(before.stringify(element));
            }

            @Override
            public String name() {
                return before.name() + " and then " + AlgebraicUnaryOperator.this.name();
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

            @Override
            public String stringify(String element) {
                return after.stringify(AlgebraicUnaryOperator.this.stringify(element));
            }

            @Override
            public String name() {
                return AlgebraicUnaryOperator.this.name() + " and then " + after.name();
            }
        };
    }

    String stringify(String element);

    default <E extends AlgebraicElement<E>> String stringify(E element) {
        return stringify(element.toString());
    }

    default String getSymbol() {
        return stringify(TextUtils.PLACEHOLDER);
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

            @Override
            public String stringify(String element) {
                return "self(" + element + ")";
            }

            @Override
            public String name() {
                return "identity";
            }
        };
    }
}
