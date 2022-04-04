package org.meeuw.test.math.abstractalgebra;

import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.InvalidAlgebraicResult;
import org.meeuw.math.operators.BasicAlgebraicBinaryOperator;
import org.meeuw.math.exceptions.NoSuchOperatorException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class OperatorTest {
    static {
        Assertions.setMaxStackTraceElementsDisplayed(10);
    }
    public static class AStruct implements AdditiveSemiGroup<A> {

        @Override
        public Cardinality getCardinality() {
            return new Cardinality(2);
        }

        @Override
        public Class<A> getElementClass() {
            return A.class;
        }
    }

    public static class A implements AdditiveSemiGroupElement<A> {

        @Override
        public AdditiveSemiGroup<A> getStructure() {
            return new AStruct();
        }

        @Override
        public A plus(A summand) {
            return new A();
        }
        @Override
        public String toString() {
            return "<a>";
        }

        public A dividedBy(A divisor) {
            return new A();
        }
    }

    public static class ANull extends A {
        @Override
        public A plus(A summand) {
            return null;
        }
    }

    public static class AIllegalArgument extends A {
        @Override
        public A plus(A summand) {
            throw new MyException("foo bar");
        }
    }

    public static class MyException extends RuntimeException {

        public MyException(String message) {
            super(message);
        }
    }

    @Test
    public void add() {
        Assertions.assertThat(BasicAlgebraicBinaryOperator.ADDITION.apply(new A(), new A())).isInstanceOf(A.class);
    }

    @Test
    public void stringify() {
        assertThat(BasicAlgebraicBinaryOperator.ADDITION.stringify(new A(), new A())).isEqualTo("<a> + <a>");
        assertThat(BasicAlgebraicBinaryOperator.ADDITION.getStringify().apply("x", "y")).isEqualTo("x + y");
    }

    @Test
    public void illegalState() {
        assertThatThrownBy(() -> {
            BasicAlgebraicBinaryOperator.ADDITION.apply(new ANull(), new A());
        }).isInstanceOf(InvalidAlgebraicResult.class);
    }

    @Test
    public void myException() {
        assertThatThrownBy(() -> {
            BasicAlgebraicBinaryOperator.ADDITION.apply(new AIllegalArgument(), new A());
        }).isInstanceOf(MyException.class).hasMessage("foo bar");
    }

    @Test
    public void wrongArgument() {
        assertThatThrownBy(() -> {
            BasicAlgebraicBinaryOperator.MULTIPLICATION.apply(new A(), new A());
        }).isInstanceOf(NoSuchOperatorException.class).hasMessage("A <a> has no operator 'times'");
    }

    @Test
    public void illegalArgument() {
        assertThatThrownBy(() -> {
            BasicAlgebraicBinaryOperator.DIVISION.apply(new A(), new A());
        }).isInstanceOf(NoSuchOperatorException.class).hasMessage("A <a> has no operator 'dividedBy'");
    }

    @Test
    public void inverse() {
        assertThatThrownBy(() -> {
            BasicAlgebraicBinaryOperator.ADDITION.inverse(new A());
        }).isInstanceOf(NoSuchOperatorException.class);
    }

    @Test
    public void unity() {
        assertThatThrownBy(() -> {
            BasicAlgebraicBinaryOperator.ADDITION.unity(new AStruct());
        }).isInstanceOf(NoSuchOperatorException.class);
    }

}
