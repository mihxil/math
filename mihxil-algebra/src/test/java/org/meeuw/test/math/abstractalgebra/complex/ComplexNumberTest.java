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

import org.meeuw.math.abstractalgebra.MultiplicativeSemiGroupElement;
import org.meeuw.math.abstractalgebra.complex.ComplexNumber;
import org.meeuw.math.abstractalgebra.complex.ComplexNumbers;
import org.meeuw.math.abstractalgebra.reals.RealField;
import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.math.abstractalgebra.test.*;

import static org.meeuw.assertj.Assertions.assertThat;
import static org.meeuw.math.abstractalgebra.complex.ComplexNumber.real;
import static org.meeuw.math.abstractalgebra.reals.RealNumber.of;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Log4j2
class ComplexNumberTest implements
    CompleteFieldTheory<ComplexNumber>,
    MetricSpaceTheory<ComplexNumber, RealNumber>,
    WithScalarTheory<ComplexNumber, RealNumber> {

    static final ComplexNumbers structure = ComplexNumbers.INSTANCE;

    @Test
    public void isMultiplicativeSemiGroupElement() {
        ComplexNumber cn = new ComplexNumber(of(1), of(1));
        assertThat(cn).isInstanceOf(MultiplicativeSemiGroupElement.class);
    }


    @Override
    public Arbitrary<ComplexNumber> elements() {
        return Arbitraries.randomValue(
            (random) ->
                new ComplexNumber(
                    of(200 * random.nextDouble() - 100),
                    of(200 * random.nextDouble() - 100)))
            .injectDuplicates(0.1)
            .dontShrink()
            .edgeCases(config -> {
                config.add(structure.i());
                config.add(structure.one());
                config.add(structure.zero());
            });
    }

    @Override
    public Arbitrary<RealNumber> scalars() {
        return Arbitraries.of(
            RealNumber.of(0), RealNumber.of(1), RealNumber.of(2), RealNumber.of(-1)
        );
    }

    @Test
    public void sqrt() {
        assertThat(real(RealNumber.of(-1)).sqrt()).isEqTo(ComplexNumbers.INSTANCE.i());

        assertThat(ComplexNumber.of(RealNumber.of(0), RealNumber.of(-1)).sqrt().toString()).isEqualTo("0.70710678118655 - 0.70710678118655i");
    }

    @Test
    public void veryLarge() {
        ComplexNumber base1 = ComplexNumber.of(-705.6, -13);
        ComplexNumber exponent1 = ComplexNumber.of(198.8,-100);

        ComplexNumber result1 = base1.pow(exponent1);

        ComplexNumber base2 = ComplexNumber.of(-705.6, -13);
        ComplexNumber exponent2 = ComplexNumber.of(198.8,-100);

        ComplexNumber result2 = base2.pow(exponent2);

        assertThat(result1.eq(result2)).isTrue();

        log.info("{}", result1);
    }

    @Test
    public void euler() {
        assertThat(
            real(RealField.INSTANCE.e())
                .pow(
                    ComplexNumbers.INSTANCE.i().times(RealField.INSTANCE.pi()))
        ).isEqTo(real(-1));
    }
}
