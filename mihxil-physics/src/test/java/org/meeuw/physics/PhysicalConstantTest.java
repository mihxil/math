package org.meeuw.physics;

import lombok.extern.log4j.Log4j2;

import org.junit.jupiter.api.Test;
import org.meeuw.math.text.configuration.UncertaintyConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.text.configuration.UncertaintyConfiguration.Notation.PARENTHESES;
import static org.meeuw.math.text.spi.FormatServiceProvider.with;
import static org.meeuw.physics.PhysicalConstant.*;

/**
 * @author Michiel Meeuwissen
 */
@Log4j2
class PhysicalConstantTest {

    @Test
    public void NA() {
        assertThat(NA.toString()).isEqualTo("6.02214076·10²³ mol⁻¹");
        assertThat(NA.getName()).isEqualTo("Avogadro's number");
        log.info("{}={}", NA.getSymbol(), NA.toString());
    }

    @Test
    public void c() {
        assertThat(c.toString()).isEqualTo("2.99792458·10⁸ m·s⁻¹");
    }

    @Test
    public void h() {
        assertThat(h.toString()).isEqualTo("6.62607015·10⁻³⁴ J·s");
        assertThat(hbar.toString()).isEqualTo("1.05457181764616·10⁻³⁴ J·s");
        log.info("{}={}", hbar.getSymbol(), hbar.toString());
    }

    @Test
    public void G() {
        assertThat(G.toString()).isEqualTo("(6.67430 ± 0.00015)·10⁻¹¹ m³·kg⁻¹·s⁻²");
        log.info("{}={}", G.getSymbol(), G.toString());

        with(UncertaintyConfiguration.class, (ub) -> ub.withNotation(PARENTHESES),
            () -> assertThat(G.toString()).isEqualTo("6.67430(15)·10⁻¹¹ m³·kg⁻¹·s⁻²")
        );
    }

}
