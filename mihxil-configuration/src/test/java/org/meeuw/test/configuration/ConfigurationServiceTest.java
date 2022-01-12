package org.meeuw.test.configuration;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

import org.meeuw.configuration.*;


class ConfigurationServiceTest {

    public static class Unregistered implements ConfigurationAspect {

        @Override
        public List<Class<?>> associatedWith() {
            return Collections.emptyList();
        }
    }

    @Test
    public void invalidConfigurationAspect() {
        Assertions.assertThatThrownBy(() -> ConfigurationService.getConfiguration().getAspect(Unregistered.class)).isInstanceOf(ConfigurationException.class);


    }

}
