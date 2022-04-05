package org.meeuw.test.math.abstractalgebra.integers;

import lombok.extern.log4j.Log4j2;

import java.util.Random;

import net.jqwik.api.*;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

import org.meeuw.math.abstractalgebra.integers.ModuloField;
import org.meeuw.math.abstractalgebra.integers.ModuloFieldElement;
import org.meeuw.math.abstractalgebra.test.FieldTheory;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.exceptions.InvalidStructureCreationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Log4j2
class ModuloFieldElementTest implements FieldTheory<ModuloFieldElement> {

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
        log.info(moduloFieldElement);
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


    public static class Modulo13 extends AbstractTest {

        protected Modulo13() {
            super(ModuloField.of(13));
        }
    }

    public static class Modulo2 extends AbstractTest {

        protected Modulo2() {
            super(ModuloField.of(2));
        }

    }

}
