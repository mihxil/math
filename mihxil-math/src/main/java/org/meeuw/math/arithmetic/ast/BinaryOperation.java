package org.meeuw.math.arithmetic.ast;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.operators.AlgebraicBinaryOperator;

@Getter
@EqualsAndHashCode(callSuper = true)
public class BinaryOperation<E extends FieldElement<E>> extends AbstractExpression<E>  {
    private final AlgebraicBinaryOperator operator;
    private final Expression<E> left;
    private final Expression<E> right;

    public BinaryOperation(
        AlgebraicBinaryOperator operator,
        Expression<E> left,
        Expression<E> right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    public BinaryOperation<E> reverse() {
        return new BinaryOperation<>(operator, right, left);
    }

    @Override
    public BinaryOperation<E> canonize(AlgebraicStructure<E> structure) {
        Expression<E> cleft = left.canonize(structure);
        Expression<E> cright = right.canonize(structure);
        if (structure.isCommutative(operator) && cleft.compareTo(  cright) > 0)  {
            return new BinaryOperation<>(operator, cright, cleft);
        } else {
            return new BinaryOperation<>(operator, cleft, cright);
        }
    }

    @Override
    public E eval() {
        return operator.apply(left.eval(), right.eval());
    }

    @Override
    public String toString() {
        return operator.stringify(
            left.toString(),
            right.toString()
        );
    }


    @Override
    public int compareTo(Expression<E> o) {
        return toString().compareTo(o.toString());
    }
}
