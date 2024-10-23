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
