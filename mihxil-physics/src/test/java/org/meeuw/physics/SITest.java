package org.meeuw.physics;

import lombok.extern.log4j.Log4j2;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.physics.Dimension.L;
import static org.meeuw.physics.Dimension.T;
import static org.meeuw.physics.SI.INSTANCE;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Log4j2
class SITest {

    @Test
    public void ly() {
        assertThat(SI.ly.toString()).isEqualTo("ly");
        assertThat(SI.ly.toSI().toString()).isEqualTo("9.4607304725808·10¹⁵ m");
    }

    @Test
    public void test() {
        for (Prefix p : SI.Prefixes.values()) {
            log.info("{}: {} ", p, p.getAsDouble());
        }
    }

    @Test
    public void forDimension() {
        assertThat((Object) INSTANCE.forDimension(T)).isEqualTo(SIUnit.s);
    }

    @Test
    public void forDimensions() {
        assertThat(INSTANCE.forDimensions(L, T.with(-1)).toString()).isEqualTo("m·s⁻¹");
        assertThat(INSTANCE.forDimensions(DimensionalAnalysis.FORCE).toString()).isEqualTo("N");

        for (DimensionalAnalysis q : DimensionalAnalysis.getQuantities()) {
            log.info("{}: {}", q, INSTANCE.forDimensions(q));

        }
    }


}
