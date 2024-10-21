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
package org.meeuw.test.math.abstractalgebra.dim2.dim3;

import java.math.*;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.lifecycle.AfterProperty;
import net.jqwik.api.lifecycle.BeforeProperty;
import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.dim2.Matrix2;
import org.meeuw.math.abstractalgebra.dim2.Matrix2Group;

import org.meeuw.math.abstractalgebra.reals.RealField;
import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.theories.abstractalgebra.MultiplicativeGroupTheory;
import org.meeuw.theories.abstractalgebra.WithScalarTheory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.abstractalgebra.dim2.Matrix2.of;

/**
 * @author Michiel Meeuwissen
 */
class Matrix2Test implements MultiplicativeGroupTheory<Matrix2>, WithScalarTheory<Matrix2, RealNumber> {


    @BeforeProperty
    public void approx() {
        Matrix2Group.INSTANCE.setDoubleEquivalence((v1, v2) -> round(v1) == round(v2));
    }
    @AfterProperty
    public  void shutdown() {
        Matrix2Group.INSTANCE.clearDoubleEquivalence();
    }
    public static double round(double v) {
        if (Double.isNaN(v)){
            return v;
        }
        return BigDecimal.valueOf(v).round(new MathContext(2)).setScale(0, RoundingMode.HALF_UP).doubleValue();
    }

    @Override
    public Arbitrary<RealNumber> scalars() {
        return Arbitraries.randomValue((random) ->
            new RealNumber(random.nextDouble() * 200 - 100, random.nextDouble() * 10))
            .dontShrink()
            .edgeCases(config -> {
                config.add(RealField.INSTANCE.zero());
                config.add(RealField.INSTANCE.one());
                config.add(RealField.INSTANCE.one().times(-1));
            });
    }

    @Test
    void times() {
        Matrix2 example = of(
            1, 2,
            4, 5
        );
        assertThat(example.times(2).getValues()).isEqualTo(
            new double[][] {
                new double[] {2, 4},
                new double[] {8, 10}
            }
        );
        assertThat(example.times(example.getStructure().one()))
                .isEqualTo(example);
    }



    @Override
    public Arbitrary<Matrix2> elements() {
        return Arbitraries.of(
            of(
                1, 2,
                4, 5
            ),
            of(
                4, 2.2,
                0, 1
            )

        );
    }
}
