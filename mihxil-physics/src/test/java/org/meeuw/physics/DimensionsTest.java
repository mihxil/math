package org.meeuw.physics;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;

import org.junit.jupiter.api.Test;
import org.meeuw.math.abstractalgebra.test.MultiplicativeAbelianGroupTheory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.physics.Dimension.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class DimensionsTest implements MultiplicativeAbelianGroupTheory<Dimensions> {

    @Test
    public void string() {
        Dimensions of = Dimensions.of(I, I);
        assertThat(of.toString()).isEqualTo("I²");
        assertThat(of.dividedBy(of).toString()).isEqualTo("1");

    }

    @Override
    public Arbitrary<Dimensions> elements() {
        return Arbitraries.of(
            Dimensions.of(I, L),
            Dimensions.of(I, I,T, Θ)
        );
    }

}
