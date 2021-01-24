package org.meeuw.math.text.spi.test;

import java.text.Format;
import java.util.*;

import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.text.configuration.Configuration;
import org.meeuw.math.text.configuration.ConfigurationAspect;
import org.meeuw.math.text.spi.AlgebraicElementFormatProvider;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
public class TestFormatProvider extends AlgebraicElementFormatProvider {
    @Override
    public Format getInstance(Configuration configuration) {
        return null;
    }

    @Override
    public int weight(AlgebraicElement<?> weight) {
        return 0;
    }

    @Override
    public List<Class<? extends ConfigurationAspect>> getConfigurationAspects() {
        return Arrays.asList(TestConfigurationAspect.class, InvalidConfigurationAspect.class);
    }
}
