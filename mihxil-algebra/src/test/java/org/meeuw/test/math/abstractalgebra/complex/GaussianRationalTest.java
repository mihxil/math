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
package org.meeuw.test.math.abstractalgebra.complex;

import lombok.extern.log4j.Log4j2;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;

import org.junit.jupiter.api.Test;
import org.meeuw.math.abstractalgebra.complex.*;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.theories.abstractalgebra.FieldTheory;
import org.meeuw.theories.abstractalgebra.WithScalarTheory;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Log4j2
class GaussianRationalTest implements
    FieldTheory<GaussianRational>,
    WithScalarTheory<GaussianRational, RationalNumber> {

    static final GaussianRationals structure = GaussianRationals.INSTANCE;

    @Test
    public void string() {
        assertThat(structure.zero().toString()).isEqualTo("0");
        assertThat(structure.one().toString()).isEqualTo("1");
        assertThat(structure.one().negation().toString()).isEqualTo("-1");
        assertThat(structure.i().toString()).isEqualTo("i");
        assertThat(structure.i().negation().toString()).isEqualTo("-i");
        assertThat(structure.i().times(structure.getElementStructure().one().times(RationalNumber.of(3))).toString()).isEqualTo("3i");
        assertThat(structure.one().plus(structure.i().times(structure.getElementStructure().one().times(RationalNumber.of(3)))).toString()).isEqualTo("1 + 3i");
        assertThat(structure.one().minus(structure.i().times(structure.getElementStructure().one().times(RationalNumber.of(3)))).toString()).isEqualTo("1 - 3i");

    }

    @Test
    public void isqr() {
        assertThat(structure.i().sqr()).isEqualTo(structure.one().negation());
    }

    @Test
    public void pow3Example() {
        GaussianRational gaussian = GaussianRational.of(RationalNumber.of(-77, 100),
            RationalNumber.of(-75, 64));
        assertThat(gaussian.pow(-3).toString()).isEqualTo("⁴⁵⁵⁶³⁰⁴⁷²⁴⁷⁸⁷²⁰⁰⁰⁰⁰⁰⁄₁₂₇₅₂₅₄₉₄₉₅₇₉₄₇₀₁₇₈₄₉ + ⁷⁹⁷⁰⁶⁶⁴⁹⁶⁰⁰⁰⁰⁰⁰⁰⁰⁰⁰⁄₁₂₇₅₂₅₄₉₄₉₅₇₉₄₇₀₁₇₈₄₉i");
    }

    @Test
    public void stream() {
        assertThat(structure.stream().limit(20)).map(AbstractComplexNumber::toString).containsExactly(
            "0",
            "1",
            "i",
            "-1",
            "1 + i",
            "-i",
            "2",
            "-1 + i",
            "1 - i",
            "2i",
            "-2",
            "2 + i",
            "-1 - i",
            "1 + 2i",
            "-2i",
            "¹⁄₂",
            "-2 + i",
            "2 - i",
            "-1 + 2i",
            "1 - 2i"
        );

    }

    @Override
    public Arbitrary<GaussianRational> elements() {
        return Arbitraries.randomValue(
            (random) ->
                GaussianRational.of(
                    RationalNumber.of(random.nextInt(200) - 100, random.nextInt(100) + 1),
                    RationalNumber.of(random.nextInt(200) - 100, random.nextInt(100) + 1))
        )
            .injectDuplicates(0.1)
            .dontShrink()
            .edgeCases(config -> {
                config.add(structure.i());
                config.add(structure.one());
                config.add(structure.zero());
            });
    }

    @Override
    public Arbitrary<RationalNumber> scalars() {
        return Arbitraries.of(
            RationalNumber.of(1),
            RationalNumber.of(0),
            RationalNumber.of(2),
            RationalNumber.of(-1)
        );
    }

    @Test
    public void parse() {
        assertThat(structure.parse("i")).isEqualTo(structure.i());
        assertThat(structure.parse("2i")).isEqualTo(structure.i().times(2));
        assertThat(structure.parse("1 - 2i")).isEqualTo(structure.one().minus(structure.i().times(2)));
        assertThat(structure.parse("1/2 - 2i")).isEqualTo(structure.one().dividedBy(2).minus(structure.i().times(2)));

        assertThat(structure.parse("1 1/2 - 2i")).isEqualTo(structure.one().times(3).dividedBy(2).minus(structure.i().times(2)));


    }
}
