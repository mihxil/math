package org.meeuw.test.math.text.spi.test;


import lombok.*;

import java.util.Collections;
import java.util.List;

import org.meeuw.configuration.ConfigurationAspect;

/**
 * @author Michiel Meeuwissen
 */
@ToString
public class TestConfigurationAspect implements ConfigurationAspect {

    @With
    @Getter
    final int someInt;

    @lombok.Builder
    private TestConfigurationAspect(int someInt) {
        this.someInt = someInt;
    }

    public TestConfigurationAspect() {
        this(-1);
    }

    @Override
    public List<Class<?>> associatedWith() {
        return Collections.singletonList(TestFormatProvider.class);
    }
}
