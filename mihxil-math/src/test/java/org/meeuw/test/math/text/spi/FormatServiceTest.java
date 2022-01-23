package org.meeuw.test.math.text.spi;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Isolated;

import org.meeuw.configuration.Configuration;
import org.meeuw.configuration.ConfigurationService;
import org.meeuw.math.text.configuration.NumberConfiguration;
import org.meeuw.math.text.spi.AlgebraicElementFormatProvider;
import org.meeuw.test.math.text.spi.test.TestConfigurationAspect;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.configuration.ConfigurationService.*;
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
                "TestFormatProvider [InvalidConfigurationAspect(someInt=1), TestConfigurationAspect(someInt=-1)]",
                "UncertainDoubleFormatProvider [NumberConfiguration(minimalExponent=4, thousands=NONE), UncertaintyConfiguration(notation=PLUS_MINUS, considerRoundingErrorFactor=1000.0)]"
            );
    }

    @Test
    public void getAndSetConfiguration() {
        Configuration configuration = getConfiguration();
        NumberConfiguration aspect = configuration.getAspect(NumberConfiguration.class);
        int minimalExponent = aspect.getMinimalExponent();
        ConfigurationService.setConfiguration(configuration.toBuilder()
            .configure(NumberConfiguration.class, (nc) -> nc.withMinimalExponent(8))
            .build()
        );
        assertThat(getConfiguration()
            .getAspectValue(NumberConfiguration.class, NumberConfiguration::getMinimalExponent)
        ).isEqualTo(8);
    }


    @Test
    public void testConfigurationAspects() {
        defaultConfiguration((con) -> con
            .configure(NumberConfiguration.class, c -> c.withMinimalExponent(4))
            .configure(TestConfigurationAspect.class, c -> c.withSomeInt(-1))
        );
        assertThat(getConfigurationAspect(NumberConfiguration.class).getMinimalExponent()).isEqualTo(4);

        ConfigurationService.withAspect(NumberConfiguration.class, (b) -> b.withMinimalExponent(6), () -> {
            assertThat(getConfigurationAspect(NumberConfiguration.class).getMinimalExponent()).isEqualTo(6);
            }
        );
        assertThat(getConfigurationAspect(NumberConfiguration.class).getMinimalExponent()).isEqualTo(4);

        defaultConfiguration((con) -> con.configure(NumberConfiguration.class,
            c -> c.withMinimalExponent(5))
        );
        assertThat(getConfigurationAspect(NumberConfiguration.class).getMinimalExponent()).isEqualTo(5);

        ConfigurationService.resetToDefaultDefaults();

        assertThat(getConfigurationAspect(NumberConfiguration.class).getMinimalExponent()).isEqualTo(4);


        withConfiguration((con) -> con
                .configure(TestConfigurationAspect.class, (tc) -> tc.withSomeInt(5))
                .configure(NumberConfiguration.class, (tc) -> tc.withMinimalExponent(3))
        , () -> {
                assertThat(getConfigurationAspect(TestConfigurationAspect.class).getSomeInt()).isEqualTo(5);
                assertThat(getConfigurationAspect(NumberConfiguration.class).getMinimalExponent()).isEqualTo(3);

        });


        assertThat(getConfigurationAspect(TestConfigurationAspect.class).getSomeInt()).isEqualTo(-1);
    }




}
