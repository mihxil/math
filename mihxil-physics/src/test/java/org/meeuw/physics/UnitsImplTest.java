package org.meeuw.physics;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;

import org.junit.jupiter.api.Test;
import org.meeuw.math.abstractalgebra.test.MultiplicativeAbelianGroupTheory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.meeuw.physics.SI.*;
import static org.meeuw.physics.SIUnit.m;
import static org.meeuw.physics.SIUnit.s;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class UnitsImplTest implements MultiplicativeAbelianGroupTheory<Units> {

    Units DISTANCE = INSTANCE.forDimensions(DimensionalAnalysis.DISTANCE);
    Units TIME = INSTANCE.forDimensions(DimensionalAnalysis.TIME);

    @Override
    public Arbitrary<Units> elements() {
        return Arbitraries.of(DimensionalAnalysis.getQuantities()).map(INSTANCE::forDimensions);
    }

    @Test
    public void forDivision() {
        assertThat(Units.forDivision(DISTANCE, TIME).toString()).isEqualTo("m·s⁻¹");
        assertThat(Units.forDivision(null, TIME)).isNull();
        assertThat(Units.forDivision(DISTANCE, null)).isNull();
    }

    @Test
    public void forMultiplication() {
        assertThat(Units.forMultiplication(DISTANCE, TIME)).isEqualTo(Units.of(m, s));
        assertThat(Units.forMultiplication(null, TIME)).isNull();
        assertThat(Units.forMultiplication(DISTANCE, null)).isNull();
    }

    @Test
    public void forAddition() {
        assertThatThrownBy(() -> Units.forAddition(DISTANCE, TIME)).isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Cannot add [L] to [T]");
        assertThatThrownBy(() -> Units.forAddition(null, TIME)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Units.forAddition(DISTANCE, null)).isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    public void forExponentiation() {
        assertThat(Units.forExponentiation(DISTANCE, 2)).isEqualTo(Units.of(m, m));
        assertThat(Units.forExponentiation(null, 2)).isNull();
    }

    @Test
    public void forInversion() {
        assertThat(Units.forInversion(DISTANCE).toString()).isEqualTo("m⁻¹");
        assertThat(Units.forInversion(null)).isNull();
    }
}
