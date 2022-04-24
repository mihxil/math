package org.meeuw.test.configuration.spi;

import java.util.Collections;
import java.util.List;

import org.meeuw.configuration.ConfigurationAspect;

public class Unregistered implements ConfigurationAspect {

    @Override
    public List<Class<?>> associatedWith() {
        return Collections.emptyList();
    }
}
