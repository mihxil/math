package org.meeuw.physics;

import lombok.extern.log4j.Log4j2;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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


}
