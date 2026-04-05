package org.meeuw.test.math.arithmetic.ast;

import lombok.extern.java.Log;

import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.math.arithmetic.ast.*;
import org.meeuw.math.operators.BasicAlgebraicBinaryOperator;
import org.meeuw.math.operators.BasicAlgebraicUnaryOperator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.abstractalgebra.reals.DoubleElement.exactly;
import static org.meeuw.math.abstractalgebra.reals.RealField.INSTANCE;

@Log
class ExpressionComponentsTest {

    // --- Value tests ---

    @Test
    void valueEval() {
        Value<RealNumber> v = new Value<>(exactly(3.0));
        assertThat(v.eval().getValue()).isEqualTo(3.0);
    }

    @Test
    void valueToString() {
        Value<RealNumber> v = new Value<>(exactly(5.0));
        String s = v.toString();
        assertThat(s).contains("value:");
        assertThat(s).contains("5");
    }

    @Test
    void valueCompareToSameValue() {
        Value<RealNumber> a = new Value<>(exactly(2.0));
        Value<RealNumber> b = new Value<>(exactly(2.0));
        assertThat(a.compareTo(b)).isEqualTo(0);
    }

    @Test
    void valueCompareToOtherValue() {
        Value<RealNumber> a = new Value<>(exactly(1.0));
        Value<RealNumber> b = new Value<>(exactly(9.0));
        assertThat(a.compareTo(b)).isLessThan(0);
    }

    @Test
    void valueCompareToNonValue() {
        Value<RealNumber> v = new Value<>(exactly(1.0));
        org.meeuw.math.arithmetic.ast.BinaryOperation<RealNumber> op =
            new org.meeuw.math.arithmetic.ast.BinaryOperation<>(
                BasicAlgebraicBinaryOperator.ADDITION,
                new Value<>(exactly(1.0)),
                new Value<>(exactly(2.0))
            );
        // compareTo with a non-Value falls back to toString comparison
        int cmp = v.compareTo(op);
        assertThat(v.toString().compareTo(op.toString())).isEqualTo(cmp);
    }

    // --- Variable tests ---

    @Test
    void variableEval() {
        Variable<RealNumber> x = new Variable<>("x", () -> exactly(7.0));
        assertThat(x.eval().getValue()).isEqualTo(7.0);
    }

    @Test
    void variableToString() {
        Variable<RealNumber> x = new Variable<>("myVar", () -> exactly(1.0));
        assertThat(x.toString()).isEqualTo("myVar");
    }

    @Test
    void variableCompareTo() {
        Variable<RealNumber> a = new Variable<>("a", () -> exactly(1.0));
        Variable<RealNumber> b = new Variable<>("b", () -> exactly(1.0));
        assertThat(a.compareTo(b)).isLessThan(0);
        assertThat(b.compareTo(a)).isGreaterThan(0);
        assertThat(a.compareTo(a)).isEqualTo(0);
    }

    @Test
    void variableDynamicValue() {
        int[] counter = {0};
        Variable<RealNumber> x = new Variable<>("x", () -> exactly(++counter[0]));
        assertThat(x.eval().getValue()).isEqualTo(1.0);
        assertThat(x.eval().getValue()).isEqualTo(2.0);
    }

    // --- UnaryOperation tests ---

    @Test
    void unaryOperationEval() {
        Value<RealNumber> two = new Value<>(exactly(2.0));
        UnaryOperation<RealNumber> sqr = new UnaryOperation<>(BasicAlgebraicUnaryOperator.SQR, two);
        assertThat(sqr.eval().getValue()).isEqualTo(4.0);
    }

    @Test
    void unaryOperationToString() {
        Value<RealNumber> v = new Value<>(exactly(3.0));
        UnaryOperation<RealNumber> op = new UnaryOperation<>(BasicAlgebraicUnaryOperator.NEGATION, v);
        String s = op.toString();
        log.info("UnaryOperation toString: " + s);
        assertThat(s).contains("unary:");
    }

