package org.meeuw.math.arithmetic.ast;


import lombok.Getter;

import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.operators.AlgebraicUnaryOperator;

@Getter
public class UnaryOperator<E extends AlgebraicElement<E>> extends AbstractExpression<E> {
    private final AlgebraicUnaryOperator operator;
    private final Expression<E> operand;

    public UnaryOperator(AlgebraicUnaryOperator operator, Expression<E> operand) {
        this.operator = operator;
        this.operand = operand;
    }

    @Override
    public E eval() {
        return operator.apply(operand.eval());
    }

    @Override
    public int compareTo(Expression<E> o) {
        return 0;
    }
}
