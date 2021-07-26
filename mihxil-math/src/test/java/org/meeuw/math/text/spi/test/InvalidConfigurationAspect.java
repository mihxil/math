package org.meeuw.math.text.spi.test;


import lombok.With;

import org.meeuw.configuration.ConfigurationAspect;

/**
 * Has no no-args constructor
 * @author Michiel Meeuwissen
 */
public class InvalidConfigurationAspect implements ConfigurationAspect {

    @With
    final int someInt;


    public InvalidConfigurationAspect(int someInt) {
        this.someInt = someInt;
    }
}
