/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.test.physics;

import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;

import org.junit.jupiter.api.Test;

import org.meeuw.configuration.ConfigurationService;
import org.meeuw.math.text.configuration.UncertaintyConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.text.configuration.UncertaintyConfiguration.Notation.PARENTHESES;
import static org.meeuw.physics.PhysicalConstant.*;

/**
 * @author Michiel Meeuwissen
 */
@Log
class PhysicalConstantTest {

    @Test
    public void NA() {
        assertThat(NA.toString()).isEqualTo("6.02214076·10²³ mol⁻¹");
        assertThat(NA.getName()).isEqualTo("Avogadro's number");
        log.info("%s=%s".formatted(NA.getSymbol(), NA.toString()));
    }

    @Test
    public void c() {
        assertThat(c.toString()).isEqualTo("2.99792458·10⁸ m·s⁻¹");
    }

    @Test
    public void h() {
        assertThat(h.toString()).isEqualTo("6.62607015·10⁻³⁴ J·s");
        assertThat(hbar.toString()).isEqualTo("1.05457181764616·10⁻³⁴ J·s");
        log.info(hbar.getSymbol() + "=" + hbar);
    }

    @Test
    public void G() {
        assertThat(G.toString()).isEqualTo("(6.67430 ± 0.00015)·10⁻¹¹ m³·kg⁻¹·s⁻²");
        log.info(G.getSymbol() + "+" + G);

        ConfigurationService.withAspect(UncertaintyConfiguration.class, (ub) -> ub.withNotation(PARENTHESES),
            () -> assertThat(G.toString()).isEqualTo("6.67430(15)·10⁻¹¹ m³·kg⁻¹·s⁻²")
        );
    }

    @Test
    public void kB() {
        assertThat(kB.toString()).isEqualTo("(1.3806485 ± 0.0000008)·10⁻²³ J·K⁻¹");
    }

}
