package org.meeuw.math.text.spi.test;


import lombok.With;

import org.meeuw.math.text.configuration.ConfigurationAspect;

/**
 * @author Michiel Meeuwissen
 */
public class InvalidConfigurationAspect implements ConfigurationAspect {

    @With
    final int someInt;

    public InvalidConfigurationAspect(int someInt) {
        this.someInt = someInt;
    }
}
