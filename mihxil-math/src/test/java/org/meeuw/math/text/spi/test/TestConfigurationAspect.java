package org.meeuw.math.text.spi.test;


import lombok.Getter;
import lombok.With;

import org.meeuw.configuration.ConfigurationAspect;

/**
 * @author Michiel Meeuwissen
 */
public class TestConfigurationAspect implements ConfigurationAspect {

    @With
    @Getter
    final int someInt;


    @lombok.Builder
    private TestConfigurationAspect(int someInt) {
        this.someInt = someInt;
    }
    public TestConfigurationAspect() {
        this(0);
    }
}
