package org.meeuw.math.abstractalgebra.integers;

import net.jqwik.api.*;

import org.junit.jupiter.api.Test;
import org.meeuw.math.abstractalgebra.test.FieldTheory;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class ModuloFieldElementTest implements FieldTheory<ModuloFieldElement> {

    @Test


    @Override
    @Provide
    public Arbitrary<? extends ModuloFieldElement> elements() {
        ModuloField structure = ModuloField.of(13);
        return Arbitraries.integers().between(0, 12).map(i -> new ModuloFieldElement(i, structure)).injectDuplicates(0.1);
    }
}
