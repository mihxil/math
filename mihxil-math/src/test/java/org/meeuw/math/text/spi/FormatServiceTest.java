package org.meeuw.math.text.spi;

import org.junit.jupiter.api.Test;
import org.meeuw.math.text.configuration.*;
import org.meeuw.math.text.spi.test.InvalidConfigurationAspect;
import org.meeuw.math.text.spi.test.TestConfigurationAspect;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.meeuw.math.text.spi.FormatService.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class FormatServiceTest {

    @Test
    public void getFormat() {
        assertThat(getProviders()
            .map(AlgebraicElementFormatProvider::toString))
            .containsExactly(
                "TestFormatProvider [class org.meeuw.math.text.spi.test.TestConfigurationAspect, class org.meeuw.math.text.spi.test.InvalidConfigurationAspect]",
                "UncertainDoubleFormatProvider [class org.meeuw.math.text.configuration.NumberConfiguration, class org.meeuw.math.text.configuration.UncertaintyConfiguration]");
    }

    @Test
    public void getAndSetConfiguration() {
        Configuration configuration = FormatService.getConfiguration();
        NumberConfiguration aspect = configuration.getAspect(NumberConfiguration.class);
        int minimalExponent = aspect.getMinimalExponent();
        FormatService.setConfiguration(configuration.toBuilder().aspect(NumberConfiguration.class, (nc) -> nc.withMinimalExponent(8)).build());
        assertThat(FormatService.getConfiguration().getAspectValue(NumberConfiguration.class, NumberConfiguration::getMinimalExponent)).isEqualTo(8);
    }


    @Test
    public void testConfigurationAspects() {
        FormatService.defaultConfiguration((con) -> con
            .aspect(NumberConfiguration.class, c -> c.withMinimalExponent(4))
            .aspect(TestConfigurationAspect.class, c -> c.withSomeInt(-1))
        );
        assertThat(getConfigurationAspect(NumberConfiguration.class).getMinimalExponent()).isEqualTo(4);

        with(NumberConfiguration.class, (b) -> b.withMinimalExponent(6), () -> {
            assertThat(getConfigurationAspect(NumberConfiguration.class).getMinimalExponent()).isEqualTo(6);
            }
        );
        assertThat(getConfigurationAspect(NumberConfiguration.class).getMinimalExponent()).isEqualTo(4);

        defaultConfiguration((con) -> con.aspect(NumberConfiguration.class, c -> c.withMinimalExponent(5)));
        assertThat(getConfigurationAspect(NumberConfiguration.class).getMinimalExponent()).isEqualTo(5);

        with((con) -> con
                .aspect(TestConfigurationAspect.class, (tc) -> tc.withSomeInt(5))
                .aspect(NumberConfiguration.class, (tc) -> tc.withMinimalExponent(3))
            , () -> {
                assertThat(getConfigurationAspect(TestConfigurationAspect.class).getSomeInt()).isEqualTo(5);
                assertThat(getConfigurationAspect(NumberConfiguration.class).getMinimalExponent()).isEqualTo(3);

        });


        assertThat(getConfigurationAspect(TestConfigurationAspect.class).getSomeInt()).isEqualTo(-1);
    }


    @Test
    public void invalidConfigurationAspect() {
        assertThatThrownBy(() -> getConfigurationAspect(InvalidConfigurationAspect.class)).isInstanceOf(ConfigurationException.class);

    }


}
