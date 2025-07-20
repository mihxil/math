package org.meeuw.math.arithmetic.ast;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.abstractalgebra.AlgebraicElement;

/**
 * A container for an AST expression and its result
 * @since 0.19
 */
public record EvaluatedExpression<E extends AlgebraicElement<E>>(
    Expression<E> expression,
    E result) {
    @Override
    @NonNull
    public String toString() {
        return AST.toInfix(expression) + " = " + result;
    }
}
