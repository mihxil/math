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

import lombok.extern.java.Log;

import java.math.MathContext;

import net.jqwik.api.*;
import org.junit.jupiter.api.Test;

import org.meeuw.configuration.ConfigurationService;
import org.meeuw.math.abstractalgebra.MultiplicativeSemiGroupElement;
import org.meeuw.math.abstractalgebra.complex.ComplexNumber;
import org.meeuw.math.abstractalgebra.complex.ComplexNumbers;
import org.meeuw.math.abstractalgebra.reals.RealField;
import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.math.exceptions.IllegalLogarithmException;
import org.meeuw.math.numbers.MathContextConfiguration;
import org.meeuw.theories.abstractalgebra.*;

import static org.assertj.core.api.Assumptions.assumeThat;
import static org.meeuw.assertj.Assertions.assertThat;
import static org.meeuw.configuration.ConfigurationService.setConfiguration;
import static org.meeuw.math.abstractalgebra.complex.ComplexNumber.imaginary;
import static org.meeuw.math.abstractalgebra.reals.RealNumber.real;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Log
class ComplexNumberTest implements
    CompleteFieldTheory<ComplexNumber>,
    MetricSpaceTheory<ComplexNumber, RealNumber>,
    WithScalarTheory<ComplexNumber, RealNumber> {

    static final ComplexNumbers structure = ComplexNumbers.INSTANCE;

    @Test
    public void isMultiplicativeSemiGroupElement() {
        ComplexNumber cn = ComplexNumber.of(real(1), real(1));
        assertThat(cn).isInstanceOf(MultiplicativeSemiGroupElement.class);
    }


    @Override
    public Arbitrary<ComplexNumber> elements() {
        return Arbitraries.randomValue(
            (random) ->
                ComplexNumber.of(
                    real(200 * random.nextDouble() - 100),
                    real(200 * random.nextDouble() - 100)))
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
        assertThat(ComplexNumber.real(real(-1)).sqrt()).isEqTo(imaginary(real(1)));

        assertThat(ComplexNumber.of(real(0), real(-1)).sqrt()
            .toString()).isEqualTo("0.707106781186548 - 0.707106781186548i");
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

        log.info("" +  result1);
    }

    @Test
    public void euler() {
        assertThat(
            ComplexNumber.real(RealField.INSTANCE.e())
                .pow(
                    ComplexNumbers.INSTANCE.i().times(RealField.INSTANCE.pi()))
        ).isEqTo(ComplexNumber.real(-1));
    }

    @Test
    public void i8() {
        var i8 = ComplexNumber.of(0, 8);
        var minusi8 = i8.negation();
        log.info("" +  i8.sqrt().plus(minusi8.sqrt()));
        log.info("" + ComplexNumbers.INSTANCE.i().sqrt());
    }

    @Test
    void cosOfBig() {
        var e = ComplexNumber.of(-65.7229978396012, 14.10973179331796);

        ComplexNumber cos = e.cos();
        assertThat(cos.asin().sin()).isEqTo(cos);
        ComplexNumber   cosAcos = cos.acos();
        ComplexNumber cos2 = cosAcos.cos();
        assertThat(cos2).withFailMessage(
            String.format("cos(acos(cos(%s))) = cos(acos(%s)) = cos(%s) = %s !=  cos(%s) = %s", e, cos, cosAcos, cos2, e, cos)

        ).isEqTo(cos);

    }

    @Test
    void sinOfBig() {
        var e = ComplexNumber.of(-15.70774780923645,  - 75.6674698917352);

        var sin = e.sin();
        var sinAsin = sin.asin();
        var sin2 = sinAsin.sin();
        assertThat(sin2).withFailMessage(
            String.format("sin(asin(sin(%s))) = sin(asin(%s)) = sin(%s) = %s !=  sin(%s) = %s",
                e, sin, sinAsin, sin2, e, sin)
        ).isEqTo(sin);
    }

    @Property
    public void eml(@ForAll(ELEMENTS) ComplexNumber x) {
        ComplexNumbers s = x.getStructure();
        try (ConfigurationService.Reset res = setConfiguration(builder ->
            builder.configure(MathContextConfiguration.class,
                (mathContextConfiguration) -> mathContextConfiguration.withContext(new MathContext(4))))) {
            assertThat(x.eml(s.one())).isEqTo(x.exp());

            //ln(x)=eml(1,eml(eml(1,x),1)),
            assertThat(s.one().eml(s.one().eml(x).eml(s.one()))).isEqTo(x.ln());
            log.info("eml " + x);
        } catch (IllegalLogarithmException illegalLogarithmException) {
            assumeThat(illegalLogarithmException.getReason()).isEqualTo(IllegalLogarithmException.Reason.ZERO);
        } finally {
            ConfigurationService.resetToDefaults();

        }
    }

}
