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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.assertj.core.api.Assertions;

import org.meeuw.math.exceptions.InvalidAlgebraicResult;
import org.meeuw.math.exceptions.NoSuchOperatorException;
import org.meeuw.math.operators.BasicAlgebraicBinaryOperator;
import org.meeuw.test.math.sample.MyException;
import org.meeuw.test.math.sample.SampleElement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.parallel.ExecutionMode.SAME_THREAD;
import static org.meeuw.math.operators.BasicAlgebraicBinaryOperator.*;


@Execution(SAME_THREAD)
public class BasicAlgebraicBinaryOperatorTest {

   static {
        Assertions.setMaxStackTraceElementsDisplayed(10);
    }
    @BeforeEach
    void setup() {
        SampleElement.PLUS.remove();
    }

    @Test
    public void powInverse() {
        assertThatThrownBy(() ->
            POWER.inverse(new SampleElement())
        ).isInstanceOf(NoSuchOperatorException.class);

    }

    @Test
    public void addInverse() {
        assertThatThrownBy(() ->
            ADDITION.inverse(new SampleElement())
        ).isInstanceOf(InvalidAlgebraicResult.class);

    }

    @Test
    public void throwsException() {
        SampleElement.PLUS.set((a, b) -> { throw new NullPointerException();});
        assertThatThrownBy(() -> {
            ADDITION.apply(new SampleElement(), new SampleElement());
        }).isInstanceOf(NullPointerException.class);

    }

    @Test
    public void returnsNull() {
        SampleElement.PLUS.set((a, b) -> null);
        assertThatThrownBy(() -> {
            ADDITION.apply(new SampleElement(), new SampleElement());
        }).isInstanceOf(InvalidAlgebraicResult.class);

    }

    @Test
    public void callNonExisting() {
        assertThatThrownBy(() -> {
            MULTIPLICATION.apply(new SampleElement(), new SampleElement());
        }).isInstanceOf(NoSuchOperatorException.class);

    }



    @Test
    public void add() {
        Assertions.assertThat(ADDITION.apply(new SampleElement(), new SampleElement())).isInstanceOf(SampleElement.class);
    }

    @Test
    public void stringify() {
        assertThat(ADDITION.stringify(new SampleElement(), new SampleElement())).isEqualTo("sampleelement + sampleelement");
        assertThat(ADDITION.getStringify().apply("x", "y")).isEqualTo("x + y");
    }

    @Test
    public void invalidResult() {
        assertThatThrownBy(() ->
            ADDITION.apply(new SampleElement((a, b) -> null), new SampleElement())
        ).isInstanceOf(InvalidAlgebraicResult.class);
    }

    @Test
    public void myException() {
        assertThatThrownBy(() ->
            ADDITION.apply(new SampleElement((a, b) -> {throw new MyException("foo bar");}), new SampleElement())
        ).isInstanceOf(MyException.class).hasMessage("foo bar");
    }

    @Test
    public void wrongArgument() {
        assertThatThrownBy(() ->
            MULTIPLICATION.apply(new SampleElement(), new SampleElement())
        ).isInstanceOf(NoSuchOperatorException.class).hasMessage("SampleElement sampleelement has no operator 'times'");
    }

    @Test
    public void illegalArgument() {
        assertThatThrownBy(() ->
            BasicAlgebraicBinaryOperator.DIVISION.apply(new SampleElement(), new SampleElement())
        ).isInstanceOf(NoSuchOperatorException.class).hasMessage("SampleElement sampleelement has no operator 'dividedBy'");
    }




}
