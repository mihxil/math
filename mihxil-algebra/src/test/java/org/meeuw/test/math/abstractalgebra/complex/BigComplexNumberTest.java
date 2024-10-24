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

import net.jqwik.api.*;
import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.MultiplicativeSemiGroupElement;
import org.meeuw.math.abstractalgebra.complex.BigComplexNumber;
import org.meeuw.math.abstractalgebra.complex.BigComplexNumbers;
import org.meeuw.math.abstractalgebra.reals.BigDecimalElement;
import org.meeuw.theories.abstractalgebra.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.abstractalgebra.reals.BigDecimalElement.of;
import static org.meeuw.math.uncertainnumbers.CompareConfiguration.withLooseEquals;

/**
 * @author Michiel Meeuwissen
 * @since 0.8
 */
class BigComplexNumberTest implements
    CompleteFieldTheory<BigComplexNumber>,
    MetricSpaceTheory<BigComplexNumber, BigDecimalElement>,
    WithScalarTheory<BigComplexNumber, BigDecimalElement> {

    static final BigComplexNumbers structure = BigComplexNumbers.INSTANCE;

    @Test
    public void isMultiplicativeSemiGroupElement() {
        BigComplexNumber cn = BigComplexNumber.of(of(1), of(1));
        assertThat(cn).isInstanceOf(MultiplicativeSemiGroupElement.class);
    }
    @Test
    public void testOf() {
        assertThat(BigComplexNumber.of(BigDecimalElement.ONE)).isEqualTo(BigComplexNumber.of(BigDecimalElement.ONE, BigDecimalElement.ZERO));
    }


    @Test
    public void tetration() {
        BigComplexNumber i = BigComplexNumber.of(of(0), of(1));

        withLooseEquals(() -> {
            assertThat(i.tetrate(1)).isEqualTo(i);
            assertThat(i.tetrate(2)).isEqualTo(
                BigComplexNumber.of(
                    of(".2078795763507619085469556198349787700338778416317696080751358830554198772854821397886002778654260353")
                )
            );
            assertThat(i.tetrate(3)).isEqualTo(
                BigComplexNumber.of("0.9471589980723783806534753520181933335039061339031493636713681179446929279300488084526262684626490224 + 0.3207644499793085346601168458748631401023670206812767998296571687407552221593630018130863397275275956i")
            );
        });
    }

    @Override
    public Arbitrary<BigComplexNumber> elements() {
        Arbitrary<Double> real = Arbitraries.doubles().between(-100, 100);
        Arbitrary<Double> imaginary = Arbitraries.doubles().between(-100, 100);

        return Combinators.combine(real, imaginary).as((r, i)
            ->
                BigComplexNumber.of(
                    of(r),
                    of(i))
            )
            .edgeCases(config -> {
                config.add(structure.i());
                config.add(structure.one());
                config.add(structure.zero());
            })
            ;
    }

    @Override
    public Arbitrary<BigDecimalElement> scalars() {
        return Arbitraries.of(
            of(0),
            of(1), of(2), of(-1)
        );
    }

}
