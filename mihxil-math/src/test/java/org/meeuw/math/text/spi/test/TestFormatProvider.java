package org.meeuw.math.text.spi.test;

import java.text.Format;
import java.util.*;

import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.configuration.Configuration;
import org.meeuw.configuration.ConfigurationAspect;
import org.meeuw.math.text.spi.AlgebraicElementFormatProvider;

/**
 * @author Michiel Meeuwissen
 */
public class TestFormatProvider extends AlgebraicElementFormatProvider {
    @Override
    public Format getInstance(Configuration configuration) {
        return null;
    }

    @Override
    public int weight(Class<? extends AlgebraicElement<?>> weight) {
        return 0;
    }

    @Override
    public List<Class<? extends ConfigurationAspect>> getConfigurationAspects() {
        return Arrays.asList(TestConfigurationAspect.class, InvalidConfigurationAspect.class);
    }
}
