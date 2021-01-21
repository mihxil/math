package org.meeuw.math.text.spi;

import org.junit.jupiter.api.Test;
import org.meeuw.math.text.configuration.NumberConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.text.spi.FormatServiceProvider.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class FormatServiceProviderTest {

    @Test
    public void getFormat() {
        assertThat(getProviders().map(AlgebraicElementFormatProvider::toString)).containsExactly("UncertainDoubleFormatProvider [class org.meeuw.math.text.configuration.NumberConfiguration, class org.meeuw.math.text.configuration.UncertaintyConfiguration]");
    }


    @Test
    public void testConfigurationAspects() {
        defaultConfiguration((con) -> con.config(NumberConfiguration.class, c -> c.withMinimalExponent(4)));
        assertThat(getConfigurationAspect(NumberConfiguration.class).getMinimalExponent()).isEqualTo(4);

        with(NumberConfiguration.class, (b) -> b.withMinimalExponent(6), () -> {
            assertThat(getConfigurationAspect(NumberConfiguration.class).getMinimalExponent()).isEqualTo(6);
            }
        );
        assertThat(getConfigurationAspect(NumberConfiguration.class).getMinimalExponent()).isEqualTo(4);

        defaultConfiguration((con) -> con.config(NumberConfiguration.class, c -> c.withMinimalExponent(5)));
        assertThat(getConfigurationAspect(NumberConfiguration.class).getMinimalExponent()).isEqualTo(5);
    }


}
