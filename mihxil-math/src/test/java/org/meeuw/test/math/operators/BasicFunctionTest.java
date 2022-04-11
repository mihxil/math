package org.meeuw.test.math.operators;

import org.junit.jupiter.api.Test;

import org.meeuw.math.operators.BasicFunction;
import org.meeuw.test.math.sample.MyException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class BasicFunctionTest {

    public static class A {

        final int i;

        public A(int i) {
            this.i = i;
        }
        public A abs() {
            return new A(Math.abs(i));
        }
    }

    public static class B {

        public B abs() {
            throw new MyException("foobar");
        }
    }

    public static class C {

        protected B abs() {
            return new B();
        }
    }

    @Test
    public void abs() {
        assertThatThrownBy(() -> {
            BasicFunction.ABS.apply("tests");
        }).isInstanceOf(NoSuchMethodException.class);

        assertThat(((A) BasicFunction.ABS.apply(new A(-1))).i).isEqualTo(1);

        assertThatThrownBy(() -> {
            BasicFunction.ABS.apply(new B());
        }).isInstanceOf(MyException.class);

        assertThatThrownBy(() -> {
            BasicFunction.ABS.apply(new C());
        }).isInstanceOf(NoSuchMethodException.class);
    }
}
