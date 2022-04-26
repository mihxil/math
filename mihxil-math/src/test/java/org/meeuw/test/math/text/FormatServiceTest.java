/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.test.math.text;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Isolated;

import org.meeuw.configuration.ConfigurationService;
import org.meeuw.math.text.spi.AlgebraicElementFormatProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.text.FormatService.getProviders;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Isolated
class FormatServiceTest {

    @BeforeEach
    public void restoreDefaults() {
        ConfigurationService.resetToDefaultDefaults();
        ConfigurationService.resetToDefaults();
    }

    @Test
    public void getFormat() {
        assertThat(getProviders()
            .map(AlgebraicElementFormatProvider::toString))
            .contains(
                "UncertainDoubleFormatProvider [NumberConfiguration(minimalExponent=4, thousands=NONE), UncertaintyConfiguration(notation=PLUS_MINUS, considerRoundingErrorFactor=1000.0)]"
            );
    }
}