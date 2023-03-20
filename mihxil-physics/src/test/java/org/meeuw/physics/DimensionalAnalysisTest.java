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
package org.meeuw.physics;

import lombok.extern.log4j.Log4j2;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import org.junit.jupiter.api.Test;

import org.meeuw.theories.abstractalgebra.MultiplicativeAbelianGroupTheory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.physics.Dimension.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Log4j2
class DimensionalAnalysisTest implements MultiplicativeAbelianGroupTheory<DimensionalAnalysis> {

    @Test
    public void string() {
        DimensionalAnalysis of = DimensionalAnalysis.of(I, I);
        assertThat(of.toString()).isEqualTo("I²");
        assertThat(of.dividedBy(of).toString()).isEqualTo("1");
    }

    @Override
    public Arbitrary<DimensionalAnalysis> elements() {
        return Arbitraries.of(
            DimensionalAnalysis.of(I, L),
            DimensionalAnalysis.of(I, I,T, Θ)
        );
    }

}
