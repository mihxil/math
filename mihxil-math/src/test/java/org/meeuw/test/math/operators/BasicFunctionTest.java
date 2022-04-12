/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.test.math.operators;

import lombok.EqualsAndHashCode;

import java.util.function.Function;

import org.junit.jupiter.api.Test;

import org.meeuw.math.operators.BasicFunction;
import org.meeuw.test.math.sample.MyException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class BasicFunctionTest {

    @EqualsAndHashCode
    public static class A {

        final int i;

        public A(int i) {
            this.i = i;
        }
        public A abs() {
            return new A(Math.abs(i));
        }

        @Override
        public String toString() {
            return "a:" + i;
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

    @Test
    public void andThen() {
        assertThat(BasicFunction.ABS.andThen(new Function<A, String>() {
            @Override
            public String apply(A a) {
                return String.valueOf(a.i + 1);
            }
        }).apply(new A(-1))).isEqualTo("2");
    }

    @Test
    public void compose() {
        assertThat(BasicFunction.ABS.compose(new Function<String, A>() {
            @Override
            public A apply(String a) {
                return new A(Integer.parseInt(a) - 1);
            }
        }).apply("-5")).isEqualTo(new A(6));
    }
}
