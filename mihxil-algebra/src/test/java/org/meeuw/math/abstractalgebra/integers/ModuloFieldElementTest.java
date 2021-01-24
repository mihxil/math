package org.meeuw.math.abstractalgebra.integers;

import net.jqwik.api.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.meeuw.math.abstractalgebra.test.FieldTheory;
import org.meeuw.math.exceptions.InvalidElementCreationException;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class ModuloFieldElementTest implements FieldTheory<ModuloFieldElement> {

    @Test
    public void illegal() {
        Assertions.assertThatThrownBy(() ->
            ModuloField.of(16) // must be prime
        ).isInstanceOf(InvalidElementCreationException.class);
    }

    @Override
    @Provide
    public Arbitrary<ModuloFieldElement> elements() {
        final ModuloField structure = ModuloField.of(13);
        return Arbitraries.integers().between(0, 12)
            .map(structure::element)
            .injectDuplicates(0.1);
    }
}