    @Test
    void unaryOperationCompareTo() {
        Value<RealNumber> v = new Value<>(exactly(1.0));
        UnaryOperation<RealNumber> op1 = new UnaryOperation<>(BasicAlgebraicUnaryOperator.SQR, v);
        UnaryOperation<RealNumber> op2 = new UnaryOperation<>(BasicAlgebraicUnaryOperator.NEGATION, v);
        assertThat(op1.compareTo(op2)).isEqualTo(0);
    }

    // --- BinaryOperation tests ---

    @Test
    void binaryOperationReverse() {
        Value<RealNumber> two = new Value<>(exactly(2.0));
        Value<RealNumber> three = new Value<>(exactly(3.0));
        org.meeuw.math.arithmetic.ast.BinaryOperation<RealNumber> op =
            new org.meeuw.math.arithmetic.ast.BinaryOperation<>(
                BasicAlgebraicBinaryOperator.SUBTRACTION, two, three);
        org.meeuw.math.arithmetic.ast.BinaryOperation<RealNumber> rev = op.reverse();
        assertThat(rev.getLeft()).isSameAs(three);
        assertThat(rev.getRight()).isSameAs(two);
        // 2 - 3 = -1, reversed 3 - 2 = 1
        assertThat(op.eval().getValue()).isEqualTo(-1.0);
        assertThat(rev.eval().getValue()).isEqualTo(1.0);
    }

    // --- EvaluatedExpression tests ---

    @Test
    void evaluatedExpressionToString() {
        Value<RealNumber> v = new Value<>(exactly(5.0));
        EvaluatedExpression<RealNumber> ee = new EvaluatedExpression<>(v, v.eval());
        String s = ee.toString();
        log.info("EvaluatedExpression: " + s);
        assertThat(s).contains("=");
        assertThat(s).contains("5");
    }

    @Test
    void evaluatedExpressionAccessors() {
        Value<RealNumber> v = new Value<>(exactly(42.0));
        RealNumber result = v.eval();
        EvaluatedExpression<RealNumber> ee = new EvaluatedExpression<>(v, result);
        assertThat(ee.expression()).isSameAs(v);
        assertThat(ee.result()).isSameAs(result);
    }

    // --- RPN tests ---

    @Test
    void rpnValueGetValue() {
        RealNumber val = exactly(3.0);
        org.meeuw.math.arithmetic.rpn.Value<RealNumber> rpnVal =
            new org.meeuw.math.arithmetic.rpn.Value<>(val);
        assertThat(rpnVal.getValue()).isSameAs(val);
    }

    @Test
    void rpnBinaryOperationGetOperator() {
        org.meeuw.math.arithmetic.rpn.BinaryOperation rpnOp =
            new org.meeuw.math.arithmetic.rpn.BinaryOperation(BasicAlgebraicBinaryOperator.ADDITION);
        assertThat(rpnOp.getOperator()).isEqualTo(BasicAlgebraicBinaryOperator.ADDITION);
    }

    // --- AbstractExpression builder methods ---

    @Test
    void abstractExpressionBuilderMethods() {
        Value<RealNumber> two = new Value<>(exactly(2.0));
        Value<RealNumber> three = new Value<>(exactly(3.0));

        assertThat(two.plus(three).eval().getValue()).isEqualTo(5.0);
        assertThat(two.minus(three).eval().getValue()).isEqualTo(-1.0);
        assertThat(two.times(three).eval().getValue()).isEqualTo(6.0);
        assertThat(three.dividedBy(two).eval().getValue()).isEqualTo(1.5);
    }

    // --- AST.toInfix with Variable ---

    @Test
    void toInfixWithVariable() {
        Variable<RealNumber> x = new Variable<>("x", () -> exactly(10.0));
        String infix = AST.toInfix(x);
        log.info("infix of variable: " + infix);
        assertThat(infix).isNotEmpty();
    }
}
