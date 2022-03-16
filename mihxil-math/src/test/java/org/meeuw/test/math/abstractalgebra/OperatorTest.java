package org.meeuw.test.math.abstractalgebra;

import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.operators.BasicAlgebraicBinaryOperator;
import org.meeuw.math.exceptions.NoSuchOperatorException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class OperatorTest {

    public static class  A implements AdditiveSemiGroupElement<A> {

        @Override
        public AdditiveSemiGroup<A> getStructure() {
            return null;
        }

        @Override
        public A plus(A summand) {
            return new A();
        }
        @Override
        public String toString() {
            return "<a>";
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
            throw new IllegalArgumentException("foo bar");
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
        }).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void illegalArgument() {
        assertThatThrownBy(() -> {
            BasicAlgebraicBinaryOperator.ADDITION.apply(new AIllegalArgument(), new A());
        }).isInstanceOf(IllegalArgumentException.class).hasMessage("foo bar");
    }

    @Test
    public void wrongArgument() {
        assertThatThrownBy(() -> {
            BasicAlgebraicBinaryOperator.MULTIPLICATION.apply(new A(), new A());
        }).isInstanceOf(NoSuchOperatorException.class).hasMessage("A <a> has no operator 'times'");
    }

}
