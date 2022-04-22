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
package org.meeuw.test.math.text.spi;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Isolated;

import org.meeuw.configuration.Configuration;
import org.meeuw.configuration.ConfigurationService;
import org.meeuw.math.text.configuration.NumberConfiguration;
import org.meeuw.math.text.spi.AlgebraicElementFormatProvider;
import org.meeuw.test.math.text.spi.test.TestConfigurationAspect;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.text.spi.FormatService.getProviders;

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
                "TestFormatProvider [InvalidConfigurationAspect(someInt=1), TestConfigurationAspect]",
                "UncertainDoubleFormatProvider [NumberConfiguration(minimalExponent=4, thousands=NONE), UncertaintyConfiguration(notation=PLUS_MINUS, considerRoundingErrorFactor=1000.0)]"
            );
    }

    @Test
    public void getAndSetConfiguration() {
        Configuration configuration = ConfigurationService.getConfiguration();
        NumberConfiguration aspect = configuration.getAspect(NumberConfiguration.class);
        int minimalExponent = aspect.getMinimalExponent();
        ConfigurationService.setConfiguration(configuration.toBuilder()
            .configure(NumberConfiguration.class, (nc) -> nc.withMinimalExponent(8))
            .build()
        );
        assertThat(ConfigurationService.getConfiguration()
            .getAspectValue(NumberConfiguration.class, NumberConfiguration::getMinimalExponent)
        ).isEqualTo(8);
    }


    @Test
    public void testConfigurationAspects() {
        ConfigurationService.defaultConfiguration((con) -> con
            .configure(NumberConfiguration.class, c -> c.withMinimalExponent(4))
            .configure(TestConfigurationAspect.class,
                c -> c
                    .withSomeInt(-1)
                    .withSomeSerializable(null)
                    .withNotSerializable(new TestConfigurationAspect.NotSerializable(3, "x"))
            )
        );
        assertThat(ConfigurationService.getConfigurationAspect(NumberConfiguration.class).getMinimalExponent()).isEqualTo(4);

        ConfigurationService.withAspect(NumberConfiguration.class, (b) -> b.withMinimalExponent(6), () -> {
            assertThat(ConfigurationService.getConfigurationAspect(NumberConfiguration.class).getMinimalExponent()).isEqualTo(6);
            }
        );
        assertThat(ConfigurationService.getConfigurationAspect(NumberConfiguration.class).getMinimalExponent()).isEqualTo(4);

        ConfigurationService.defaultConfiguration((con) -> con.configure(NumberConfiguration.class,
            c -> c.withMinimalExponent(5))
        );
        assertThat(ConfigurationService.getConfigurationAspect(NumberConfiguration.class).getMinimalExponent()).isEqualTo(5);


        ConfigurationService.resetToDefaultDefaults();

        assertThat(ConfigurationService.getConfigurationAspect(NumberConfiguration.class).getMinimalExponent()).isEqualTo(4);


        ConfigurationService.withConfiguration((con) -> con
                .configure(TestConfigurationAspect.class, (tc) -> tc.withSomeInt(5))
                .configure(NumberConfiguration.class, (tc) -> tc.withMinimalExponent(3))
        , () -> {
                assertThat(ConfigurationService.getConfigurationAspect(TestConfigurationAspect.class).getSomeInt()).isEqualTo(5);
                assertThat(ConfigurationService.getConfigurationAspect(NumberConfiguration.class).getMinimalExponent()).isEqualTo(3);

        });


        assertThat(ConfigurationService.getConfigurationAspect(TestConfigurationAspect.class).getSomeInt()).isEqualTo(-1);
    }




}
