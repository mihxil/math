package org.meeuw.physics;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;

import org.junit.jupiter.api.Test;
import org.meeuw.math.abstractalgebra.MultiplicativeAbelianGroupTheory;

import static org.meeuw.physics.Dimension.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class DimensionsTest implements MultiplicativeAbelianGroupTheory<Dimensions> {

    @Override
    public Arbitrary<Dimensions> elements() {
        return Arbitraries.of(
            Dimensions.of(I, L),
            Dimensions.of(I, I,T, TH)
        );
    }

    @Test
    public void stream() {

    }
}
