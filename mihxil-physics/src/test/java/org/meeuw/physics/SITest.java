package org.meeuw.physics;

import lombok.extern.log4j.Log4j2;

import java.util.Optional;

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
    public void prefixes() {
        prefixes(SI.DecimalPrefix.none);
    }

    @Test
    public void binaryPrefixes() {
         prefixes(SI.BinaryPrefix.none);
    }

    public void prefixes(Prefix startPoint) {
        Optional<? extends Prefix> prefix = startPoint.inc();
        while (prefix.isPresent()) {
            log.info("{}: {} ", prefix.get(), prefix.get().getAsDouble());
            prefix = prefix.get().inc();
        }
        prefix = startPoint.dec();
        while (prefix.isPresent()) {
            log.info("{}: {} ", prefix.get(), prefix.get().getAsDouble());
            prefix = prefix.get().dec();
        }
    }

    @Test
    public void forDimension() {
        assertThat((Object) INSTANCE.forDimension(T)).isEqualTo(SIUnit.s);
    }

    @Test
    public void forDimensions() {
        assertThat(INSTANCE.forDimensions(L, T.with(-1)).toString()).isEqualTo("m·s⁻¹");
        assertThat(INSTANCE.forQuantity(Quantity.FORCE).toString()).isEqualTo("N");
    }

    @Test
    public void prefix() {
        Units kmPerS = SI.mPerS.withPrefix(SI.DecimalPrefix.k);
        assertThat(kmPerS.toString()).isEqualTo("km·s⁻¹");
        assertThat(kmPerS.getSIFactor().getValue()).isEqualTo(1000d);
        assertThat(SI.km.getSIFactor().getValue()).isEqualTo(1000d);
    }

    @Test
    public void getBaseUnits() {
        assertThat(INSTANCE.getBaseUnits().toString()).isEqualTo("[m, kg, s, A, K, mol, cd]");
    }

    @Test
    public void getUnits() {
        assertThat(INSTANCE.getUnits().toString()).isEqualTo("[m, kg, s, A, K, mol, cd, m·s⁻¹, N, g, Hz, Pa, J, min, h, eV, AU, pc, ly]");
    }

    @Test
    public  void getForQuantity() {
        assertThat(INSTANCE.forQuantity(Quantity.LUMINOUS_INTENSITY)).isEqualTo(SIUnit.cd);
        assertThat(INSTANCE.forQuantity(Quantity.ENERGY)).isEqualTo(SI.J);
    }


}
