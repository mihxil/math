package org.meeuw.math.arithmetic.ast;


import lombok.Getter;

import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.operators.*;

@Getter
public class UnaryOperator<E extends AlgebraicElement<E>> implements Expression<E> {
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
}
