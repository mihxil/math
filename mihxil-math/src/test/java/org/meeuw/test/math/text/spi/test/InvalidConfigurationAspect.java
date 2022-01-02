package org.meeuw.test.math.text.spi.test;


import lombok.ToString;
import lombok.With;

import java.util.Collections;
import java.util.List;

import org.meeuw.configuration.ConfigurationAspect;

/**
 * Has no no-args constructor
 * @author Michiel Meeuwissen
 */
@ToString
public class InvalidConfigurationAspect implements ConfigurationAspect {

    @With
    final int someInt;

    public InvalidConfigurationAspect() {
        someInt = 1;
        //throw new IllegalStateException();
    }



    public InvalidConfigurationAspect(int someInt) {
        this.someInt = someInt;
    }

    @Override
    public List<Class<?>> associatedWith() {
        return Collections.singletonList(TestFormatProvider.class);
    }
}
