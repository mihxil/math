package org.meeuw.math.arithmetic.ast;

import lombok.EqualsAndHashCode;

import org.meeuw.math.abstractalgebra.*;

import static org.meeuw.math.operators.BasicAlgebraicBinaryOperator.*;

@EqualsAndHashCode
public abstract class AbstractExpression<E extends AlgebraicElement<E>> implements Expression<E> {



    public BinaryOperation<E> times(Expression<E> multiplier) {
        return new BinaryOperation<>(MULTIPLICATION, this, multiplier);
    }

    public BinaryOperation<E> dividedBy(Expression<E> divisor) {
        return new BinaryOperation<>(DIVISION, this, divisor);
    }

    public BinaryOperation<E> plus(Expression<E> summand) {
        return new BinaryOperation<>(ADDITION, this, summand);
    }


    public BinaryOperation<E> minus(Expression<E> summand) {
        return new BinaryOperation<>(SUBTRACTION, this, summand);
    }
}
