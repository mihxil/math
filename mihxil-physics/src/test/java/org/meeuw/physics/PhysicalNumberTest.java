package org.meeuw.physics;

import lombok.extern.log4j.Log4j2;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;

import org.junit.jupiter.api.Test;
import org.meeuw.math.abstractalgebra.test.MultiplicativeAbelianGroupTheory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.physics.UnitsImplTest.units;


/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Log4j2
class PhysicalNumberTest implements MultiplicativeAbelianGroupTheory<PhysicalNumber> {

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
        Units pc = UnitsImpl.of(SI.pc);
        PhysicalNumber two_pc = new Measurement(2, 0.1, pc);

        PhysicalNumber inLightYear = two_pc.toUnits(UnitsImpl.of(SI.ly));
        assertThat(inLightYear.getValue()).isEqualTo(6.523127554334867);
        assertThat(inLightYear.toString()).isEqualTo("6.5 ± 0.3 ly");


    }

    @Override
    public Arbitrary<PhysicalNumber> elements() {
        return Arbitraries
            .randomValue(
                (random) -> new Measurement(
                    random.nextDouble() * 100,
                    Math.abs(random.nextDouble() * 10),
                    units[random.nextInt(units.length)])
            );
/*
        return Arbitraries.of(
            new Measurement(0.6, 0.1, DISTANCE),
            PhysicalConstant.c,
            PhysicalNumbers.ONE
        ).,*/
    }
}
