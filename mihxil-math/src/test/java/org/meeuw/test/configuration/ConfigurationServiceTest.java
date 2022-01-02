package org.meeuw.test.configuration;

import org.junit.jupiter.api.*;

import org.meeuw.configuration.*;
import org.meeuw.test.math.text.spi.test.InvalidConfigurationAspect;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.meeuw.configuration.ConfigurationService.getConfigurationAspect;

class ConfigurationServiceTest {



    @Test
    @Disabled
    public void invalidConfigurationAspect() {
        assertThatThrownBy(() -> getConfigurationAspect(InvalidConfigurationAspect.class)).isInstanceOf(ConfigurationException.class);

    }

}
