package org.meeuw.math.text.spi;

import java.text.Format;
import java.util.List;

import org.meeuw.configuration.Configuration;
import org.meeuw.configuration.ConfigurationAspect;
import org.meeuw.math.abstractalgebra.AlgebraicElement;

import static org.meeuw.configuration.ConfigurationService.getConfiguration;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public abstract class AlgebraicElementFormatProvider {

    public abstract Format getInstance(Configuration configuration);

    public abstract int weight(Class<? extends AlgebraicElement<?>> weight);

    @Override
    public String toString() {
        StringBuilder build = new StringBuilder();
        build.append(getClass().getSimpleName());
        List<ConfigurationAspect> configurationAspects =
            getConfiguration().getConfigurationAspectsAssociatedWith(this.getClass());
        if (! configurationAspects.isEmpty()) {
            build.append(" ").append(configurationAspects);
        }
        return build.toString();
    }
}
