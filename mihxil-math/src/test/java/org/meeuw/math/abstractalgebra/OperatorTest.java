package org.meeuw.math.abstractalgebra;

import org.junit.jupiter.api.Test;

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
        assertThat(Operator.ADDITION.apply(new A(), new A())).isInstanceOf(A.class);
    }

    @Test
    public void stringify() {
        assertThat(Operator.ADDITION.stringify(new A(), new A())).isEqualTo("<a> + <a>");
        assertThat(Operator.ADDITION.getStringify().apply("x", "y")).isEqualTo("x + y");
    }

    @Test
    public void illegalState() {
        assertThatThrownBy(() -> {
            Operator.ADDITION.apply(new ANull(), new A());
        }).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void illegalArgument() {
        assertThatThrownBy(() -> {
            Operator.ADDITION.apply(new AIllegalArgument(), new A());
        }).isInstanceOf(IllegalArgumentException.class).hasMessage("foo bar");
    }

    @Test
    public void wrongArgument() {
        assertThatThrownBy(() -> {
            Operator.MULTIPLICATION.apply(new A(), new A());
        }).isInstanceOf(IllegalArgumentException.class).hasMessage("org.meeuw.math.abstractalgebra.MultiplicativeSemiGroupElement.times(<a>,<a>): object is not an instance of declaring class");
    }

}
