package org.meeuw.math.abstractalgebra.integers;

import lombok.extern.log4j.Log4j2;

import net.jqwik.api.*;
import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

import org.meeuw.math.abstractalgebra.test.FieldTheory;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.exceptions.InvalidStructureCreationException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Log4j2
class ModuloFieldElementTest  {

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


    public static abstract class AbstractTest implements FieldTheory<ModuloFieldElement> {
        final ModuloField structure;

        protected AbstractTest(ModuloField structure) {
            this.structure = structure;
        }

        @Test
        public void multiplicationTable() {
            structure.multiplicationTable().forEach(log::info);
        }

        @Provide
        public Arbitrary<ModuloFieldElement> elements() {
            return Arbitraries.integers()
                .between(0, (int) structure.getCardinality().getValue() - 1)
                .map(structure::element)
                .edgeCases(config -> {
                    config.add(structure.zero);
                    config.add(structure.one);
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
