package org.meeuw.math.abstractalgebra.integers;

import lombok.extern.log4j.Log4j2;

import net.jqwik.api.*;
import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

import org.meeuw.math.abstractalgebra.test.FieldTheory;
import org.meeuw.math.exceptions.InvalidElementCreationException;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Log4j2
class ModuloFieldElementTest  {

    @Test
    public void illegal() {
        Assertions.assertThatThrownBy(() ->
            ModuloField.of(12) // must be (power of a) prime
        ).isInstanceOf(InvalidElementCreationException.class);
    }
    public static abstract class AbstractTest implements FieldTheory<ModuloFieldElement> {
        final ModuloField structure;

        protected AbstractTest(ModuloField structure) {
            this.structure = structure;
        }

        @Test
        public void multtable() {
            structure.stream().forEach(e1 ->
                structure.stream().forEach(e2 -> {
                    log.info(e1 + "x" + e2 + "=" + e1.times(e2));
                    }
                )
            );
        }
        @Provide
        public Arbitrary<ModuloFieldElement> elements() {
            return Arbitraries.integers()
                .between(0, (int) structure.getCardinality().getValue() - 1)
                .map(structure::element)
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
