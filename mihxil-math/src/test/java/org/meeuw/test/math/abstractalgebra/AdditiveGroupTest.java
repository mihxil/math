/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.test.math.abstractalgebra;

import org.junit.jupiter.api.Test;

import org.meeuw.math.operators.BasicAlgebraicBinaryOperator;
import org.meeuw.math.operators.BasicAlgebraicUnaryOperator;
import org.meeuw.test.math.sample.SampleStructure;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 */
class AdditiveGroupTest {

    @Test
    public void additiveGroupOperators() {
        assertThat(new SampleStructure().getSupportedOperators())
            .contains(BasicAlgebraicBinaryOperator.ADDITION, BasicAlgebraicBinaryOperator.SUBTRACTION);

    }
    @Test
    public void additiveGroupUnaryOperators() {
        assertThat(new SampleStructure().getSupportedUnaryOperators())
            .contains(BasicAlgebraicUnaryOperator.NEGATION);
    }

}
