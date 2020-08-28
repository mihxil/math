package org.meeuw.math.abstractalgebra.modulo;

import net.jqwik.api.*;

import org.meeuw.math.abstractalgebra.test.RingTheory;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class ModuloRingElementTest implements RingTheory<ModuloRingElement> {

    @Override
    @Provide
    public Arbitrary<? extends ModuloRingElement> elements() {
        ModuloRing structure = ModuloRing.of(10);
        return Arbitraries.integers().between(0, 10).map(i -> new ModuloRingElement(i, structure));
    }
}
