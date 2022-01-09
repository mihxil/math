package org.meeuw.physics;

import java.util.ArrayList;
import java.util.Collection;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.test.MultiplicativeAbelianGroupTheory;
import org.meeuw.math.text.spi.FormatService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.meeuw.physics.SI.DecimalPrefix.k;
import static org.meeuw.physics.SI.INSTANCE;
import static org.meeuw.physics.SI.km;
import static org.meeuw.physics.SIUnit.m;
import static org.meeuw.physics.SIUnit.s;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class UnitsImplTest implements MultiplicativeAbelianGroupTheory<Units> {

    final Units DISTANCE = INSTANCE.forQuantity(Quantity.DISTANCE);
    final Units TIME = INSTANCE.forQuantity(Quantity.TIME);


    @Override
    public Arbitrary<Units> elements() {
        Collection<Units> units = new ArrayList<>();
        units.addAll(INSTANCE.getUnits());
        units.addAll(CGS.INSTANCE.getUnits());
        units.addAll(Planck.INSTANCE.getUnits());
        return Arbitraries.of(units);
    }

    @Test
    public void distanceAndTime() {
        assertThat(DISTANCE).isEqualTo(SIUnit.m);
        assertThat(TIME).isEqualTo(SIUnit.s);
    }


    @Test
    public void reciprocal() {
        Units perKm = UnitsGroup.INSTANCE.one().dividedBy(m.withPrefix(k));
        assertThat(perKm.reciprocal()).isEqualTo(km);
        assertThat(km.pow(-1)).isEqualTo(perKm);
        assertThat(km.reciprocal()).isEqualTo(perKm);

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

    @Test
    public void parse() {
        assertThat(FormatService.fromString("/s", Units.class)).isEqualTo(Units.of(SIUnit.s).reciprocal());
        assertThat(FormatService.fromString("", Units.class)).isEqualTo(Units.DIMENSIONLESS);
    }
}
