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
class DimensionalAnalysisTest implements MultiplicativeAbelianGroupTheory<DimensionalAnalysis> {

    @Test
    public void string() {
        DimensionalAnalysis of = DimensionalAnalysis.of(I, I);
        assertThat(of.toString()).isEqualTo("I²");
        assertThat(of.dividedBy(of).toString()).isEqualTo("1");

    }

    @Override
    public Arbitrary<DimensionalAnalysis> elements() {
        return Arbitraries.of(
            DimensionalAnalysis.of(I, L),
            DimensionalAnalysis.of(I, I,T, Θ)
        );
    }

}
