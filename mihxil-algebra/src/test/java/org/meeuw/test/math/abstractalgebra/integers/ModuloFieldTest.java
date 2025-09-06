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
package org.meeuw.test.math.abstractalgebra.integers;

import lombok.extern.java.Log;

import java.util.Random;

import net.jqwik.api.*;
import org.junit.jupiter.api.*;
import org.assertj.core.api.Assertions;

import org.meeuw.math.abstractalgebra.integers.ModuloField;
import org.meeuw.math.abstractalgebra.integers.ModuloFieldElement;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.exceptions.InvalidStructureCreationException;
import org.meeuw.math.operators.AlgebraicBinaryOperator;
import org.meeuw.math.operators.BasicAlgebraicIntOperator;
import org.meeuw.theories.abstractalgebra.FieldTheory;
import org.meeuw.theories.abstractalgebra.ScalarFieldTheory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Log
class ModuloFieldTest implements ScalarFieldTheory<ModuloFieldElement> {

    @Test
    public void illegal() {
        assertThatThrownBy(() ->
            ModuloField.of(12) // must be (power of a) prime
        ).isInstanceOf(InvalidElementCreationException.class);
    }

    @Test
    public void illegalNegative() {
        assertThatThrownBy(() ->
            ModuloField.of(-3) // must be (power of a) prime
        ).isInstanceOf(InvalidStructureCreationException.class).hasMessage("Divisor of modulo structure must be > 0");
    }

    @Test
    public void illegal0() {
        Assertions.setMaxStackTraceElementsDisplayed(20);
        assertThatThrownBy(() ->
            ModuloField.of(0) //
        ).isInstanceOf(InvalidStructureCreationException.class);
    }

    @RepeatedTest(20)
    public void nextRandom() {
        ModuloField modulo23 = ModuloField.of(23);
        ModuloFieldElement moduloFieldElement = modulo23.nextRandom(new Random());
        log.info(moduloFieldElement.toString());
        assertThat(moduloFieldElement.getStructure()).isEqualTo(modulo23);
    }


    @Override
    public Arbitrary<ModuloFieldElement> elements() {
        return Arbitraries.of(
            ModuloField.of(23).stream().toArray(ModuloFieldElement[]::new)
        );
    }

    @Test
    public void dividedByLong() {
        ModuloFieldElement ten = ModuloField.of(23).element(10);
        assertThat(ten.dividedBy(5).getValue()).isEqualTo(2);
        assertThat(ten.dividedBy(9).getValue()).isEqualTo(19);
    }

    @Test
    public void fromString() {
        ModuloField f = ModuloField.of(23);
        ModuloFieldElement ten = f.fromString("10");
        assertThat(ten.getValue()).isEqualTo(10);

        ModuloFieldElement _22 = f.fromString("22");
        assertThat(_22.getValue()).isEqualTo(22);
    }




    public static abstract class AbstractTest implements FieldTheory<ModuloFieldElement> {
        final ModuloField structure;

        protected AbstractTest(ModuloField structure) {
            this.structure = structure;
        }

        @Test
        public void multiplicationTable() {
            structure.multiplicationTable().forEach(log::info);
        }

        @Override
        @Provide
        public Arbitrary<ModuloFieldElement> elements() {
            return Arbitraries.integers()
                .between(0, structure.getCardinality().getValue().intValue() - 1)
                .map(structure::element)
                .edgeCases(config -> {
                    config.add(structure.zero());
                    config.add(structure.one());
                })
                .injectDuplicates(0.1);
        }
    }

    @Nested
    @Group
    class Modulo13Test extends AbstractTest {

        protected Modulo13Test() {
            super(ModuloField.of(13));
        }

        @Test
        public void moduloPower() {
            var ten = structure.element(11);
            var big = 100_00_000_001L;
            var pow100million = ten.pow(big);
            log.info(BasicAlgebraicIntOperator.POWER.stringify(ten.toString(),  Long.toString(big)) + "=" + pow100million);

        }
        @Test
        public void simple() {
            AlgebraicBinaryOperator operationBySymbol = structure.getOperationBySymbol("*").get();
            assertThat(operationBySymbol.apply(structure.element(4), structure.element(7)).toString()).isEqualTo("2");
            assertThat(structure.fromString("4").times(structure.fromString("7")).toString()).isEqualTo("2");
        }
    }

    public static class Modulo2Test extends AbstractTest {

        protected Modulo2Test() {
            super(ModuloField.of(2));
        }

    }

}
