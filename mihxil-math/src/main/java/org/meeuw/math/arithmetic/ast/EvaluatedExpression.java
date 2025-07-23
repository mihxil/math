package org.meeuw.math.arithmetic.ast;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.abstractalgebra.FieldElement;

/**
 * A container for an AST expression and its result
 * @since 0.19
 */
public record EvaluatedExpression<E extends FieldElement<E>>(

    Expression<E> expression,
    E result) {
    @Override
    @NonNull
    public String toString() {
        return AST.toInfix(expression) + " = " + result;
    }
}
