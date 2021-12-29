package org.meeuw.physics;

import lombok.extern.log4j.Log4j2;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.test.MultiplicativeAbelianGroupTheory;
import org.meeuw.math.abstractalgebra.test.SignedNumberTheory;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Log4j2
class PhysicalNumberTest implements
    MultiplicativeAbelianGroupTheory<PhysicalNumber>,
    SignedNumberTheory<PhysicalNumber> {

    @Test
    public void add() {
        PhysicalNumber lys = new Measurement(2, 0.1, SI.ly);
        PhysicalNumber psc = new Measurement(1, 0.1, SI.pc);
        log.info("{} + {} ({})= {}", lys, psc, psc.toUnits(SI.ly), lys.plus(psc));
        assertThat(lys.plus(psc).toString()).isEqualTo("5.3 ± 0.4 ly");
        assertThat(psc.plus(lys).toString()).isEqualTo("1.61 ± 0.13 pc");
    }

    @Test
    public void toUnits() {
        Units pc = Units.of(SI.pc);
        PhysicalNumber two_pc = new Measurement(2, 0.1, pc);

        PhysicalNumber inLightYear = two_pc.toUnits(Units.of(SI.ly));
        assertThat(inLightYear.getValue()).isEqualTo(6.523127554334867);
        assertThat(inLightYear.toString()).isEqualTo("6.5 ± 0.3 ly");

        assertThat(inLightYear.toUnits(SI.INSTANCE).toString()).isEqualTo("(6.2 ± 0.3)·10¹⁶ m");
        assertThat(inLightYear.toUnits(CGS.INSTANCE).toString()).isEqualTo("(6.2 ± 0.3)·10¹⁸ cm");
        assertThat(inLightYear.toUnits(Planck.INSTANCE).toString()).isEqualTo("(3.82 ± 0.17)·10⁵¹ ℓₚ");
    }

    @Test
    public void setPrefix() {
        Units km = Units.of(SIUnit.m);

    }

    @Test
    public void lt() {
        PhysicalNumber two_lightyear = new Measurement(2, 0.1, SI.ly);
        PhysicalNumber three_km = new Measurement(3, 0.1, SIUnit.m);
        assertThat(three_km.lt(two_lightyear)).isTrue();
    }

    /**
     * Returns only velocities for now.
     *
     */
    @Override
    public Arbitrary<PhysicalNumber> elements() {
        return
            Arbitraries
                .<PhysicalNumber>randomValue(
                    (random) -> new Measurement(
                        random.nextDouble() * 200 - 100,
                        Math.abs(random.nextDouble() * 10),
                        SI.VELOCITY
                    )
                )
                .injectDuplicates(0.01)
                .dontShrink()
                .edgeCases(config -> {
                    config.add(new Measurement(0, 0.001, SI.VELOCITY));
                    config.add(PhysicalConstant.c);
                });

    }
}
