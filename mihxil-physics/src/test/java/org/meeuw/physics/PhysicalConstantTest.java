package org.meeuw.physics;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 */
class PhysicalConstantTest {

    @Test
    public void NA() {
        assertThat(PhysicalConstant.NA.toString()).isEqualTo("6.022·10²³ mol⁻¹");
        assertThat(PhysicalConstant.NA.getName()).isEqualTo("Avogadro's number");

    }

    @Test
    public void c() {
        assertThat(PhysicalConstant.c.toString()).isEqualTo("2.998·10⁸ m·s⁻¹");
    }

    @Test
    public void h() {
        assertThat(PhysicalConstant.h.toString()).isEqualTo("6.626·10⁻²⁶ J·s");
    }

}
