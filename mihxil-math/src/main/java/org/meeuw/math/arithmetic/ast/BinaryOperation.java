package org.meeuw.math.arithmetic.ast;


import lombok.EqualsAndHashCode;
import lombok.Getter;

import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.operators.AlgebraicBinaryOperator;

@Getter
@EqualsAndHashCode
public class BinaryOperation<E extends AlgebraicElement<E>> implements Expression<E> {
    private final AlgebraicBinaryOperator operator;
    private final Expression<E> left;
    private final Expression<E> right;

    public BinaryOperation(AlgebraicBinaryOperator operator, Expression<E> left, Expression<E> right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    @Override
    public E eval() {
        return operator.apply(left.eval(), right.eval());
    }

    @Override
    public String toString() {
        return operator.stringify(left.toString(), right.toString());
    }

}
