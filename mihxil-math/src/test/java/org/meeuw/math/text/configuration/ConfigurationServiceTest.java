package org.meeuw.math.text.configuration;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class ConfigurationServiceTest {


    @Test
    public void test() {
        ConfigurationService.configureDefault((con) -> con.config(NumberConfiguration.class, c -> c.withMinimalExponent(4)));
        assertThat(ConfigurationService.getConfiguration(NumberConfiguration.class).getMinimalExponent()).isEqualTo(4);

        ConfigurationService.get().with(NumberConfiguration.class, (b) -> b.withMinimalExponent(6)).run(() -> {
            assertThat(ConfigurationService.getConfiguration(NumberConfiguration.class).getMinimalExponent()).isEqualTo(6);
            }
        );
        assertThat(ConfigurationService.getConfiguration(NumberConfiguration.class).getMinimalExponent()).isEqualTo(4);

        ConfigurationService.configureDefault((con) -> con.config(NumberConfiguration.class, c -> c.withMinimalExponent(5)));
        assertThat(ConfigurationService.getConfiguration(NumberConfiguration.class).getMinimalExponent()).isEqualTo(5);
    }

}
