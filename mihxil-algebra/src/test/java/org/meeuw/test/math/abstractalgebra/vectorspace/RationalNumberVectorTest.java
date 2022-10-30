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
package org.meeuw.test.math.abstractalgebra.vectorspace;

import java.util.Arrays;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.math.abstractalgebra.test.VectorSpaceTheory;
import org.meeuw.math.abstractalgebra.test.WithScalarTheory;
import org.meeuw.math.abstractalgebra.vectorspace.NVector;
import org.meeuw.math.abstractalgebra.vectorspace.NVectorSpace;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumbers.INSTANCE;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class RationalNumberVectorTest implements
    VectorSpaceTheory<NVector<RationalNumber>, RationalNumber>,
    WithScalarTheory<NVector<RationalNumber>, RationalNumber>
{


    @Test
    public void space() {
        NVectorSpace<RationalNumber> space4 = NVectorSpace.of(4, INSTANCE);
        NVectorSpace<RationalNumber> space3 = NVectorSpace.of(3, INSTANCE);
        assertThat(space3.equals(space4)).isFalse();
        assertThat(space3.equals(space3)).isTrue();
        assertThat(space3.hashCode() == new NVectorSpace<>(3, INSTANCE).hashCode()).isTrue();
    }



    @SuppressWarnings({"EqualsWithItself", "EqualsBetweenInconvertibleTypes"})
    @Test
    public void stringEqualsHashCode() {
        NVector<RationalNumber> example = of(1, 2, 3);
        assertThat(example.toString()).isEqualTo("(1, 2, 3)");
        assertThat(example.getSpace().toString()).isEqualTo("ℚ³");

        assertThat(example.equals(example)).isTrue();
        assertThat(example.equals("bla")).isFalse();
        assertThat(example.equals(of(3, 2, 1))).isFalse();
        assertThat(example.equals(of(1, 2, 3, 4))).isFalse();
        assertThat(example.equals(of(1, 2, 4))).isFalse();
        assertThat(example.equals(of(2, 2, 3))).isFalse();
        assertThat(example.equals(of(1, 2, 3))).isTrue();

        assertThat(example.hashCode()).isEqualTo(of(1, 2, 3).hashCode());
    }


    NVector<RationalNumber> of(long... a) {
        return NVector.of(Arrays.stream(a).mapToObj(RationalNumber::of).toArray(RationalNumber[]::new));
    }

    @Override
    public Arbitrary<? extends NVector<RationalNumber>> elements() {
        return Arbitraries
            .randomValue(INSTANCE::nextRandom)
            .tuple3()
            .map((t) -> NVector.of(t.get1(), t.get2(), t.get3()));
    }

    @Override
    public Arbitrary<RationalNumber> scalars() {
        return Arbitraries
            .randomValue(INSTANCE::nextRandom)
            .injectDuplicates(1)
            .dontShrink()
            .edgeCases(config -> {
                config.add(RationalNumber.ONE);
                config.add(RationalNumber.ZERO);
            });
    }
}
