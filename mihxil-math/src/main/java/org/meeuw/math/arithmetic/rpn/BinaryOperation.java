package org.meeuw.math.arithmetic.rpn;


import lombok.Getter;

import org.meeuw.math.operators.AlgebraicBinaryOperator;
import org.meeuw.math.operators.BasicAlgebraicBinaryOperator;

@Getter
public class BinaryOperation implements StackElement {
    private final AlgebraicBinaryOperator operator;

    public BinaryOperation(BasicAlgebraicBinaryOperator operator) {
        this.operator = operator;
    }

}
