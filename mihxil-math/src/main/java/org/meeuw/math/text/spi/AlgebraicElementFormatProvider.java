package org.meeuw.math.text.spi;

import java.text.Format;
import java.util.List;

import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.configuration.ConfigurationAspect;
import org.meeuw.configuration.Configuration;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public abstract class AlgebraicElementFormatProvider {

    public abstract Format getInstance(Configuration configuration);

    public abstract int weight(Class<? extends AlgebraicElement<?>> weight);

    public abstract List<Class<? extends ConfigurationAspect>> getConfigurationAspects();

    @Override
    public String toString() {
        StringBuilder build = new StringBuilder();
        build.append(getClass().getSimpleName());
        List<Class<? extends ConfigurationAspect>> configurationAspects = getConfigurationAspects();
        if (! configurationAspects.isEmpty()) {
            build.append(" ").append(configurationAspects);
        }
        return build.toString();
    }
}
