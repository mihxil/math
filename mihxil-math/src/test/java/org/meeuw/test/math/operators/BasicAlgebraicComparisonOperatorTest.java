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

import org.junit.jupiter.api.Test;

import org.meeuw.math.operators.BasicComparisonOperator;
import org.meeuw.test.math.sample.SampleElement;

import static org.assertj.core.api.Assertions.assertThat;

public class BasicAlgebraicComparisonOperatorTest {

    @Test
    public void test() {
        SampleElement e1 = new SampleElement();
        SampleElement e2 = new SampleElement();
        assertThat(BasicComparisonOperator.EQ.test(e1, e2)).isFalse();
        assertThat(BasicComparisonOperator.EQ.test(e1, e1)).isTrue();

        assertThat(BasicComparisonOperator.EQ.stringify("a", "b")).isEqualTo("a ≈ b");

        assertThat(BasicComparisonOperator.EQ.getSymbol()).isEqualTo("≈");
    }



}
