package org.meeuw.test.configuration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import org.meeuw.configuration.*;
import org.meeuw.test.math.text.spi.test.InvalidConfigurationAspect;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.meeuw.configuration.ConfigurationService.getConfigurationAspect;

class ConfigurationServiceTest {


    @AfterAll
    public static void restoreDefaults() {
        Configuration configuration = ConfigurationService.getConfiguration();
        ConfigurationService.setConfiguration(configuration.toBuilder().defaults().build());
    }


    @Test
    public void invalidConfigurationAspect() {
        assertThatThrownBy(() -> getConfigurationAspect(InvalidConfigurationAspect.class)).isInstanceOf(ConfigurationException.class);

    }

}
